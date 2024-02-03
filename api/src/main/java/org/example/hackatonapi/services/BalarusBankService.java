package org.example.hackatonapi.services;

import org.example.hackatonapi.models.BelarusBankCurrencyRate;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BalarusBankService implements BankService {
    private static final String API_URL = "https://belarusbank.by/api/kurs_cards";

    RestTemplate restTemplate = new RestTemplate();

    public List<CurrencyDTO> getAllConvertedCurrencyRates() {
        BelarusBankCurrencyRate[] currencyRates = getCurrencyRates();
        return convertToCurrencyDTOList(currencyRates);
    }

    public BelarusBankCurrencyRate[] getCurrencyRates() {
        return restTemplate.getForObject(API_URL, BelarusBankCurrencyRate[].class);
    }

    public static List<CurrencyDTO> convertToCurrencyDTOList(BelarusBankCurrencyRate[] currencyRates) {
        List<CurrencyDTO> convertedRates = new ArrayList<>();

        for (BelarusBankCurrencyRate currencyRate : currencyRates) {
            LocalDate date = LocalDate.parse(
                    currencyRate.getKursDateTime(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );


            if (currencyRate.getUsdCardIn() > 0 || currencyRate.getUsdCardOut() > 0) {
                convertedRates.add(new CurrencyDTO("USD", currencyRate.getUsdCardIn(), currencyRate.getUsdCardOut(), date));
            }

            if (currencyRate.getEurCardIn() > 0 || currencyRate.getEurCardOut() > 0) {
                convertedRates.add(new CurrencyDTO("EUR", currencyRate.getEurCardIn(), currencyRate.getEurCardOut(), date));
            }

            if (currencyRate.getRubCardIn() > 0 || currencyRate.getRubCardOut() > 0) {
                convertedRates.add(new CurrencyDTO("RUB", currencyRate.getRubCardIn(), currencyRate.getRubCardOut(), date));
            }
        }

        return convertedRates;
    }

    private CurrencyDTO convertToCurrencyDTO(BelarusBankCurrencyRate currencyRate) {
        LocalDate date = LocalDate.parse(
                currencyRate.getKursDateTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        if (currencyRate.getUsdCardIn() > 0 || currencyRate.getUsdCardOut() > 0) {
            return new CurrencyDTO("USD", currencyRate.getUsdCardIn(), currencyRate.getUsdCardOut(), date);
        }

        if (currencyRate.getEurCardIn() > 0 || currencyRate.getEurCardOut() > 0) {
            return new CurrencyDTO("EUR", currencyRate.getEurCardIn(), currencyRate.getEurCardOut(), date);
        }

        if (currencyRate.getRubCardIn() > 0 || currencyRate.getRubCardOut() > 0) {
            return new CurrencyDTO("RUB", currencyRate.getRubCardIn(), currencyRate.getRubCardOut(), date);
        }

        return null;
    }

    @Override
    public CurrencyDTO getCurrencyRateForDate(String currencyCode, String date) {
        BelarusBankCurrencyRate[] currencyRates = getCurrencyRates();

        for (BelarusBankCurrencyRate currencyRate : currencyRates) {
            LocalDate currencyDate = LocalDateTime.parse(
                            currencyRate.getKursDateTime(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .toLocalDate();

            if ("USD".equalsIgnoreCase(currencyCode) && currencyDate.isEqual(LocalDate.parse(date))) {
                return convertToCurrencyDTO(currencyRate);
            }
            if ("EUR".equalsIgnoreCase(currencyCode) && currencyDate.isEqual(LocalDate.parse(date))) {
                return convertToCurrencyDTO(currencyRate);
            }
            if ("RUB".equalsIgnoreCase(currencyCode) && currencyDate.isEqual(LocalDate.parse(date))) {
                return convertToCurrencyDTO(currencyRate);
            }
        }
        return null;
    }

    @Override
    public List<CurrencyDTO> getCurrencyRatesInDateRange(String currencyCode, String startDate, String endDate) {
        BelarusBankCurrencyRate[] currencyRates = getCurrencyRates();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<CurrencyDTO> currencyDTOs = new ArrayList<>();

        for (BelarusBankCurrencyRate currencyRate : currencyRates) {
            LocalDate currencyDate = LocalDateTime.parse(
                    currencyRate.getKursDateTime(),
                    formatter
            ).toLocalDate();

            if (currencyRate.getUsdCardIn() > 0 || currencyRate.getUsdCardOut() > 0 ||
                    currencyRate.getEurCardIn() > 0 || currencyRate.getEurCardOut() > 0 ||
                    currencyRate.getRubCardIn() > 0 || currencyRate.getRubCardOut() > 0) {
                if (!currencyDate.isBefore(LocalDate.parse(startDate)) && !currencyDate.isAfter(LocalDate.parse(endDate))) {
                    currencyDTOs.add(convertToCurrencyDTO(currencyRate));
                }
            }
        }

        return currencyDTOs;
    }

}
