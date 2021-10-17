package com.plenigo.nasaepiccli.service;


import com.plenigo.nasaepiccli.model.ImageRequestContext;
import com.plenigo.nasaepiccli.model.ImageResponseContext;

public interface NasaEpicService {

    ImageResponseContext fetchImages(ImageRequestContext fetchContext);

}
