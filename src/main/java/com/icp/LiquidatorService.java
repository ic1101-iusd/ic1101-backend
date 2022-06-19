package com.icp;

import static com.icp.NumberUtils.fromNat;
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
        return NumberUtils.fromNat(icpProtocolProxy.getCollateralPrice());
    }

    void liquidate(BigInteger id) throws ExecutionException, InterruptedException {
        icpProtocolProxy.liquidatePosition(id).get();
    }

    BigInteger getLastPositionId() {
        return icpProtocolProxy.getLastPositionId();
    }

    boolean isLiquidated(SharedPosition positionDTO, BigDecimal price) {
        System.out.println(positionDTO.getId() + ":" + liquidationRation(positionDTO.getCollateralAmount(), price, positionDTO.getStableAmount()).doubleValue());
        return liquidationRation(positionDTO.getCollateralAmount(), price, positionDTO.getStableAmount()).doubleValue() < HEALTH_RATIO;
    }

    BigDecimal liquidationRation(BigInteger colAmount, BigDecimal colPrice, BigInteger debt) {
        return (NumberUtils.fromNat(colAmount).multiply(colPrice)).divide(NumberUtils.fromNat(debt).multiply(MIN_RISK), 2, RoundingMode.HALF_UP);
    }

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

            BigInteger amountBTC = BigInteger.valueOf(Long.parseLong(System.getenv("AMOUNT_BTC")));
            BigInteger amountTokens = BigInteger.valueOf(Long.parseLong(System.getenv("AMOUNT_TOKEN")));

            icpBtcTokenProxy.approve(ownerContractPrincipal, amountBTC);
            System.out.println("BTC Token approved");
            icpProtocolProxy.createPosition(amountBTC, amountTokens).get();
            System.out.println(String.format("Created position amountBTC %s amountTokens %s", amountBTC, amountTokens));
            icpTokenProxy.approve(ownerContractPrincipal, amountTokens);
            System.out.println("ICP Token approved");
        }
    }


}
