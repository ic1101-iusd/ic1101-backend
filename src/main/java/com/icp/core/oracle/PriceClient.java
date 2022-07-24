package com.icp.core.oracle;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceClient {

    private final RestTemplate restTemplate;

    public Map<Crypto, BigDecimal> getCoingeckoPrice(List<Crypto> crypto) {
        ResponseEntity<Map> response = restTemplate.getForEntity(String.format("https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd", toQuery(crypto)), Map.class);

        Map<String, Map> body = response.getBody();

        return Objects.requireNonNull(body)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> Crypto.from((e.getKey())), e -> new BigDecimal(String.valueOf(e.getValue().get("usd")))));
    }

    public Map<Crypto, BigDecimal> getCoinmarketcapPrice(List<Crypto> crypto) {
        ResponseEntity<Map> response = restTemplate.getForEntity(String.format("https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?slug=%s&CMC_PRO_API_KEY=a1a31b1d-5c7c-4d4b-b96a-c4e1e69088a0", toQuery(crypto)), Map.class);

        Collection<Object> values = Optional.ofNullable(response.getBody())
                .map(body -> body.get("data"))
                .map(Map.class::cast)
                .map(Map::values)
                .orElse(Collections.emptyList());

        Map<Crypto, BigDecimal> res = new HashMap<>();

        values.forEach(value -> {
            Map casted = asMap(value);
            res.put(Crypto.from(String.valueOf(casted.get("slug"))),
                    new BigDecimal(String.valueOf(asMap(asMap(casted.get("quote")).get("USD")).get("price")))
            );
        });

        return res;
    }

    private static String toQuery(List<Crypto> crypto) {
        return crypto.stream().
                map(Crypto::getAlias).
                collect(Collectors.joining(","));
    }

    private static Map asMap(Object o) {
        return (Map) o;
    }
}
