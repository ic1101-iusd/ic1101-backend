package com.icp.core.oracle;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OracleService {

    private final List<Crypto> SUPPORTED_COLLATERALS = List.of(Crypto.BTC, Crypto.ICP);
    private final PriceClient priceClient;

    public Map<Crypto, Double> aggregatePrices() {
        Map<Crypto, BigDecimal> coingecko = priceClient.getCoingeckoPrice(SUPPORTED_COLLATERALS);
        Map<Crypto, BigDecimal> coinmarketcap = priceClient.getCoinmarketcapPrice(SUPPORTED_COLLATERALS);
        return Stream.of(coingecko.entrySet(), coinmarketcap.entrySet()).flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingDouble(e -> e.getValue().doubleValue())));
    }

}
