package com.windy.cafemanagement.Utils;

public class UtilConvert {
    public static Double convertStringMoneyToDouble(String value) {
        String cleanedvalue = value.replace(".", "");
        return Double.parseDouble(cleanedvalue);
    }
}
