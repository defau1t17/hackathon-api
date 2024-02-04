package org.example.hackatonapi.tg_bot.services;

import org.example.hackatonapi.api.models.dto.CurrencyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final String baseUrl = "http://localhost:8080";

    public CurrencyDTO getCurrencyRateForDate(String currencyCode, String bankName, String date) {
        String url = baseUrl + "/api/banks/rate?currencyCode=" + currencyCode + "&bankName=" + bankName + "&date=" + date;

        try {
            ResponseEntity<CurrencyDTO> responseEntity = new RestTemplate().getForEntity(url, CurrencyDTO.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            handleNotFoundException(ex);
            return null;
        } catch (Exception ex) {
            handleGeneralException(ex);
            return null;
        }
    }

    public byte[] getStatisticsAsPNG(String currencyCode, String bankName, String startDate, String endDate) {
        String url = baseUrl + "/api/banks/statistics/png?currencyCode=" + currencyCode + "&bankName=" + bankName +
                "&startDate=" + startDate + "&endDate=" + endDate;

        try {
            ResponseEntity<byte[]> responseEntity = new RestTemplate().getForEntity(url, byte[].class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            handleNotFoundException(ex);
            return null;
        } catch (Exception ex) {
            handleGeneralException(ex);
            return null;
        }
    }

    private void handleNotFoundException(HttpClientErrorException.NotFound ex) {
        System.err.println("API call returned 404 Not Found error. Message: " + ex.getMessage());
    }

    private void handleGeneralException(Exception ex) {
        System.err.println("An error occurred during the API call. Message: " + ex.getMessage());
    }
}
