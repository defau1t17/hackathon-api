package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BelarusBankCurrencyRate {
    @JsonProperty("kurs_date_time")
    private String kursDateTime;

    @JsonProperty("USDCARD_in")
    private Double usdCardIn;

    @JsonProperty("USDCARD_out")
    private Double usdCardOut;

    @JsonProperty("EURCARD_in")
    private Double eurCardIn;

    @JsonProperty("EURCARD_out")
    private Double eurCardOut;

    @JsonProperty("RUBCARD_in")
    private Double rubCardIn;

    @JsonProperty("RUBCARD_out")
    private Double rubCardOut;
}
