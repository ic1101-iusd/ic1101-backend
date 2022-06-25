package com.icp;

import com.icp.core.CollateralPriceHistory;
import com.icp.core.CollateralPriceTask;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/price")
@CrossOrigin("*")
public class CollateralPriceController {

    @GetMapping("/next")
    public CollateralPriceHistory nextPrice() {
        return CollateralPriceTask.COLLATERAL_PRICE_HISTORIES.getLast();
    }

    @GetMapping("/current")
    public CollateralPriceHistory current() {
        return CollateralPriceTask.CURRENT_PRICE.get();
    }

    @GetMapping("/history")
    public List<CollateralPriceHistory> history() {
        return new ArrayList<>(CollateralPriceTask.COLLATERAL_PRICE_HISTORIES);
    }
}
