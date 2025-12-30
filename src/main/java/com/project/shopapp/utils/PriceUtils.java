package com.project.shopapp.utils;

import java.math.BigDecimal;

public final class PriceUtils {

    private PriceUtils() {
    };

    public static BigDecimal roundPrice(long raw) {
        if (raw < 100000) {
            return BigDecimal.valueOf((raw / 1000) * 1000);
        } else if (raw < 1000000) {
            return BigDecimal.valueOf((raw / 10000) * 10000);
        } else if (raw < 10000000) {
            return BigDecimal.valueOf((raw / 100000) * 100000);
        } else {
            return BigDecimal.valueOf((raw / 1000000) * 1000000);
        }
    }
}
