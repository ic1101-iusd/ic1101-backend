package com.icp;

import static org.ic4j.types.Principal.fromString;

import java.math.BigInteger;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.ic4j.agent.ProxyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private ICPBtcTokenProxy icpBtcTokenProxy;

    @PostConstruct
    public void init() {
        String btcTokenPrincipal = System.getenv("BTC_TOKEN_PRINCIPAL");
        icpBtcTokenProxy = ProxyBuilder.create(ICPContext.agent(),
                fromString(btcTokenPrincipal))
                .getProxy(ICPBtcTokenProxy.class);
    }

    @CrossOrigin(origins="*")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("The server is running");
    }

    @SneakyThrows
    @CrossOrigin(origins="*")
    @PostMapping("/transfer/{principal}")
    public ResponseEntity<String> transfer(@PathVariable String principal) {
        icpBtcTokenProxy.transfer(fromString(principal), BigInteger.valueOf(100000000L)).get();
        return ResponseEntity.ok("Sent 1 BTC to " + principal);
    }
}
