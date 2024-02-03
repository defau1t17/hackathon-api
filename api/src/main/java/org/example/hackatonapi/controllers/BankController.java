package org.example.hackatonapi.controllers;

import org.example.hackatonapi.models.enums.BankConsts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/banks")
public class BankController {

    @GetMapping
    public ResponseEntity<Set<String>> getAllBanks() {
        Set<String> banks = Stream.of(BankConsts.ALFA, BankConsts.NBRB, BankConsts.BELBANK)
                .collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.OK).body(banks);
    }

//    @GetMapping("/{bankName}/currencies")
//    @Operation(summary = "Get list of available currencies for a specific bank", description = "Get list of available currencies for a specific bank")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK - Returns an array of available currencies"),
//            @ApiResponse(responseCode = "404", description = "Not Found - If the bank with the specified name is not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error - In case of server errors")
//    })
//    public ResponseEntity<List<CurrencyDTO>> getCurrenciesForBank(@PathVariable String bankName) {
//        List<CurrencyDTO> currencies = CurrencyService.getCurrenciesByBank(bankName);
//
//        if (currencies.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(currencies);
//    }
//
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
