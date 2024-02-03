package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AlfaBankCurrencyRate {
    @JsonProperty("rates")
    private List<AlfaCurrency> rates;

}
