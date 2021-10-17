package com.plenigo.nasaepiccli.service;

import java.time.LocalDate;

public interface NasaEpicUrlResolver {

    default void appendDateToUrl(StringBuilder urlBuilder, String date, String separator) {
        LocalDate imageDate = LocalDate.parse(date);
        urlBuilder.append(imageDate.getYear()).append(separator);
        urlBuilder.append(imageDate.getMonthValue()).append(separator);
        urlBuilder.append(imageDate.getDayOfMonth());
    }

}
