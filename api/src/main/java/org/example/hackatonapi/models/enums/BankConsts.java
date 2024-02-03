package org.example.hackatonapi.models.enums;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BankConsts {
    private BankConsts() {
    }

    public static final String ALFA = "ALFA";
    public static final String NBRB = "NBRB";
    public static final String BELBANK = "BELBANK";

    public static final Set<String> ALL_BANKS = Stream.of(ALFA, NBRB, BELBANK)
            .collect(Collectors.toSet());
}