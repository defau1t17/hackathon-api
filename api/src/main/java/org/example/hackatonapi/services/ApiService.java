package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class ApiService {

    private final String baseUrl = "http://localhost:8080";

    public Set<String> getAllBanks() {
        String url = baseUrl + "/api/banks";
        ResponseEntity<Set> responseEntity = new RestTemplate().getForEntity(url, Set.class);
        return responseEntity.getBody();
    }

    public List<CurrencyDTO> getBankCurrencies(String bankName) {
        String url = baseUrl + "/api/banks/" + bankName + "/currencies";
        ResponseEntity<List> responseEntity = new RestTemplate().getForEntity(url, List.class);
        return responseEntity.getBody();
    }

    public CurrencyDTO getCurrencyRateForDate(String currencyCode, String bankName, String date) {
        String url = baseUrl + "/api/banks/rate?currencyCode=" + currencyCode + "&bankName=" + bankName + "&date=" + date;
        ResponseEntity<CurrencyDTO> responseEntity = new RestTemplate().getForEntity(url, CurrencyDTO.class);
        return responseEntity.getBody();
    }

    public List<CurrencyDTO> getCurrencyRatesInDateRange(String currencyCode, String bankName, String startDate, String endDate) {
        String url = baseUrl + "/api/banks/rates?currencyCode=" + currencyCode + "&bankName=" + bankName +
                "&startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<List> responseEntity = new RestTemplate().getForEntity(url, List.class);
        return responseEntity.getBody();
    }

    public byte[] getStatisticsAsPNG(String currencyCode, String bankName, String startDate, String endDate) {
        String url = baseUrl + "/api/banks/statistics/png?currencyCode=" + currencyCode + "&bankName=" + bankName +
                "&startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<byte[]> responseEntity = new RestTemplate().getForEntity(url, byte[].class);
        return responseEntity.getBody();
    }
}
