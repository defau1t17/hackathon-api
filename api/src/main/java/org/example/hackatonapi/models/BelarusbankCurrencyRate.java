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

    @JsonProperty("USDCARD_EURCARD_in")
    private double usdToEurIn;

    @JsonProperty("USDCARD_EURCARD_out")
    private double usdToEurOut;

    @JsonProperty("USDCARD_RUBCARD_in")
    private double usdToRubIn;

    @JsonProperty("USDCARD_RUBCARD_out")
    private double usdToRubOut;

    @JsonProperty("RUBCARD_EURCARD_in")
    private double rubToEurIn;

    @JsonProperty("RUBCARD_EURCARD_out")
    private double rubToEurOut;
}
