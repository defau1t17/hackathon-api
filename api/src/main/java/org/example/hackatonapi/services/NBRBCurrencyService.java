package org.example.hackatonapi.services;
import org.example.hackatonapi.models.NBRBCurrency;

import org.example.hackatonapi.models.NBRBRate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NBRBCurrencyService {

    public NBRBCurrency[] GetCurrencies() {
        final String API_URL = "https://api.nbrb.by/exrates/currencies";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, NBRBCurrency[].class);
    }

    public static NBRBRate[] GetRates() {
        final String API_URL = "https://api.nbrb.by/exrates/rates?periodicity=0";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, NBRBRate[].class);
    }


}
