package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class NBRBRate {
    @JsonProperty("Cur_ID")
    private int Cur_ID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date Date;
    @JsonProperty("Cur_Abbreviation")
    private String Cur_Abbreviation;
    @JsonProperty("Cur_Scale")
    private int Cur_Scale;
    @JsonProperty("Cur_OfficialRate")
    private double Cur_OfficialRate;
}
