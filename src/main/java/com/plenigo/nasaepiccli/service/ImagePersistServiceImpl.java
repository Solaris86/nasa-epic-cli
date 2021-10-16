package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageList;
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
    public void persistImages(ImageList imageList, String basePath) {
        String capturedDate = imageList.getCapturedDate().toString();
        String imageLocation = basePath + "/" + capturedDate;
        Path imagePath = Path.of(imageLocation);

        if (!Files.exists(imagePath)) {
            logger.debug("Creating folder [{}] in supplied path [{}]", capturedDate, basePath);
            try {
                Files.createDirectory(imagePath);
                logger.debug("Folder successfully created, path [{}]", imagePath);
            } catch (IOException ex) {
                logger.error("An error occurred while trying to create directory [{}]", imageLocation, ex);
                throw new RuntimeException(ex.getMessage(), ex.getCause());
            }
        } else {
            logger.debug("Skipping folder creation since it already exists, path [{}]", imageLocation);
        }

        imageList.getImages().forEach(image -> {
            try {
                String pathName = imageLocation + "/" + image.getMetadata().getName() + ".jpg";
                ImageIO.write(image.getImage(), "jpg", new File(pathName));
            } catch (IOException ex) {
                logger.error("An error occurred while saving [{}] image to specified path", image.getMetadata().getName(), ex);
                throw new RuntimeException(ex.getMessage(), ex.getCause());
            }
        });

        logger.debug("[{}] images have been successfully saved to [{}]", imageList.getImages().size(), imageLocation);
    }

}
