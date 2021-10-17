package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageRequestContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NasaEpicApiUrlResolverImpl implements NasaEpicApiUrlResolver {

    private static final String EPIC_API_URL_DATE_SEPARATOR = "-";

    @Value("${nasa.api.epic.url}")
    private String epicApiBaseUrl;

    @Value("${nasa.api.key}")
    private String apiKey;

    @Override
    public String resolveApiUrl(ImageRequestContext imageRequestContext) {
        StringBuilder apiUrlBuilder = new StringBuilder(epicApiBaseUrl);
        apiUrlBuilder.append("/").append(imageRequestContext.getImageColor().getColor());

        if (StringUtils.isNotBlank(imageRequestContext.getDate())) {
            apiUrlBuilder.append("/date/");
            appendDateToUrl(apiUrlBuilder, imageRequestContext.getDate(), EPIC_API_URL_DATE_SEPARATOR);
        }

        apiUrlBuilder.append("?api_key=").append(apiKey);

        return apiUrlBuilder.toString();
    }

}
