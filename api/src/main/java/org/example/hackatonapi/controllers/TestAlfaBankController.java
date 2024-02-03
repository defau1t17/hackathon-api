package org.example.hackatonapi.controllers;

import org.example.hackatonapi.models.AlfabankCurrencyRate;
import org.example.hackatonapi.services.AlfabankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alfabank")
public class TestAlfaBankController {
    public TestAlfaBankController(AlfabankService alfabankService) {
        this.alfabankService = alfabankService;
    }

    private final AlfabankService alfabankService;

    @GetMapping
    public ResponseEntity<AlfabankCurrencyRate> getCurrencies() {
        return ResponseEntity.ok(alfabankService.getCurrencies());
    }
}