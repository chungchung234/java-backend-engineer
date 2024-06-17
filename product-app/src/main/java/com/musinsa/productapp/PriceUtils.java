package com.musinsa.productapp;

import java.text.DecimalFormat;

public class PriceUtils {

    /**
     * int의 1000단위로 콤마를 찍어서 문자열로 반환
     * @param price int
     * @return price String
     */
    public static String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return decimalFormat.format(price);
    }
}
