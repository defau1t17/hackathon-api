package org.example.hackatonapi.api.services;

import org.example.hackatonapi.api.models.NBRBRate;
import org.example.hackatonapi.api.models.dto.CurrencyDTO;
import org.example.hackatonapi.api.services.interfaces.BankServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.List;

@Service
public class NBRBCurrencyService implements BankServiceInterface {

    private final static String RATES_API_URL = "https://api.nbrb.by/exrates/rates?periodicity=0";
    private final static String RATE_FOR_CURRENCY_ON_DATE_API_URL_FORMAT = "https://api.nbrb.by/exrates/rates/%s?parammode=2&ondate=%s";

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

    @Override
    public List<CurrencyDTO> getCurrencyRatesInDateRange(String currencyCode, String startDate, String endDate) {
        NBRBRate[] rates = getRates();
        List<CurrencyDTO> currencyDTOs = new ArrayList<>();

        for (NBRBRate rate : rates) {
            LocalDate currencyDate = rate.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            if (currencyCode.equalsIgnoreCase(rate.getCur_Abbreviation()) &&
                    !currencyDate.isBefore(LocalDate.parse(startDate)) &&
                    !currencyDate.isAfter(LocalDate.parse(endDate))) {
                currencyDTOs.add(convertRateToCurrencyDTO(rate));
            }
        }

        return currencyDTOs;
    }
}
