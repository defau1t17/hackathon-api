package org.example.hackatonapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.models.enums.BankConsts;
import org.example.hackatonapi.services.AlfabankService;
import org.example.hackatonapi.services.BalarusBankService;
import org.example.hackatonapi.services.NBRBCurrencyService;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/banks")
public class BankController {
    public BankController(AlfabankService alfabankService, BalarusBankService balarusBankService, NBRBCurrencyService nbrbCurrencyService) {
        this.alfabankService = alfabankService;
        this.balarusBankService = balarusBankService;
        this.nbrbCurrencyService = nbrbCurrencyService;
    }

    private final AlfabankService alfabankService;
    private final BalarusBankService balarusBankService;
    private final NBRBCurrencyService nbrbCurrencyService;

    @GetMapping
    public ResponseEntity<Set<String>> getAllBanks() {
        Set<String> banks = BankConsts.ALL_BANKS;

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

        List<CurrencyDTO> currencies;

        switch (bankName.toUpperCase()) {
            case BankConsts.ALFA:
                currencies = alfabankService.convertAlfabankCurrencyToDTO(alfabankService.getCurrencies());
                break;
            case BankConsts.BELBANK:
                currencies = balarusBankService.getAllConvertedCurrencyRates();
                break;
            case BankConsts.NBRB:
                currencies = Arrays.asList(nbrbCurrencyService.convertToCurrencyDTO(nbrbCurrencyService.getRates()));
                break;
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/rate")
    public ResponseEntity<CurrencyDTO> getCurrencyRateForDate(
            @RequestParam String currencyCode,
            @RequestParam String bankName,
            @RequestParam String date) {

        if (!BankConsts.ALL_BANKS.contains(bankName.toUpperCase())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        CurrencyDTO currencyDTO;
        switch (bankName.toUpperCase()) {
            case BankConsts.ALFA:
                currencyDTO = alfabankService.getCurrencyRateForDate(currencyCode, date);
                break;
            case BankConsts.BELBANK:
                currencyDTO = balarusBankService.getCurrencyRateForDate(currencyCode, date);
                break;
            case BankConsts.NBRB:
                currencyDTO = nbrbCurrencyService.getCurrencyRateForDate(currencyCode, date);
                break;
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (currencyDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(currencyDTO);
    }

    @GetMapping("/rates")
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

        List<CurrencyDTO> currencyDTOs;
        switch (bankName.toUpperCase()) {
            case BankConsts.ALFA:
                currencyDTOs = alfabankService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
                break;
            case BankConsts.BELBANK:
                currencyDTOs = balarusBankService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
                break;
            case BankConsts.NBRB:
                currencyDTOs = nbrbCurrencyService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
                break;
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (currencyDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(currencyDTOs);
    }

    @GetMapping(value = "/statistics/png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getStatistics(
            @RequestParam String currencyCode,
            @RequestParam String bankName,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        List<CurrencyDTO> historicalData = getHistoricalData(currencyCode, bankName, startDate, endDate);

        assert historicalData != null;
        XYDataset dataset = createDataset(historicalData);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Currency Statistics",
                "Date",
                "Exchange Rate",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesFilled(0, true);
        plot.setRenderer(renderer);


        byte[] chartBytes = chartToBytes(chart);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(chartBytes);
    }

    private List<CurrencyDTO> getHistoricalData(String currencyCode, String bankName, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            return null;
        }

        List<CurrencyDTO> historicalData;

        switch (bankName.toUpperCase()) {
            case BankConsts.ALFA:
                historicalData = alfabankService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
                break;
            case BankConsts.BELBANK:
                historicalData = balarusBankService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
                break;
            case BankConsts.NBRB:
                historicalData = nbrbCurrencyService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
                break;
            default:
                return null;
        }

        return historicalData;
    }


    private XYDataset createDataset(List<CurrencyDTO> historicalData) {
        XYSeries series = new XYSeries("Exchange Rate");

        for (CurrencyDTO dataPoint : historicalData) {
            series.add(dataPoint.getDate().toEpochDay(), dataPoint.getOffBuyRate());
        }

        return new XYSeriesCollection(series);
    }

    private byte[] chartToBytes(JFreeChart chart) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(byteArrayOutputStream, chart, 800, 600);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
