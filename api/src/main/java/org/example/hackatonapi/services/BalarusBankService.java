package org.example.hackatonapi.services;

import org.example.hackatonapi.models.BelarusBankCurrencyRate;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<CurrencyDTO> convertToCurrencyDTOList(BelarusBankCurrencyRate[] currencyRates) {
        return Arrays.stream(currencyRates)
                .map(this::convertToCurrencyDTO)
                .collect(Collectors.toList());
    }

    private CurrencyDTO convertToCurrencyDTO(BelarusBankCurrencyRate currencyRate) {
        CurrencyDTO currencyDTO = new CurrencyDTO();

        currencyDTO.setDate(
                LocalDateTime.parse(currencyRate.getKursDateTime(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        .toLocalDate());

        currencyDTO.setShortName("USD");
        currencyDTO.setOffBuyRate(currencyRate.getUsdCardIn());
        currencyDTO.setOffSellRate(currencyRate.getUsdCardOut());

        return currencyDTO;
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
}
