package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.dto.ImageMetadata;
import com.plenigo.nasaepiccli.exception.ThirdPartyApiEmptyResponseException;
import com.plenigo.nasaepiccli.exception.ThirdPartyApiException;
import com.plenigo.nasaepiccli.exception.ThirdPartyArchiveException;
import com.plenigo.nasaepiccli.model.ImageColor;
import com.plenigo.nasaepiccli.model.ImageRequestContext;
import com.plenigo.nasaepiccli.model.ImageResponseContext;
import com.plenigo.nasaepiccli.model.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NasaEpicServiceImplTest {

    private static final String DUMMY_API_URL = "http://dummy.api.com";
    private static final String DUMMY_ARCHIVE_URL = "http://dummy.archive.com";

    @Mock
    private NasaEpicApiUrlResolver apiUrlResolver;

    @Mock
    private NasaEpicArchiveUrlResolver archiveUrlResolver;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BufferedImage bufferedImage;

    @Captor
    private ArgumentCaptor<ImageRequestContext> imageRequestContextCaptor;

    @Captor
    private ArgumentCaptor<String> apiUrlCaptor;

    @Captor
    private ArgumentCaptor<URL> dummyUrlCaptor;

    @Captor
    private ArgumentCaptor<String> imageNameCaptor;

    private ImageRequestContext imageRequestContext;
    private ImageMetadata[] fetchedImages;

    @InjectMocks
    private NasaEpicServiceImpl nasaEpicService;

    @BeforeEach
    void setUp() {
        imageRequestContext = ImageRequestContext.builder()
                .date("2021-01-01")
                .imageType(ImageType.JPG)
                .imageColor(ImageColor.NATURAL)
                .build();

        fetchedImages = new ImageMetadata[1];
        fetchedImages[0] = ImageMetadata.builder()
                .name("dummyImage")
                .date(LocalDateTime.now())
                .caption("dummyCaption")
                .build();
    }

    @Test
    void fetchImagesSuccess() {
        when(apiUrlResolver.resolveApiUrl(any(ImageRequestContext.class))).thenReturn(DUMMY_API_URL);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(fetchedImages);
        when(archiveUrlResolver.resolveArchiveUrl(any(ImageRequestContext.class), anyString())).thenReturn(DUMMY_ARCHIVE_URL);

        try (MockedStatic<ImageIO> mockedImage = mockStatic(ImageIO.class)) {
            mockedImage.when(() -> ImageIO.read(new URL(DUMMY_ARCHIVE_URL))).thenReturn(bufferedImage);

            ImageResponseContext imageResponseContext = nasaEpicService.fetchImages(imageRequestContext);

            verify(apiUrlResolver, times(1)).resolveApiUrl(imageRequestContextCaptor.capture());
            assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());

            verify(restTemplate, times(1)).getForObject(apiUrlCaptor.capture(), any());
            assertEquals(DUMMY_API_URL, apiUrlCaptor.getValue());

            verify(archiveUrlResolver, times(1)).resolveArchiveUrl(imageRequestContextCaptor.capture(), imageNameCaptor.capture());
            assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());
            assertEquals(fetchedImages[0].getName(), imageNameCaptor.getValue());

            mockedImage.verify(() -> ImageIO.read(dummyUrlCaptor.capture()), times(1));
            assertEquals(DUMMY_ARCHIVE_URL, dummyUrlCaptor.getValue().toString());

            assertNotNull(imageResponseContext.getImages());
            assertEquals(1, imageResponseContext.getImages().size());
            assertNotNull(imageResponseContext.getImages().get(0));
            assertEquals(imageRequestContext.getImageType(), imageResponseContext.getImageType());
            assertEquals(imageRequestContext.getDate(), imageResponseContext.getCapturedDate().toString());
        }

    }

    @Test
    void fetchImagesSuccessEmptyCapturedDate() {
        LocalDateTime responseDateTime = LocalDateTime.now();
        imageRequestContext.setDate(null);
        fetchedImages[0].setDate(responseDateTime);

        when(apiUrlResolver.resolveApiUrl(any(ImageRequestContext.class))).thenReturn(DUMMY_API_URL);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(fetchedImages);
        when(archiveUrlResolver.resolveArchiveUrl(any(ImageRequestContext.class), anyString())).thenReturn(DUMMY_ARCHIVE_URL);

        try (MockedStatic<ImageIO> mockedImage = mockStatic(ImageIO.class)) {
            mockedImage.when(() -> ImageIO.read(new URL(DUMMY_ARCHIVE_URL))).thenReturn(bufferedImage);

            ImageResponseContext imageResponseContext = nasaEpicService.fetchImages(imageRequestContext);

            verify(apiUrlResolver, times(1)).resolveApiUrl(imageRequestContextCaptor.capture());
            assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());

            verify(restTemplate, times(1)).getForObject(apiUrlCaptor.capture(), any());
            assertEquals(DUMMY_API_URL, apiUrlCaptor.getValue());

            verify(archiveUrlResolver, times(1)).resolveArchiveUrl(imageRequestContextCaptor.capture(), imageNameCaptor.capture());
            assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());
            assertEquals(fetchedImages[0].getName(), imageNameCaptor.getValue());

            mockedImage.verify(() -> ImageIO.read(dummyUrlCaptor.capture()), times(1));
            assertEquals(DUMMY_ARCHIVE_URL, dummyUrlCaptor.getValue().toString());

            assertNotNull(imageResponseContext.getImages());
            assertEquals(1, imageResponseContext.getImages().size());
            assertNotNull(imageResponseContext.getImages().get(0));
            assertEquals(imageRequestContext.getImageType(), imageResponseContext.getImageType());
            assertEquals(imageRequestContext.getDate(), imageResponseContext.getCapturedDate().toString());
        }

    }

    @Test
    void fetchImagesEpicApiError() {
        when(apiUrlResolver.resolveApiUrl(any(ImageRequestContext.class))).thenReturn(DUMMY_API_URL);
        when(restTemplate.getForObject(anyString(), any())).thenThrow(RestClientException.class);

        assertThrows(ThirdPartyApiException.class, () -> nasaEpicService.fetchImages(imageRequestContext));

        verify(apiUrlResolver, times(1)).resolveApiUrl(imageRequestContextCaptor.capture());
        assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());

        verify(restTemplate, times(1)).getForObject(apiUrlCaptor.capture(), any());
        assertEquals(DUMMY_API_URL, apiUrlCaptor.getValue());
    }

    @Test
    void fetchImagesEmptyEpicApiResponse() {
        when(apiUrlResolver.resolveApiUrl(any(ImageRequestContext.class))).thenReturn(DUMMY_API_URL);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new ImageMetadata[] {});

        assertThrows(ThirdPartyApiEmptyResponseException.class, () -> nasaEpicService.fetchImages(imageRequestContext));

        verify(apiUrlResolver, times(1)).resolveApiUrl(imageRequestContextCaptor.capture());
        assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());

        verify(restTemplate, times(1)).getForObject(apiUrlCaptor.capture(), any());
        assertEquals(DUMMY_API_URL, apiUrlCaptor.getValue());
    }

    @Test
    void fetchImagesEpicArchiveError() {
        when(apiUrlResolver.resolveApiUrl(any(ImageRequestContext.class))).thenReturn(DUMMY_API_URL);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(fetchedImages);
        when(archiveUrlResolver.resolveArchiveUrl(any(ImageRequestContext.class), anyString())).thenReturn(DUMMY_ARCHIVE_URL);

        try (MockedStatic<ImageIO> mockedImageIO = mockStatic(ImageIO.class)) {
            mockedImageIO.when(() -> ImageIO.read(new URL(DUMMY_ARCHIVE_URL))).thenThrow(IOException.class);

            assertThrows(ThirdPartyArchiveException.class, () -> nasaEpicService.fetchImages(imageRequestContext));

            verify(apiUrlResolver, times(1)).resolveApiUrl(imageRequestContextCaptor.capture());
            assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());

            verify(restTemplate, times(1)).getForObject(apiUrlCaptor.capture(), any());
            assertEquals(DUMMY_API_URL, apiUrlCaptor.getValue());

            verify(archiveUrlResolver, times(1)).resolveArchiveUrl(imageRequestContextCaptor.capture(), imageNameCaptor.capture());
            assertEquals(imageRequestContext, imageRequestContextCaptor.getValue());
            assertEquals(fetchedImages[0].getName(), imageNameCaptor.getValue());

            mockedImageIO.verify(() -> ImageIO.read(dummyUrlCaptor.capture()), times(1));
            assertEquals(DUMMY_ARCHIVE_URL, dummyUrlCaptor.getValue().toString());
        }

    }
}