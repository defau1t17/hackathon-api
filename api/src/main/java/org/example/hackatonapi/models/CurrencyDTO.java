package org.example.hackatonapi.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CurrencyDTO {
    private String shortName;
    private double offBuyRate;
    private double offSellRate;
    private LocalDate date;

    public CurrencyDTO(String name, double buy, LocalDate date) {
        this.shortName = name;
        this.offBuyRate = buy;
        this.offSellRate = buy;
        this.date = date;
    }

}
