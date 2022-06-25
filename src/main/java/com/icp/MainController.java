package com.icp;

import static org.ic4j.types.Principal.fromString;

import com.icp.contract.ICPBtcTokenProxy;
import com.icp.core.ICPContext;
import java.math.BigInteger;
import lombok.SneakyThrows;
import org.ic4j.agent.Agent;
import org.ic4j.agent.ProxyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class MainController {

    private ICPBtcTokenProxy icpBtcTokenProxy;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("The server is running");
    }

    @SneakyThrows
    @PostMapping("/transfer/{principal}")
    public ResponseEntity<String> transfer(@PathVariable String principal) {
        transferWithFallback(principal);
        return ResponseEntity.ok("Sent 1 BTC to " + principal);
    }

    private void transferWithFallback(String principal) {
        try {
            getBtc(principal, ICPContext.agent());
        } catch (Exception e) {
            ICPContext.init();
            getBtc(principal, ICPContext.agent());
        }
    }

    @SneakyThrows
    private void getBtc(String principal, Agent agent) {
        String btcTokenPrincipal = System.getenv("BTC_TOKEN_PRINCIPAL");
        icpBtcTokenProxy = ProxyBuilder.create(agent,
                fromString(btcTokenPrincipal))
                .getProxy(ICPBtcTokenProxy.class);
        icpBtcTokenProxy.transfer(fromString(principal), BigInteger.valueOf(100000000L)).get();
    }
}
