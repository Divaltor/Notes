package com.divaltor.notes.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextUtil {
    private final static int PREVIEW_SYMBOLS = 300, PREVIEW_ROWS = 6;
    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy HH:mm");

    /**
     * @param str String to truncate
     * @return Truncated text for preview
     */
    public static String truncate(String str) {
        if (str.length() > PREVIEW_SYMBOLS)
            return str.substring(0, PREVIEW_SYMBOLS) + "...";
        int ns = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                ns++;
                if (ns == PREVIEW_ROWS) return str.substring(0, i - 1) + "...";
            }
        }
        return str;
    }

    /**
     * @return Current formatted date
     */
    public static String getCurrentDate() {
        return DATE_FORMAT.format(new Date(System.currentTimeMillis()));
    }
}
