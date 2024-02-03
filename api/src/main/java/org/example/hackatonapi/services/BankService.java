package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;

public interface BankService {
    CurrencyDTO getCurrencyRateForDate(String currencyCode, String date);
}
