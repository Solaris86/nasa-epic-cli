package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.dto.ImageMetadata;
import com.plenigo.nasaepiccli.model.Image;
import com.plenigo.nasaepiccli.model.ImageResponseContext;
import com.plenigo.nasaepiccli.model.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ImagePersistServiceImplTest {

    private static final String CAPTURED_DATE = "2021-01-01";
    private static final String BASE_PATH = "C:/";

    private ImageMetadata imageMetadata;
    private ImageResponseContext imageResponseContext;

    @Mock
    private BufferedImage image;

    @Mock
    private Path createdPath;

    @Captor
    private ArgumentCaptor<BufferedImage> imageCaptor;

    @Captor
    private ArgumentCaptor<String> formatNameCaptor;

    @Captor
    private ArgumentCaptor<File> fileCaptor;

    @Captor
    private ArgumentCaptor<Path> pathCaptor;

    private final ImagePersistService imagePersistService = new ImagePersistServiceImpl();

    @BeforeEach
    void setUp() {
        imageMetadata = ImageMetadata.builder()
                .name("image01")
                .date(LocalDateTime.now())
                .caption("caption")
                .build();

        imageResponseContext = ImageResponseContext.builder()
                .images(Collections.singletonList(Image.builder()
                                .metadata(imageMetadata)
                                .image(image)
                                .capturedDate(LocalDate.parse(CAPTURED_DATE))
                        .build()))
                .capturedDate(LocalDate.parse(CAPTURED_DATE))
                .imageType(ImageType.JPG)
                .build();
    }

    @Test
    void persistImageSuccess() {
        try (final MockedStatic<Files> filesMock = mockStatic(Files.class);
             final MockedStatic<ImageIO> imageIOMock = mockStatic(ImageIO.class)) {
            filesMock.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            filesMock.when(() -> Files.createDirectory(any(Path.class))).thenReturn(createdPath);
            imageIOMock.when(() -> ImageIO.write(any(BufferedImage.class), anyString(), any(File.class))).thenReturn(true);

            imagePersistService.persistImages(imageResponseContext, BASE_PATH);
        }
    }
}