package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class ApiService {

    @Value("${api.base.url}")
    private String baseUrl;

    RestTemplate restTemplate = new RestTemplate();

    public Set<String> getAllBanks() {
        String url = baseUrl + "/api/banks";
        return restTemplate.getForObject(url, Set.class);
    }

    public List<CurrencyDTO> getBankCurrencies(String bankName) {
        String url = baseUrl + "/api/banks/{bankName}/currencies";
        return restTemplate.getForObject(url, List.class, bankName);
    }

    public CurrencyDTO getCurrencyRateForDate(String currencyCode, String bankName, String date) {
        String url = baseUrl + "/api/banks/rate?currencyCode={currencyCode}&bankName={bankName}&date={date}";
        return restTemplate.getForObject(url, CurrencyDTO.class, currencyCode, bankName, date);
    }

    public List getCurrencyRatesInDateRange(String currencyCode, String bankName, String startDate, String endDate) {
        String url = baseUrl + "/api/banks/rates?currencyCode={currencyCode}&bankName={bankName}&startDate={startDate}&endDate={endDate}";
        return restTemplate.getForObject(url, List.class, currencyCode, bankName, startDate, endDate);
    }

    public byte[] getStatisticsChart(String currencyCode, String bankName, String startDate, String endDate) {
        String url = baseUrl + "/api/banks/statistics/png?currencyCode={currencyCode}&bankName={bankName}&startDate={startDate}&endDate={endDate}";
        return restTemplate.getForObject(url, byte[].class, currencyCode, bankName, startDate, endDate);
    }
}
