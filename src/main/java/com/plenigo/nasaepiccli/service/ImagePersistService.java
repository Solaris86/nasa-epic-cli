package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageList;

public interface ImagePersistService {

    void persistImages(ImageList imageList, String basePath);

}
