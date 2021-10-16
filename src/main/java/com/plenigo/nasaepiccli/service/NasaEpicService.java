package com.plenigo.nasaepiccli.service;


import com.plenigo.nasaepiccli.model.Image;

import java.util.List;

public interface NasaEpicService {

    List<Image> fetchImages(String date, String color);

}
