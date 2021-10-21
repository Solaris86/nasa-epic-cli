package com.plenigo.nasaepiccli.service;

import java.text.DecimalFormat;
import java.time.LocalDate;

public interface NasaEpicUrlResolver {

    default void appendDateToUrl(StringBuilder urlBuilder, String date, String separator) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        LocalDate imageDate = LocalDate.parse(date);
        urlBuilder.append(imageDate.getYear()).append(separator);
        urlBuilder.append(decimalFormat.format(imageDate.getMonthValue())).append(separator);
        urlBuilder.append(decimalFormat.format(imageDate.getDayOfMonth()));
    }

}
