package com.icp;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

    private static final BigInteger DIMENSION = BigInteger.valueOf(100000000);

    public static BigDecimal natToBigDecimal(BigInteger value) {
        return BigDecimal.valueOf(value.doubleValue() / DIMENSION.intValue());
    }

    public static BigInteger toNat(BigInteger value) {
        return value.multiply(DIMENSION);
    }

}
