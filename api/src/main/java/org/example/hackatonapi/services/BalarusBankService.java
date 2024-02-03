package org.example.hackatonapi.services;

import org.example.hackatonapi.models.BelarusbankCurrencyRate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BalarusBankService {
    private static final String API_URL = "https://belarusbank.by/api/kurs_cards";

    public BelarusbankCurrencyRate[] getCurrencyRates() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, BelarusbankCurrencyRate[].class);
    }
}
