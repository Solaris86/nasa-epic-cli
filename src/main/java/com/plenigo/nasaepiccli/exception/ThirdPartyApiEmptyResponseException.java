package com.plenigo.nasaepiccli.exception;

public class ThirdPartyApiEmptyResponseException extends RuntimeException {

    public ThirdPartyApiEmptyResponseException(String message) {
        super(message);
    }
}
