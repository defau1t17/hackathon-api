package org.example.hackatonapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {
    private String shortName;
    private double offBuyRate;
    private double offSellRate;
    private LocalDate date;

}
