package org.example.hackatonapi.api.services;


import org.example.hackatonapi.api.models.dto.CurrencyDTO;

import java.util.List;

public interface BankServiceInterface {
    CurrencyDTO getCurrencyRateForDate(String currencyCode, String date);

    List<CurrencyDTO> getCurrencyRatesInDateRange(String currencyCode, String startDate, String endDate);
}
