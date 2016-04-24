package com.giffar.ctour.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by wafdamufti on 12/12/15.
 */
public class IntegerUtils {

    public static String formatWithThousand(String value){
        Locale locale = new Locale("en", "UK");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        String pattern = "#,##0.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

        String number = decimalFormat.format(Double.parseDouble(value));
        if (number.contains(".")){
            String[] check = number.split("\\.");
            if (check.length > 0) {
                if (check[1].length() == 1) {
                    number = number + "0";
                }
            }
        }
        return number;
    }
}
