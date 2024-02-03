package org.example.hackatonapi.controllers;

import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.services.BalarusBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/belarusbank")
public class TestBelarusBankController {

    private final BalarusBankService balarusBankService;

    @Autowired
    public TestBelarusBankController(BalarusBankService balarusBankService) {
        this.balarusBankService = balarusBankService;
    }

    @GetMapping("/converted")
    public ResponseEntity<List<CurrencyDTO>> getAllConvertedCurrencyRates() {
        List<CurrencyDTO> convertedRates = balarusBankService.getAllConvertedCurrencyRates();

        return ResponseEntity.status(HttpStatus.OK).body(convertedRates);
    }
}
