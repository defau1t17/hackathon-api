package org.example.hackatonapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NBRBCurrency {
    @JsonProperty("Cur_ID")
    private int Cur_ID;
    @JsonProperty("Cur_ParentID")
    private int Cur_ParentID;
    @JsonProperty("Cur_Abbreviation")
    private String Cur_Abbreviation;
    @JsonProperty("Cur_Name")
    private String Cur_Name;
    @JsonProperty("Cur_Scale")
    private int Cur_Scale;
    @JsonProperty("Cur_Periodicity")
    private int Cur_Periodicity;
}
