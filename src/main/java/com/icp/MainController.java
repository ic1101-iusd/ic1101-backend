package com.icp;

import static org.ic4j.types.Principal.fromString;

import java.math.BigInteger;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import javax.annotation.PostConstruct;
import org.ic4j.agent.ProxyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("main/")
public class MainController {

    private ICPBtcTokenProxy icpBtcTokenProxy;

    @PostConstruct
    public void init() {
        String btcTokenPrincipal = System.getenv("BTC_TOKEN_PRINCIPAL");
        icpBtcTokenProxy = ProxyBuilder.create(ICPContext.agent(),
                fromString(btcTokenPrincipal))
                .getProxy(ICPBtcTokenProxy.class);
    }

    @GetMapping("/ok")
    public ResponseEntity ok() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer/{principal}")
    public ResponseEntity<String> transfer(@PathVariable String principal) {
        CompletableFuture<TreeMap> transfer = icpBtcTokenProxy.transfer(fromString(principal), BigInteger.valueOf(100000000L));
        return ResponseEntity.ok("1 BTC to " + principal);
    }
}
