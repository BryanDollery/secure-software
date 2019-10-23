package com.dollery.corporation.services;

import org.junit.jupiter.api.Test;

import static com.dollery.corporation.services.Colors.BLUE;
import static com.dollery.corporation.services.Colors.RED;

class OutputTest {
    private String data = "{\"field\":\"value\"};";
    private String data2 = "{\"field\":\"value\",\"field2\":\"value2\"};";

    @Test
    void shadeSymbols() {
        String shaded = Output.shadeSymbols(data);
        System.out.println("symbols only = " + shaded);
    }

    @Test
    void shadeSymbolsWithValue() {
        String shaded = Output.colorizeJson(data, RED);
        System.out.println("val = " + shaded);
    }

    @Test
    void shadeSymbolsWithLabelAndValue() {
        String shaded = Output.colorizeJson(data, BLUE, RED);
        System.out.println("both = " + shaded);
    }

    @Test
    void somethingHarder() {
        String shaded = Output.colorizeJson(data2, BLUE, RED);
        System.out.println("data2 = " + shaded);
    }
}