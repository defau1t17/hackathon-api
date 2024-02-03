package org.example.hackatonapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.models.enums.BankConsts;
import org.example.hackatonapi.services.AlfabankService;
import org.example.hackatonapi.services.BalarusBankService;
import org.example.hackatonapi.services.NBRBCurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

//    @GetMapping("/statistics")
//    @Operation(summary = "Get statistics for a specific currency within a specified time range and bank", description = "Get statistical data for a currency within a specified time range and bank")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK - Returns statistical data for the currency"),
//            @ApiResponse(responseCode = "404", description = "Not Found - If the data for the specified currency, bank, or time range is not found"),
//            @ApiResponse(responseCode = "400", description = "Bad Request - If start date is later than end date or for other invalid request parameters"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error - In case of server errors")
//    })
//    public ResponseEntity<StatisticsDTO> getStatistics(
//            @RequestParam String currencyCode,
//            @RequestParam String bankName,
//            @RequestParam String startDate,
//            @RequestParam String endDate) {
//        StatisticsDTO statistics = StatisticsService.getStatistics(currencyCode, bankName, startDate, endDate);
//
//        if (statistics == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(statistics);
//    }
}
