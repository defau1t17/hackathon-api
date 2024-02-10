package org.example.hackatonapi.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.hackatonapi.api.models.dto.CurrencyDTO;
import org.example.hackatonapi.api.models.enums.BankConsts;
import org.example.hackatonapi.api.services.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

@RestController
@RequestMapping("/api/banks")
@Tag(name = "Bank Controller", description = "Operations related to banks and currencies")
public class BankController {
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    private final BankService bankService;

    @GetMapping
    @Operation(summary = "Get all available banks", description = "Returns a set of all available banks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Returns a set of available banks"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - In case of server errors")
    })
    public ResponseEntity<Set<String>> getAllBanks() {
        Set<String> banks = bankService.getAllBanks();
        return ResponseEntity.status(HttpStatus.OK).body(banks);
    }

    @GetMapping("/{bankName}/currencies")
    @Operation(summary = "Get list of available currencies for a specific bank", description = "Get list of available currencies for a specific bank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Returns an array of available currencies"),
            @ApiResponse(responseCode = "404", description = "Not Found - If the bank with the specified name is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - In case of server errors")
    })
    public ResponseEntity<List<CurrencyDTO>> getBankCurrencies(@PathVariable String bankName) {
        if (!BankConsts.ALL_BANKS.contains(bankName.toUpperCase())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<CurrencyDTO> currencies = bankService.getBankCurrencies(bankName);

        if (currencies == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/rate")
    @Operation(summary = "Get currency rate for a specific date", description = "Returns the currency rate for a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Returns the currency rate for the specified date"),
            @ApiResponse(responseCode = "400", description = "Bad Request - If the bank name is not valid or the date format is incorrect"),
            @ApiResponse(responseCode = "404", description = "Not Found - If the bank or currency is not found for the specified date"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - In case of server errors")
    })
    public ResponseEntity<CurrencyDTO> getCurrencyRateForDate(
            @RequestParam String currencyCode,
            @RequestParam String bankName,
            @RequestParam String date) {
        if (!BankConsts.ALL_BANKS.contains(bankName.toUpperCase())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        CurrencyDTO currencyDTO = bankService.getCurrencyRateForDate(bankName, currencyCode, date);

        if (currencyDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(currencyDTO);
    }

    @GetMapping("/rates")
    @Operation(summary = "Get currency rates in a date range", description = "Returns currency rates within a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Returns currency rates within the specified date range"),
            @ApiResponse(responseCode = "400", description = "Bad Request - If the bank name is not valid, or the date range is incorrect"),
            @ApiResponse(responseCode = "404", description = "Not Found - If the bank or currency is not found for the specified date range"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - In case of server errors")
    })
    public ResponseEntity<List<CurrencyDTO>> getCurrencyRatesInDateRange(
            @RequestParam String currencyCode,
            @RequestParam String bankName,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        if (!BankConsts.ALL_BANKS.contains(bankName.toUpperCase())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (start.isAfter(end)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<CurrencyDTO> currencyDTOs = bankService.getCurrencyRatesInDateRange(bankName, currencyCode, startDate, endDate);

        if (currencyDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(currencyDTOs);
    }

    @GetMapping(value = "/statistics/png", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "Get statistical chart as PNG", description = "Returns a statistical chart as PNG image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Returns a statistical chart as PNG image"),
            @ApiResponse(responseCode = "404", description = "Not Found - If historical data is not available for the specified parameters"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - In case of server errors")
    })
    public ResponseEntity<byte[]> getStatistics(
            @RequestParam String currencyCode,
            @RequestParam String bankName,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        List<CurrencyDTO> historicalData = bankService.getHistoricalData(currencyCode, bankName, startDate, endDate);

        if (historicalData == null || historicalData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        XYDataset dataset = bankService.createDataset(historicalData);
        JFreeChart chart = bankService.createChart(dataset);

        byte[] chartBytes = bankService.chartToBytes(chart);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(chartBytes);
    }
}
