package com.megafair.util;

import java.math.BigDecimal;
import java.util.Objects;

public class CommonUtil {

    private CommonUtil() {
        throw new IllegalStateException("Utility class cannot be initiated");
    }

    public static boolean bigDecimalEquals(BigDecimal bd_1, BigDecimal bd_2) {
        if (Objects.nonNull(bd_1) && Objects.nonNull(bd_2)) {
            return bd_1.compareTo(bd_2) == 0;
        } else {
            return (Objects.isNull(bd_1) && Objects.isNull(bd_2));
        }
    }

    public static Long parseLong(String n) {
        return (Objects.nonNull(n) && n.matches("\\d+")) ? Long.valueOf(n) : null;
    }
}
