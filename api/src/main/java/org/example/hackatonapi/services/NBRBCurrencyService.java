package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.models.NBRBCurrency;
import org.example.hackatonapi.models.NBRBRate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.stream.Stream;

@Service
public class NBRBCurrencyService {

    private final String CURRENCIES_API_URL = "https://api.nbrb.by/exrates/currencies";
    private final String RATES_API_URL = "https://api.nbrb.by/exrates/rates?periodicity=0";
    private final String RATE_FOR_CURRENCY_ON_DATE_API_URL_FORMAT = "https://api.nbrb.by/exrates/rates/%s?parammode=2&ondate=%s";

    RestTemplate restTemplate = new RestTemplate();

    public NBRBCurrency[] getCurrencies() {
        return restTemplate.getForObject(CURRENCIES_API_URL, NBRBCurrency[].class);
    }

    public NBRBRate[] getRates() {
        return restTemplate.getForObject(RATES_API_URL, NBRBRate[].class);
    }

    public NBRBRate getRateForCurrencyOnDate(String curr, String date) {
        String apiURL = String.format(RATE_FOR_CURRENCY_ON_DATE_API_URL_FORMAT, curr, date);
        return restTemplate.getForObject(apiURL, NBRBRate.class);
    }

    public CurrencyDTO[] convertToCurrencyDTO(NBRBRate[] rates) {
        return Stream.of(rates)
                .map(this::convertRateToCurrencyDTO)
                .toArray(CurrencyDTO[]::new);
    }

    private CurrencyDTO convertRateToCurrencyDTO(NBRBRate rate) {
        String name = rate.getCur_Abbreviation();
        double price = rate.getCur_OfficialRate();
        LocalDate date = rate.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        return new CurrencyDTO(name, price, price, date);
    }
}
