package com.icp.core.oracle;


import static com.icp.util.NumberUtils.toNat;
import static java.math.BigDecimal.valueOf;

import com.icp.contract.ICPProtocolProxy;
import com.icp.contract.ICPProxyFactory;
import com.icp.core.CollateralPriceHistory;
import com.icp.core.RandomCollection;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class OracleTask {

    private final RandomCollection<BigDecimal> randomCollection = new RandomCollection<>();

    public static final ConcurrentLinkedDeque<CollateralPriceHistory> COLLATERAL_PRICE_HISTORIES = new ConcurrentLinkedDeque<>();
    public static AtomicReference<CollateralPriceHistory> CURRENT_PRICE = new AtomicReference<>();
    private static final Long MAX_SIZE = 200L;
    private ICPProtocolProxy icpProtocolProxy;
    private final OracleService oracleService;

    @PostConstruct
    public void init() {
        icpProtocolProxy = ICPProxyFactory.buildICPProtocolProxy();
        updateCollateralPrice();
    }

    @Scheduled(cron = "0 0/30 * * * *")
    void updateCollateralPriceJob() {
        oracleService.aggregatePrices().forEach((crypto, price) -> {
            if (crypto == Crypto.BTC) {
                icpProtocolProxy.setCollateralPrice(toNat(price));
            }
        });
    }

    //Used only for demo demonstration
    @Deprecated
    private void updateCollateralPrice() {

        if (COLLATERAL_PRICE_HISTORIES.size() > MAX_SIZE) {
            COLLATERAL_PRICE_HISTORIES.removeFirst();
        }

        CollateralPriceHistory current;

        if (COLLATERAL_PRICE_HISTORIES.isEmpty()) {
            current = new CollateralPriceHistory();
            current.setCreatedDate(new Date());
            current.setPrice(randomCollection.next());
            COLLATERAL_PRICE_HISTORIES.add(current);
            CURRENT_PRICE.set(current);
        } else {
            current = COLLATERAL_PRICE_HISTORIES.getLast();
        }

        icpProtocolProxy.setCollateralPrice(toNat(current.getPrice()));

        System.out.println("Price changed to " + current.getPrice());
        CollateralPriceHistory next = new CollateralPriceHistory();

        LocalDateTime nextDateTime = LocalDateTime.now().plus(Duration.of(20, ChronoUnit.MINUTES));
        next.setCreatedDate(Date.from(nextDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        next.setPrice(randomCollection.next());
        COLLATERAL_PRICE_HISTORIES.add(next);
        System.out.println("Next price will be " + next.getPrice());
    }

}
