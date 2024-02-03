package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BelarusbankCurrencyRate {
    @JsonProperty("kurs_date_time")
    private String kursDateTime;

    @JsonProperty("USDCARD_in")
    private double usdCardIn;

    @JsonProperty("USDCARD_out")
    private double usdCardOut;

    @JsonProperty("EURCARD_in")
    private double eurCardIn;

    @JsonProperty("EURCARD_out")
    private double eurCardOut;

    @JsonProperty("RUBCARD_in")
    private double rubCardIn;

    @JsonProperty("RUBCARD_out")
    private double rubCardOut;
}
