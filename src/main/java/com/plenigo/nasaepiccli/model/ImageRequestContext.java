package com.plenigo.nasaepiccli.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImageRequestContext {

    private String date;
    private ImageColor imageColor;
    private ImageType imageType;

}
