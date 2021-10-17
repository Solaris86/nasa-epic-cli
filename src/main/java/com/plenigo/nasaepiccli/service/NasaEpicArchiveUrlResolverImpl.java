package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageRequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NasaEpicArchiveUrlResolverImpl implements NasaEpicArchiveUrlResolver {

    private static final String EPIC_ARCHIVE_URL_DATE_SEPARATOR = "/";

    @Value("${nasa.archive.epic.url}")
    private String epicArchiveBaseUrl;

    @Override
    public String resolveArchiveUrl(ImageRequestContext imageContext, String imageName) {
        StringBuilder archiveUrlBuilder = new StringBuilder(epicArchiveBaseUrl);

        archiveUrlBuilder.append("/").append(imageContext.getImageColor().getColor()).append("/");
        appendDateToUrl(archiveUrlBuilder, imageContext.getDate(), EPIC_ARCHIVE_URL_DATE_SEPARATOR);
        archiveUrlBuilder.append("/").append(imageContext.getImageType().getName()).append("/");
        archiveUrlBuilder.append(imageName).append(imageContext.getImageType().getExtension());


        return archiveUrlBuilder.toString();
    }

}
