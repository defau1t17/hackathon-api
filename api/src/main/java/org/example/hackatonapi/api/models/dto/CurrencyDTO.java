package org.example.hackatonapi.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {
    private String shortName;
    private Double offBuyRate;
    private Double offSellRate;
    private LocalDate date;
}
