package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageColor;
import com.plenigo.nasaepiccli.model.ImageRequestContext;
import com.plenigo.nasaepiccli.model.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NasaEpicApiUrlResolverImplTest {

    private static final String EPIC_API_BASE_URL = "http://dummy.com";
    private static final String API_KEY = "dummyApiKey";
    private static final String CAPTURED_DATE = "2021-01-01";

    private ImageRequestContext imageRequestContext;

    private final NasaEpicApiUrlResolverImpl nasaEpicApiUrlResolver = new NasaEpicApiUrlResolverImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(nasaEpicApiUrlResolver, "epicApiBaseUrl", EPIC_API_BASE_URL);
        ReflectionTestUtils.setField(nasaEpicApiUrlResolver, "apiKey", API_KEY);

        imageRequestContext = ImageRequestContext.builder()
                .date(CAPTURED_DATE)
                .imageType(ImageType.JPG)
                .imageColor(ImageColor.NATURAL)
                .build();
    }

    @Test
    void resolveApiUrl() {
        final String expectApiUrl = EPIC_API_BASE_URL + "/natural/date/" + CAPTURED_DATE + "?api_key=" + API_KEY;
        final String resolvedApiUrl = nasaEpicApiUrlResolver.resolveApiUrl(imageRequestContext);

        assertEquals(expectApiUrl, resolvedApiUrl);
    }

    @Test
    void resolveApiUrlMissingDate() {
        imageRequestContext.setDate(null);

        final String expectApiUrl = EPIC_API_BASE_URL + "/natural?api_key=" + API_KEY;
        final String resolvedApiUrl = nasaEpicApiUrlResolver.resolveApiUrl(imageRequestContext);

        assertEquals(expectApiUrl, resolvedApiUrl);
    }
}