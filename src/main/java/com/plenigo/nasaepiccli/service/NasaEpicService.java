package com.plenigo.nasaepiccli.service;


import java.awt.image.BufferedImage;
import java.util.List;

public interface NasaEpicService {

    List<BufferedImage> fetchImages(String date, String color);

}
