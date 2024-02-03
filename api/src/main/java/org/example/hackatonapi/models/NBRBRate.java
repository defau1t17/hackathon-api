package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class NBRBRate {
    private int Cur_ID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date Date;
    private String Cur_Abbreviation;
    private int Cur_Scale;
    private double Cur_OfficialRate;
}
