package com.plenigo.nasaepiccli.service;


import com.plenigo.nasaepiccli.model.ImageList;

public interface NasaEpicService {

    ImageList fetchImages(String date, String color);

}
