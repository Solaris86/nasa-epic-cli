package com.plenigo.nasaepiccli.service;

import com.plenigo.nasaepiccli.model.ImageRequestContext;

public interface NasaEpicApiUrlResolver extends NasaEpicUrlResolver {

    String resolveApiUrl(ImageRequestContext imageRequestContext);

}
