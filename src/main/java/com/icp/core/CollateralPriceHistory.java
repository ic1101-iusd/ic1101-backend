package com.icp.core;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class CollateralPriceHistory {
    private BigDecimal price;
    private Date createdDate;
}
