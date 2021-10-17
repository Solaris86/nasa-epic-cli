package com.plenigo.nasaepiccli.model;

import com.plenigo.nasaepiccli.constants.CommandConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ImageType {

    PNG(CommandConstants.IMAGE_TYPE_NAME_PNG, "png", ".png"),
    JPG(CommandConstants.IMAGE_TYPE_NAME_JPG, "jpg", ".jpg"),
    THUMBNAIL(CommandConstants.IMAGE_TYPE_NAME_THUMBS, "jpg", ".jpg");

    private final String name;
    private final String formatName;
    private final String extension;

    public static ImageType forType(String type) {
        return Arrays.stream(values())
                .filter(imageType -> imageType.getName().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported image type: " + type));
    }

}
