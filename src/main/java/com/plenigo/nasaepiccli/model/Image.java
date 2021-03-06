package com.plenigo.nasaepiccli.model;

import com.plenigo.nasaepiccli.dto.ImageMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Image {

    private ImageMetadata metadata;
    private BufferedImage image;
    private LocalDate capturedDate;

}
