package org.example.hackatonapi.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AlfabankCurrencyRate {
    @JsonProperty("rates")
    private List<AlfaCurrency> rates;

}
