package org.example.hackatonapi.services;

import org.example.hackatonapi.models.CurrencyDTO;
import org.example.hackatonapi.models.NBRBCurrency;
import org.example.hackatonapi.models.NBRBRate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class NBRBCurrencyService {

    public NBRBCurrency[] GetCurrencies() {
        final String API_URL = "https://api.nbrb.by/exrates/currencies";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, NBRBCurrency[].class);
    }

    public NBRBRate[] GetRates() {
        final String API_URL = "https://api.nbrb.by/exrates/rates?periodicity=0";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, NBRBRate[].class);
    }

    public NBRBRate GetRateForCurrencyOnDate(String curr, String date) {
        final String API_URL = String.format("https://api.nbrb.by/exrates/rates/%s?parammode=2&ondate=%s", curr, date);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, NBRBRate.class);
    }

    public CurrencyDTO[] Convert(NBRBRate[] rates) {
        CurrencyDTO[] currencyDTOS = new CurrencyDTO[rates.length];

        for (int i = 0; i < rates.length; i++) {
            String name = rates[i].getCur_Abbreviation();
            double price = rates[i].getCur_OfficialRate();
            LocalDate date = rates[i].getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            System.out.println(date);

            currencyDTOS[i] = new CurrencyDTO(name, price, date);
        }

        return  currencyDTOS;
    }

}
