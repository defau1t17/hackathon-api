package org.example.hackatonapi.services;


import org.example.hackatonapi.models.AlfaCurrency;
import org.example.hackatonapi.models.AlfabankCurrencyRate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Currency;
import java.util.List;

@Service
public class AlfabankService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ALFABANK_REQUEST_URL = "https://developerhub.alfabank.by:8273/partner/1.0.1/public/rates";

    public AlfabankCurrencyRate getCurrencies(){
        return restTemplate.getForEntity(ALFABANK_REQUEST_URL, AlfabankCurrencyRate.class).getBody();
    }


}
