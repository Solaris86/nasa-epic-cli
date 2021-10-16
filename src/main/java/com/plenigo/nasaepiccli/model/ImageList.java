package com.plenigo.nasaepiccli.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImageList {

    private List<Image> images;
    private LocalDate capturedDate;

}
