package com.icp;

import static com.icp.NumberUtils.natToBigDecimal;
import static com.icp.NumberUtils.toNat;
import static org.ic4j.types.Principal.fromString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.SneakyThrows;
import org.ic4j.agent.ProxyBuilder;
import org.ic4j.types.Principal;

public class LiquidatorService {

    private final ICPProtocolProxy icpProtocolProxy;
    private static final int HEALTH_RATIO = 1;
    private static final BigDecimal MIN_RISK = BigDecimal.valueOf(1.15);


    LiquidatorService() {
        icpProtocolProxy = ProxyBuilder.create(ICPContext.agent(),
                fromString(System.getenv("PROTOCOL_PRINCIPAL")))
                .getProxy(ICPProtocolProxy.class);

        approveTokens();
    }

    @SneakyThrows
    List<SharedPosition> getPositions(BigInteger limit, BigInteger offset) {
        return Arrays.asList(icpProtocolProxy.getPositions(limit, offset));
    }

    @SneakyThrows
    BigDecimal getPrice() {
        return natToBigDecimal(icpProtocolProxy.getCollateralPrice());
    }

    void liquidate(BigInteger id) throws ExecutionException, InterruptedException {
        icpProtocolProxy.liquidatePosition(id).get();
    }

    BigInteger getLastPositionId() {
        return icpProtocolProxy.getLastPositionId();
    }

    boolean isLiquidated(SharedPosition positionDTO, BigDecimal price) {
        return liquidationRation(positionDTO.getCollateralAmount(), price, positionDTO.getStableAmount()).doubleValue() < HEALTH_RATIO;
    }

    private BigDecimal liquidationRation(BigInteger colAmount, BigDecimal colPrice, BigInteger debt) {
        return (NumberUtils.natToBigDecimal(colAmount).multiply(colPrice)).divide(natToBigDecimal(debt).multiply(MIN_RISK), 2, RoundingMode.HALF_UP);
    }

    //ICP_NETWORK=http://127.0.0.1:8001;
    // PROTOCOL_PRINCIPAL=ryjl3-tyaaa-aaaaa-aaaba-cai;BTC_TOKEN_PRINCIPAL=r7inp-6aaaa-aaaaa-aaabq-cai;
    // PROTOCOL_OWNER_PRINCIPAL=o3m4k-ph6rg-ilvir-3tas4-zz5qs-i75vs-cw6ab-ghzr4-5sxz7-wc2d4-bae;BTC_AMOUNT_APPROVE=200;TOKEN_AMOUNT_POSITION=100000;INIT_APPROVE=false
    @SneakyThrows
    private void approveTokens() {
        boolean initApprove = Boolean.parseBoolean(System.getenv("INIT_APPROVE"));
        if (initApprove) {
            System.out.println("startInit");

            String mintTokenPrincipal = System.getenv("MINT_TOKEN_PRINCIPAL");
            String btcTokenPrincipal = System.getenv("BTC_TOKEN_PRINCIPAL");
            Principal ownerContractPrincipal = Principal.fromString(System.getenv("PROTOCOL_PRINCIPAL"));

            System.out.println(String.format("MINT_TOKEN_PRINCIPAL %s MINT_TOKEN_PRINCIPAL %s PROTOCOL_PRINCIPAL %s", mintTokenPrincipal, btcTokenPrincipal, ownerContractPrincipal.toString()));

            ICPTokenProxy icpTokenProxy = ProxyBuilder.create(ICPContext.agent(),
                    fromString(mintTokenPrincipal))
                    .getProxy(ICPTokenProxy.class);
            ICPBtcTokenProxy icpBtcTokenProxy = ProxyBuilder.create(ICPContext.agent(),
                    fromString(btcTokenPrincipal))
                    .getProxy(ICPBtcTokenProxy.class);

            BigInteger amountBTC = toNat(BigInteger.valueOf(Long.parseLong(System.getenv("AMOUNT_BTC"))));
            BigInteger amountTokens = toNat(BigInteger.valueOf(Long.parseLong(System.getenv("AMOUNT_TOKEN"))));

            icpBtcTokenProxy.approve(ownerContractPrincipal, amountBTC);
            System.out.println("BTC Token approved");
            icpProtocolProxy.createPosition(amountBTC, amountTokens).get();
            System.out.println(String.format("Created position amountBTC %s amountTokens %s", amountBTC, amountTokens));
            icpTokenProxy.approve(ownerContractPrincipal, amountTokens);
            System.out.println("ICP Token approved");
        }
    }


}
