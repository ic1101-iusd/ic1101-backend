package com.icp.core;


import static com.icp.util.NumberUtils.toNat;
import static java.math.BigDecimal.valueOf;
import static org.ic4j.types.Principal.fromString;

import com.icp.contract.ICPProtocolProxy;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;
import org.ic4j.agent.ProxyBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CollateralPriceTask {

    private final RandomCollection<BigDecimal> randomCollection = new RandomCollection<>();


    public static final ConcurrentLinkedDeque<CollateralPriceHistory> COLLATERAL_PRICE_HISTORIES = new ConcurrentLinkedDeque<>();
    public static AtomicReference<CollateralPriceHistory> CURRENT_PRICE = new AtomicReference<>();
    private static final Long MAX_SIZE = 200L;
    private ICPProtocolProxy icpProtocolProxy;

    @PostConstruct
    void init() {
        icpProtocolProxy = ProxyBuilder.create(ICPContext.agent(),
                fromString(System.getenv("PROTOCOL_PRINCIPAL")))
                .getProxy(ICPProtocolProxy.class);

        randomCollection
                .add(15, valueOf(20000))
                .add(13, valueOf(19000))
                .add(12, valueOf(18000))
                .add(11, valueOf(17000))
                .add(10, valueOf(16000))
                .add(9, valueOf(15000))
                .add(8, valueOf(14000))
                .add(7, valueOf(13000))
                .add(6, valueOf(12000))
                .add(5, valueOf(11000))
                 .add(4, valueOf(10000));
    }

    @Scheduled(cron = "*/50 * * * *")
    void updateCollateralPrice() {

        if (COLLATERAL_PRICE_HISTORIES.size() > MAX_SIZE) {
            COLLATERAL_PRICE_HISTORIES.removeFirst();
        }

        CollateralPriceHistory current;

        if (COLLATERAL_PRICE_HISTORIES.isEmpty()) {
            current = new CollateralPriceHistory();
            current.setCreatedDate(new Date());
            current.setPrice(randomCollection.next());
        } else {
            current = COLLATERAL_PRICE_HISTORIES.getLast();
        }
        icpProtocolProxy.setCollateralPrice(toNat(current.getPrice()));
        COLLATERAL_PRICE_HISTORIES.add(current);
        CURRENT_PRICE.set(current);
        System.out.println("Price changed to " + current.getPrice());
        CollateralPriceHistory next = new CollateralPriceHistory();
        next.setCreatedDate(new Date());
        next.setPrice(randomCollection.next());
        COLLATERAL_PRICE_HISTORIES.add(next);
        System.out.println("Next price will be " + next.getPrice());
    }

}
