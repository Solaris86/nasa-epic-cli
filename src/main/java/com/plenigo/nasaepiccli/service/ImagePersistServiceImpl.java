package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.exception.FilePersistenceException;
import com.plenigo.nasaepiccli.model.ImageResponseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImagePersistServiceImpl implements ImagePersistService {

    private static final Logger logger = LoggerFactory.getLogger(ImagePersistServiceImpl.class);

    @Override
    public void persistImages(ImageResponseContext imageResponseContext, String basePath) {
        String capturedDate = imageResponseContext.getCapturedDate().toString();
        String imageLocation = basePath + "/" + capturedDate;
        Path imagePath = Path.of(imageLocation);

        if (!Files.exists(imagePath)) {
            logger.debug("Creating folder [{}] in supplied path [{}]", capturedDate, basePath);
            try {
                Files.createDirectory(imagePath);
                logger.debug("Folder successfully created, path [{}]", imagePath);
            } catch (IOException ex) {
                logger.error("An error occurred while trying to create directory [{}]", imageLocation, ex);
                throw new FilePersistenceException(String.format("An error occurred while trying to create directory [%s]", imageLocation), ex.getCause());
            }
        } else {
            logger.debug("Skipping folder creation since it already exists, path [{}]", imageLocation);
        }

        // TODO fix when false is returned
        imageResponseContext.getImages().forEach(imageContext -> {
            try {
                String pathName = imageLocation + "/" + imageContext.getMetadata().getName() + imageResponseContext.getImageType().getExtension();
                final boolean imageSaved = ImageIO.write(imageContext.getImage(), imageResponseContext.getImageType().getFormatName(), new File(pathName));
                if (!imageSaved) {
                    logger.debug("Image [{}] could not be saved.", imageContext.getMetadata().getName());
                }
            } catch (IOException ex) {
                logger.error("An error occurred while saving [{}] image to specified path", imageContext.getMetadata().getName(), ex);
                throw new FilePersistenceException(String.format("An error occurred while saving [%s] image to specified path",
                        imageContext.getMetadata().getName()), ex.getCause());
            }
        });

        logger.debug("[{}] images have been successfully saved to [{}]", imageResponseContext.getImages().size(), imageLocation);
    }

}
