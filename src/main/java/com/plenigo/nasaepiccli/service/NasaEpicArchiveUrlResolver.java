package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageRequestContext;

public interface NasaEpicArchiveUrlResolver extends NasaEpicUrlResolver {

    String resolveArchiveUrl(ImageRequestContext imageRequestContext, String imageName);
}
