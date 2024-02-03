package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AlfaCurrency {
    @JsonProperty("sellRate")
    private double sellRate;
    @JsonProperty("sellIso")
    private String sellIso;
    @JsonProperty("sellCode")
    private int sellCode;
    @JsonProperty("buyRate")
    private double buyRate;
    @JsonProperty("buyIso")
    private String buyIso;
    @JsonProperty("buyCode")
    private int buyCode;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("date")
    private String date;
}
