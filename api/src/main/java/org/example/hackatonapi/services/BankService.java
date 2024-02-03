package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;

import java.util.List;

public interface BankService {
    CurrencyDTO getCurrencyRateForDate(String currencyCode, String date);

    List<CurrencyDTO> getCurrencyRatesInDateRange(String currencyCode, String startDate, String endDate);
}
