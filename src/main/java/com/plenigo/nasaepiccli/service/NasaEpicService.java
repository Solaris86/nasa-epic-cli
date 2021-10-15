package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.dto.Image;

import java.util.List;

public interface NasaEpicService {

    List<Image> fetchImageMetadata(String date, String color);

}
