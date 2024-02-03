package org.example.hackatonapi.services;


import org.example.hackatonapi.models.AlfaCurrency;
import org.example.hackatonapi.models.AlfabankCurrencyRate;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlfabankService implements BankService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ALFABANK_REQUEST_URL = "https://developerhub.alfabank.by:8273/partner/1.0.1/public/rates";

    public AlfabankCurrencyRate getCurrencies() {
        AlfabankCurrencyRate body = restTemplate.getForEntity(ALFABANK_REQUEST_URL, AlfabankCurrencyRate.class).getBody();
        assert body != null;
        body.setRates(body.getRates().stream().filter(alfaCurrency -> alfaCurrency.getBuyIso().equals("BYN")).toList());
        return body;
    }

    public List<CurrencyDTO> convertAlfabankCurrencyToDTO(AlfabankCurrencyRate currencyRate) {
        List<CurrencyDTO> currencyDTOS = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (AlfaCurrency alfaCurrency : currencyRate.getRates()) {
            LocalDate date = LocalDate.parse(alfaCurrency.getDate(), formatter);
            currencyDTOS.add(new CurrencyDTO(alfaCurrency.getSellIso(), alfaCurrency.getBuyRate(), alfaCurrency.getSellRate(), date));
        }
        return currencyDTOS;
    }

    @Override
    public CurrencyDTO getCurrencyRateForDate(String currencyCode, String date) {
        AlfabankCurrencyRate currencyRate = getCurrencies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (AlfaCurrency alfaCurrency : currencyRate.getRates()) {
            LocalDate currencyDate = LocalDate.parse(alfaCurrency.getDate(), formatter);

            if (alfaCurrency.getSellIso().equalsIgnoreCase(currencyCode) && currencyDate.isEqual(LocalDate.parse(date))) {
                return new CurrencyDTO(alfaCurrency.getSellIso(), alfaCurrency.getBuyRate(), alfaCurrency.getSellRate(), currencyDate);
            }
        }
        return null;
    }

    @Override
    public List<CurrencyDTO> getCurrencyRatesInDateRange(String currencyCode, String startDate, String endDate) {
        AlfabankCurrencyRate currencyRate = getCurrencies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<CurrencyDTO> currencyDTOs = new ArrayList<>();

        for (AlfaCurrency alfaCurrency : currencyRate.getRates()) {
            LocalDate currencyDate = LocalDate.parse(alfaCurrency.getDate(), formatter);

            if (alfaCurrency.getSellIso().equalsIgnoreCase(currencyCode) &&
                    !currencyDate.isBefore(LocalDate.parse(startDate)) &&
                    !currencyDate.isAfter(LocalDate.parse(endDate))) {
                currencyDTOs.add(new CurrencyDTO(
                        alfaCurrency.getSellIso(),
                        alfaCurrency.getBuyRate(),
                        alfaCurrency.getSellRate(),
                        currencyDate
                ));
            }
        }
        return currencyDTOs;
    }

}
