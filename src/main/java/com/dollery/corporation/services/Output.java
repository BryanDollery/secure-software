package com.dollery.corporation.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dollery.corporation.services.Colors.RESET;
import static com.dollery.corporation.services.Colors.SHADE;

/**
 * The technique employed here is very basic and rudimentary. I don't want to deal with keys and values that are complex,
 * because this is only a simple tool for colouring log output. Keys and values are simple strings with no symbols
 * Otherwise this all turns to shit pretty quickly :)
 */
public final class Output {
    public static final String COMMA = SHADE.color(",");
    public static final String OPEN_BRACE = SHADE.color("{");
    public static final String CLOSE_BRACE = SHADE.color("}");
    public static final String SYMBOL_SPLITTER = "(\\W+|\\w+?\\b)";
    private static final String QUOTE = SHADE.color("\"");
    private static final Pattern P = Pattern.compile(SYMBOL_SPLITTER);

    public static String pair(String label, String value, Colors valueColor) {
        String left = quote(label, SHADE);
        String right = quote(value, valueColor);
        return left + SHADE.color(":") + right;
    }

    public static String quote(String s, Colors c) {
        return QUOTE + c.color(s) + QUOTE;
    }

    public static String colorizeJson(String data, Colors value) {
        return colorizeJson(data, RESET, value);
    }

    public static String colorizeJsonDark(String data, Colors val) {
        return colorizeJson(data, SHADE, val);
    }

    public static String colorizeJson(String data, Colors key, Colors val) {
        Matcher m = P.matcher(data);
        String shaded = "";

        Parts part = Parts.key;

        while (m.find()) {
            String s = m.group(0);

            if (isSymbol(s)) {
                shaded += SHADE.color(s);
            } else {
                shaded += part == Parts.key ? key.color(s) : val.color(s);
                part = part == Parts.key ? Parts.val : Parts.key;
            }
        }

        return shaded;
    }

    public static String shadeSymbols(String data) {
        Pattern p = Pattern.compile(SYMBOL_SPLITTER);
        Matcher m = p.matcher(data);
        String shaded = "";

        while (m.find()) {
            String s = m.group(0);
            shaded += isSymbol(s) ? SHADE.color(s) : s;
        }

        return shaded;
    }

    private static boolean isSymbol(String s) {
        return s.matches("\\W+");
    }

    private enum Parts {key, val}
}
