package org.example.hackatonapi.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CurrencyDTO {
    private String shortName;
    private double offBuyRate;
    private double offSellRate;
    private LocalDate date;
}
