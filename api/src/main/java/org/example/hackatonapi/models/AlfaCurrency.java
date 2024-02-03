package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AlfaCurrency {
    @JsonProperty("sellRate")
    private Double sellRate;
    @JsonProperty("sellIso")
    private String sellIso;
    @JsonProperty("sellCode")
    private Integer sellCode;
    @JsonProperty("buyRate")
    private Double buyRate;
    @JsonProperty("buyIso")
    private String buyIso;
    @JsonProperty("buyCode")
    private Integer buyCode;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("date")
    private String date;
}
