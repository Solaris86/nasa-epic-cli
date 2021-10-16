package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.dto.ImageMetadata;
import com.plenigo.nasaepiccli.model.Image;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    private static final String EPIC_API_URL_DATE_SEPARATOR = "-";
    private static final String EPIC_ARCHIVE_URL_DATE_SEPARATOR = "/";
    private static final String EPIC_API_RESPONSE_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Value("${nasa.api.epic.url}")
    private String epicApiBaseUrl;

    @Value("${nasa.archive.epic.url}")
    private String epicArchiveBaseUrl;

    @Value("${nasa.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public List<Image> fetchImages(String date, String color) {
        ResponseEntity<ImageMetadata[]> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(createApiUrl(date, color), ImageMetadata[].class);
        } catch (RestClientException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }

        ImageMetadata[] fetchedImages = responseEntity.getBody();
        if (ArrayUtils.isEmpty(fetchedImages)) {
            throw new RuntimeException("No images were fetched for requested date");
        }

        // TODO handle case when date is not present
        if (StringUtils.isBlank(date)) {
            date = fetchedImages[0].getDate().toLocalDate().toString();
        }
        String baseArchiveUrl = createBaseArchiveUrl(date, color);
        return Arrays.stream(fetchedImages)
                .map(imageMetadata -> {
                    try {
                        String archiveUrl = baseArchiveUrl + imageMetadata.getName() + ".png";
                        BufferedImage bufferedImage = ImageIO.read(new URL(archiveUrl));
                        return Image.builder()
                                .metadata(imageMetadata)
                                .image(bufferedImage)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(), e.getCause());
                    }
                })
                .collect(Collectors.toList());
    }

    private String createApiUrl(String date, String color) {
        StringBuilder apiUrl = new StringBuilder(epicApiBaseUrl);
        apiUrl.append("/").append(color);

        if (StringUtils.isNotBlank(date)) {
            apiUrl.append("/date/");
            appendDateToUrl(apiUrl, date, EPIC_API_URL_DATE_SEPARATOR);
        }

        apiUrl.append("?api_key=").append(apiKey);

        return apiUrl.toString();
    }

    private String createBaseArchiveUrl(String date, String color) {
        StringBuilder archiveUrl = new StringBuilder(epicArchiveBaseUrl);
        archiveUrl.append("/").append(color).append("/");
        appendDateToUrl(archiveUrl, date, EPIC_ARCHIVE_URL_DATE_SEPARATOR);
        archiveUrl.append("/png/");

        return archiveUrl.toString();
    }

    private void appendDateToUrl(StringBuilder baseUrl, String date, String separator) {
        LocalDate imageDate = LocalDate.parse(date);
        baseUrl.append(imageDate.getYear()).append(separator);
        baseUrl.append(imageDate.getMonthValue()).append(separator);
        baseUrl.append(imageDate.getDayOfMonth());
    }
}
