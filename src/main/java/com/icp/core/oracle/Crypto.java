package com.icp.core.oracle;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Crypto {
    ICP("internet-computer"),
    BTC("bitcoin");
    String alias;
    public static Crypto from(String name) {
        return Arrays.stream(values())
                .filter(c -> c.alias.equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
