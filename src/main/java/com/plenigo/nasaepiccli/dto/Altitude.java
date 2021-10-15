package com.plenigo.nasaepiccli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Altitude {

    private double q0;
    private double q1;
    private double q2;
    private double q3;

}
