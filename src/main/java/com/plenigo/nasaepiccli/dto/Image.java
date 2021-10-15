package com.plenigo.nasaepiccli.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Image {

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String caption;
    private Coordinates centroidCoordinates;
    private Position dscovrJ2000;
    private Position lunarJ2000;
    private Position sunJ2000;
    private Altitude altitude;

}
