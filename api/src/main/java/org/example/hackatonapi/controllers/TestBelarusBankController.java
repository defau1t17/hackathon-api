package org.example.hackatonapi.controllers;

import org.example.hackatonapi.models.BelarusbankCurrencyRate;
import org.example.hackatonapi.models.CurrencyDTO;
import org.example.hackatonapi.services.BalarusBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/currency-rates")
public class TestBelarusBankController {
    @Autowired
    private BalarusBankService balarusBankService;

    @GetMapping
    public BelarusbankCurrencyRate[] getAllCurrencyRates() {
        return balarusBankService.getCurrencyRates();
    }

    @GetMapping("/converted")
    public ResponseEntity<List<CurrencyDTO>> getAllConvertedCurrencyRates() {
        BelarusbankCurrencyRate[] currencyRates = balarusBankService.getCurrencyRates();
        List<CurrencyDTO> convertedRates = convertToCurrencyDTOList(currencyRates);

        return ResponseEntity.status(HttpStatus.OK).body(convertedRates);
    }

    private List<CurrencyDTO> convertToCurrencyDTOList(BelarusbankCurrencyRate[] currencyRates) {
        return Arrays.stream(currencyRates)
                .map(this::convertToCurrencyDTO)
                .collect(Collectors.toList());
    }

    private CurrencyDTO convertToCurrencyDTO(BelarusbankCurrencyRate currencyRate) {
        CurrencyDTO currencyDTO = new CurrencyDTO();

        currencyDTO.setDate(LocalDateTime.parse(currencyRate.getKursDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate());

        currencyDTO.setShortName("USD");
        currencyDTO.setOffBuyRate(currencyRate.getUsdCardIn());
        currencyDTO.setOffSellRate(currencyRate.getUsdCardOut());

        return currencyDTO;
    }
}
