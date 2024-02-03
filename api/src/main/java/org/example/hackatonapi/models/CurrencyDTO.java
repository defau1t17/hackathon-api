package org.example.hackatonapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    private String shortName;
    private double offBuyRate;
    private double offSellRate;
    private LocalDate date;

}
