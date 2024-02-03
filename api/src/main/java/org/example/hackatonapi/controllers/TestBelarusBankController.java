package org.example.hackatonapi.controllers;

import org.example.hackatonapi.models.BelarusbankCurrencyRate;
import org.example.hackatonapi.services.BalarusBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency-rates")
public class TestBelarusBankController {
    @Autowired
    private BalarusBankService currencyRateService;

    @GetMapping
    public BelarusbankCurrencyRate[] getAllCurrencyRates() {
        return currencyRateService.getCurrencyRates();
    }
}
