package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final String baseUrl = "http://localhost:8080";

    public CurrencyDTO getCurrencyRateForDate(String currencyCode, String bankName, String date) {
        String url = baseUrl + "/api/banks/rate?currencyCode=" + currencyCode + "&bankName=" + bankName + "&date=" + date;
        ResponseEntity<CurrencyDTO> responseEntity = new RestTemplate().getForEntity(url, CurrencyDTO.class);
        return responseEntity.getBody();
    }

    public byte[] getStatisticsAsPNG(String currencyCode, String bankName, String startDate, String endDate) {
        String url = baseUrl + "/api/banks/statistics/png?currencyCode=" + currencyCode + "&bankName=" + bankName +
                "&startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<byte[]> responseEntity = new RestTemplate().getForEntity(url, byte[].class);
        return responseEntity.getBody();
    }
}
