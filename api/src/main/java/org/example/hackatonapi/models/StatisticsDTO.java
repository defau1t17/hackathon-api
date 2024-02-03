package org.example.hackatonapi.models;

import lombok.Data;

@Data
public class StatisticsDTO {
    private double averageRate;
    private double maxRate;
    private double minRate;
    private double standardDeviation;
}
