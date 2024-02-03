package org.example.hackatonapi.services;

import org.example.hackatonapi.models.BelarusbankCurrencyRate;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalarusBankService {
    private static final String API_URL = "https://belarusbank.by/api/kurs_cards";

    RestTemplate restTemplate = new RestTemplate();

    public List<CurrencyDTO> getAllConvertedCurrencyRates() {
        BelarusbankCurrencyRate[] currencyRates = getCurrencyRates();
        return convertToCurrencyDTOList(currencyRates);
    }

    public BelarusbankCurrencyRate[] getCurrencyRates() {
        return restTemplate.getForObject(API_URL, BelarusbankCurrencyRate[].class);
    }

    private List<CurrencyDTO> convertToCurrencyDTOList(BelarusbankCurrencyRate[] currencyRates) {
        return Arrays.stream(currencyRates)
                .map(this::convertToCurrencyDTO)
                .collect(Collectors.toList());
    }

    private CurrencyDTO convertToCurrencyDTO(BelarusbankCurrencyRate currencyRate) {
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
}
