package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.models.NBRBCurrency;
import org.example.hackatonapi.models.NBRBRate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.stream.Stream;

@Service
public class NBRBCurrencyService implements BankService {

    private final String RATES_API_URL = "https://api.nbrb.by/exrates/rates?periodicity=0";
    private final String RATE_FOR_CURRENCY_ON_DATE_API_URL_FORMAT = "https://api.nbrb.by/exrates/rates/%s?parammode=2&ondate=%s";

    RestTemplate restTemplate = new RestTemplate();

    public NBRBRate[] getRates() {
        return restTemplate.getForObject(RATES_API_URL, NBRBRate[].class);
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

    public NBRBRate getRateForCurrencyOnDate(String curr, String date) {
        String apiURL = String.format(RATE_FOR_CURRENCY_ON_DATE_API_URL_FORMAT, curr, date);
        return restTemplate.getForObject(apiURL, NBRBRate.class);
    }

    @Override
    public CurrencyDTO getCurrencyRateForDate(String currencyCode, String date) {
        NBRBRate rate = getRateForCurrencyOnDate(currencyCode, date);

        if (rate != null) {
            return convertRateToCurrencyDTO(rate);
        }
        return null;
    }

}
