package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.dto.Image;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NasaEpicServiceImpl implements NasaEpicService {

    @Value("${nasa.api.epic.url}")
    private String epicApiBaseUrl;

    @Value("${nasa.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public List<Image> fetchImageMetadata(String date, String color) {
        String epicApiUrl = createEpicApiUrl(date, color);

        ResponseEntity<Image[]> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(epicApiUrl, Image[].class);
        } catch (RestClientException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }

        return Arrays.asList(responseEntity.getBody());
    }

    private String createEpicApiUrl(String date, String color) {
        StringBuilder url = new StringBuilder(epicApiBaseUrl);
        url.append("/").append(color);

        if (StringUtils.isNotBlank(date)) {
            LocalDate imageDate = LocalDate.parse(date);
            url.append("/date/");
            url.append(imageDate.getYear()).append("-");
            url.append(imageDate.getMonthValue()).append("-");
            url.append(imageDate.getDayOfMonth());
        }

        url.append("?api_key=").append(apiKey);

        return url.toString();
    }
}
