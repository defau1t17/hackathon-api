package org.example.hackatonapi.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlfabankCurrencyDTO {
        private String shortName;
        private double offBuyRate;
        private double offSellRate;
        private LocalDateTime DateTime;

}
