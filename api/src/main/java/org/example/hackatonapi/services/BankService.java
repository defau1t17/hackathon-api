package org.example.hackatonapi.services;

import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.models.enums.BankConsts;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@Service
public class BankService {
    private final AlfabankService alfabankService;
    private final BalarusBankService balarusBankService;
    private final NBRBCurrencyService nbrbCurrencyService;

    public BankService(AlfabankService alfabankService, BalarusBankService balarusBankService, NBRBCurrencyService nbrbCurrencyService) {
        this.alfabankService = alfabankService;
        this.balarusBankService = balarusBankService;
        this.nbrbCurrencyService = nbrbCurrencyService;
    }

    public Set<String> getAllBanks() {
        return BankConsts.ALL_BANKS;
    }

    public List<CurrencyDTO> getBankCurrencies(String bankName) {
        return switch (bankName.toUpperCase()) {
            case BankConsts.ALFA -> alfabankService.convertAlfabankCurrencyToDTO(alfabankService.getCurrencies());
            case BankConsts.BELBANK -> balarusBankService.getAllConvertedCurrencyRates();
            case BankConsts.NBRB ->
                    Arrays.asList(nbrbCurrencyService.convertToCurrencyDTO(nbrbCurrencyService.getRates()));
            default -> null;
        };
    }

    public CurrencyDTO getCurrencyRateForDate(String bankName, String currencyCode, String date) {
        return switch (bankName.toUpperCase()) {
            case BankConsts.ALFA -> alfabankService.getCurrencyRateForDate(currencyCode, date);
            case BankConsts.BELBANK -> balarusBankService.getCurrencyRateForDate(currencyCode, date);
            case BankConsts.NBRB -> nbrbCurrencyService.getCurrencyRateForDate(currencyCode, date);
            default -> null;
        };
    }

    public List<CurrencyDTO> getCurrencyRatesInDateRange(String bankName, String currencyCode, String startDate, String endDate) {
        return switch (bankName.toUpperCase()) {
            case BankConsts.ALFA -> alfabankService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
            case BankConsts.BELBANK -> balarusBankService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
            case BankConsts.NBRB -> nbrbCurrencyService.getCurrencyRatesInDateRange(currencyCode, startDate, endDate);
            default -> Collections.emptyList();
        };
    }

    public List<CurrencyDTO> getHistoricalData(String currencyCode, String bankName, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            return Collections.emptyList();
        }

        return getCurrencyRatesInDateRange(bankName, currencyCode, startDate, endDate);
    }

    public XYDataset createDataset(List<CurrencyDTO> historicalData) {
        XYSeries series = new XYSeries("Exchange Rate");

        for (CurrencyDTO dataPoint : historicalData) {
            series.add(dataPoint.getDate().toEpochDay(), dataPoint.getOffBuyRate());
        }

        return new XYSeriesCollection(series);
    }

    public JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Currency Statistics",
                "Date",
                "Exchange Rate",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesFilled(0, true);
        plot.setRenderer(renderer);

        return chart;
    }

    public byte[] chartToBytes(JFreeChart chart) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(byteArrayOutputStream, chart, 800, 600);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.err.println("Error converting chart to bytes: " + e.getMessage());
            return null;
        }
    }
}

