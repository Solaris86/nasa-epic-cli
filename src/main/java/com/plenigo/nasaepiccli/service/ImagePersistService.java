package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageResponseContext;

public interface ImagePersistService {

    void persistImages(ImageResponseContext imageResponseContext, String basePath);

}
