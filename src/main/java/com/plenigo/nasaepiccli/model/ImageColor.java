package com.plenigo.nasaepiccli.model;

import com.plenigo.nasaepiccli.constants.CommandConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ImageColor {

    NATURAL(CommandConstants.IMAGE_COLOR_NATURAL),
    ENHANCED(CommandConstants.IMAGE_COLOR_ENHANCED);

    private final String color;

    public static ImageColor forColor(String color) {
        return Arrays.stream(values())
                .filter(imageColor -> imageColor.getColor().equals(color))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported image color: " + color));
    }
}
