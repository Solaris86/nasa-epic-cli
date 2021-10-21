package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageColor;
import com.plenigo.nasaepiccli.model.ImageRequestContext;
import com.plenigo.nasaepiccli.model.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NasaEpicArchiveUrlResolverImplTest {

    private static final String EPIC_ARCHIVE_BASE_URL = "http://dummy.com";
    private static final String CAPTURED_DATE = "2021-01-01";
    private static final String IMAGE_NAME = "image01";

    private ImageRequestContext imageRequestContext;

    private NasaEpicArchiveUrlResolver archiveUrlResolver = new NasaEpicArchiveUrlResolverImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(archiveUrlResolver, "epicArchiveBaseUrl", EPIC_ARCHIVE_BASE_URL);

        imageRequestContext = ImageRequestContext.builder()
                .date(CAPTURED_DATE)
                .imageType(ImageType.JPG)
                .imageColor(ImageColor.NATURAL)
                .build();
    }

    @Test
    void resolveArchiveUrl() {
        String expectedArchiveUrl = EPIC_ARCHIVE_BASE_URL + "/natural/2021/01/01/jpg/" + IMAGE_NAME + ".jpg";
        String resolvedArchiveUrl = archiveUrlResolver.resolveArchiveUrl(imageRequestContext, IMAGE_NAME);

        assertEquals(expectedArchiveUrl, resolvedArchiveUrl);
    }
}