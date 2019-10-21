package com.dollery.services;

import static com.dollery.services.Colors.SHADE;

public final class Output {
    public static final String QUOTE = SHADE.color("\"");
    public static final String COMMA = SHADE.color(",");
    public static final String OPEN_BRACE = SHADE.color("{");
    public static final String CLOSE_BRACE = SHADE.color("}");

    public static String pair(String label, String value, Colors valueColor) {
        String left = quote(label, SHADE);
        String right = quote(value, valueColor);
        return left + SHADE.color(":") + right;
    }

    public static String quote(String s, Colors c) {
        return QUOTE + c.color(s) + QUOTE;
    }

}
