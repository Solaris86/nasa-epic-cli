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

    private final NasaEpicApiUrlResolver apiUrlResolver;
    private final NasaEpicArchiveUrlResolver archiveUrlResolver;
    private final RestTemplate restTemplate;

    @Override
    public ImageResponseContext fetchImages(ImageRequestContext imageRequestContext) {
        logger.debug("Fetching image metadata via NASA EPIC API using fetch context [{}]", imageRequestContext);

        ImageMetadata[] fetchedImages;
        try {
            fetchedImages = restTemplate.getForObject(apiUrlResolver.resolveApiUrl(imageRequestContext), ImageMetadata[].class);
        } catch (RestClientException ex) {
            logger.error("An error occurred while communicating with NASA EPIC API", ex.getCause());
            throw new ThirdPartyApiException("An error occurred while communicating with NASA EPIC API", ex.getCause());
        }

        if (ArrayUtils.isEmpty(fetchedImages)) {
            logger.error("No image metadata found for specified date [{}]", imageRequestContext.getDate());
            throw new ThirdPartyApiEmptyResponseException(String.format("No image metadata found for specified date [%s]", imageRequestContext.getDate()));
        }

        if (StringUtils.isBlank(imageRequestContext.getDate())) {
            imageRequestContext.setDate(fetchedImages[0].getDate().toLocalDate().toString());
        }

        logger.debug("Downloading images form NASA EPIC archive for date [{}]", imageRequestContext.getDate());
        List<Image> images = Arrays.stream(fetchedImages)
                .map(imageMetadata -> {
                    try {
                        String archiveUrl = archiveUrlResolver.resolveArchiveUrl(imageRequestContext, imageMetadata.getName());
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
                .capturedDate(LocalDate.parse(imageRequestContext.getDate()))
                .imageType(imageRequestContext.getImageType())
                .build();
    }

}
