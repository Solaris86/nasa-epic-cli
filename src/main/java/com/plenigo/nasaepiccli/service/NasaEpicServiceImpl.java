package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.dto.ImageMetadata;
import com.plenigo.nasaepiccli.exception.ThirdPartyApiEmptyResponseException;
import com.plenigo.nasaepiccli.exception.ThirdPartyApiException;
import com.plenigo.nasaepiccli.exception.ThirdPartyArchiveException;
import com.plenigo.nasaepiccli.model.Image;
import com.plenigo.nasaepiccli.model.ImageRequestContext;
import com.plenigo.nasaepiccli.model.ImageResponseContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NasaEpicServiceImpl implements NasaEpicService {

    private static final Logger logger = LoggerFactory.getLogger(NasaEpicServiceImpl.class);
    private static final String EPIC_API_URL_DATE_SEPARATOR = "-";
    private static final String EPIC_ARCHIVE_URL_DATE_SEPARATOR = "/";

    @Value("${nasa.api.epic.url}")
    private String epicApiBaseUrl;

    @Value("${nasa.archive.epic.url}")
    private String epicArchiveBaseUrl;

    @Value("${nasa.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public ImageResponseContext fetchImages(ImageRequestContext fetchContext) {
        logger.debug("Fetching image metadata via NASA EPIC API using fetch context [{}]", fetchContext);

        ImageMetadata[] fetchedImages;
        try {
            fetchedImages = restTemplate.getForObject(createApiUrl(fetchContext), ImageMetadata[].class);
        } catch (RestClientException ex) {
            logger.error("An error occurred while communicating with NASA EPIC API", ex.getCause());
            throw new ThirdPartyApiException("An error occurred while communicating with NASA EPIC API", ex.getCause());
        }

        if (ArrayUtils.isEmpty(fetchedImages)) {
            logger.error("No image metadata found for specified date [{}]", fetchContext.getDate());
            throw new ThirdPartyApiEmptyResponseException(String.format("No image metadata found for specified date [%s]", fetchContext.getDate()));
        }

        if (StringUtils.isBlank(fetchContext.getDate())) {
            fetchContext.setDate(fetchedImages[0].getDate().toLocalDate().toString());
        }
        String baseArchiveUrl = createBaseArchiveUrl(fetchContext);

        logger.debug("Downloading images form NASA EPIC archive for date [{}]", fetchContext.getDate());
        List<Image> images = Arrays.stream(fetchedImages)
                .map(imageMetadata -> {
                    try {
                        String archiveUrl = baseArchiveUrl + imageMetadata.getName() + fetchContext.getImageType().getExtension();
                        BufferedImage bufferedImage = ImageIO.read(new URL(archiveUrl));
                        return Image.builder()
                                .metadata(imageMetadata)
                                .image(bufferedImage)
                                .build();
                    } catch (IOException ex) {
                        logger.error("Error occurred while downloading images from NASA EPIC archive for date [{}]", imageMetadata.getDate());
                        throw new ThirdPartyArchiveException(String.format("Error occurred while downloading images from NASA EPIC archive for date [%s]",
                                imageMetadata.getDate()), ex);
                    }
                })
                .collect(Collectors.toList());

        return ImageResponseContext.builder()
                .images(images)
                .capturedDate(LocalDate.parse(fetchContext.getDate()))
                .imageType(fetchContext.getImageType())
                .build();
    }

    private String createApiUrl(ImageRequestContext fetchContext) {
        StringBuilder apiUrl = new StringBuilder(epicApiBaseUrl);
        apiUrl.append("/").append(fetchContext.getImageColor().getColor());

        if (StringUtils.isNotBlank(fetchContext.getDate())) {
            apiUrl.append("/date/");
            appendDateToUrl(apiUrl, fetchContext.getDate(), EPIC_API_URL_DATE_SEPARATOR);
        }

        apiUrl.append("?api_key=").append(apiKey);

        return apiUrl.toString();
    }

    private String createBaseArchiveUrl(ImageRequestContext fetchContext) {
        StringBuilder archiveUrl = new StringBuilder(epicArchiveBaseUrl);
        archiveUrl.append("/").append(fetchContext.getImageColor().getColor()).append("/");
        appendDateToUrl(archiveUrl, fetchContext.getDate(), EPIC_ARCHIVE_URL_DATE_SEPARATOR);
        archiveUrl.append("/").append(fetchContext.getImageType().getName()).append("/");

        return archiveUrl.toString();
    }

    private void appendDateToUrl(StringBuilder baseUrl, String date, String separator) {
        LocalDate imageDate = LocalDate.parse(date);
        baseUrl.append(imageDate.getYear()).append(separator);
        baseUrl.append(imageDate.getMonthValue()).append(separator);
        baseUrl.append(imageDate.getDayOfMonth());
    }
}
