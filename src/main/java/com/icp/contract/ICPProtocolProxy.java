package com.icp.contract;


import com.icp.core.SharedPosition;
import java.math.BigInteger;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import org.ic4j.agent.annotations.Argument;
import org.ic4j.agent.annotations.QUERY;
import org.ic4j.agent.annotations.UPDATE;
import org.ic4j.agent.annotations.Waiter;
import org.ic4j.candid.annotations.Name;
import org.ic4j.candid.types.Type;

public interface ICPProtocolProxy {

    @QUERY
    @Name("getLastPositionId")
    @Waiter(timeout = 30)
    BigInteger getLastPositionId();

    @QUERY
    @Name("getPositions")
    @Waiter(timeout = 30)
    SharedPosition[] getPositions(@Argument(Type.NAT) BigInteger limit, @Argument(Type.NAT) BigInteger offset);

    @QUERY
    @Name("getCollateralPrice")
    @Waiter(timeout = 30)
    BigInteger getCollateralPrice();

    @UPDATE
    @Name("liquidatePosition")
    @Waiter(timeout = 30)
    CompletableFuture<TreeMap> liquidatePosition(@Argument(Type.NAT) BigInteger id);

    @UPDATE
    @Name("createPosition")
    @Waiter(timeout = 30)
    CompletableFuture<TreeMap> createPosition(@Argument(Type.NAT) BigInteger collateralAmount, @Argument(Type.NAT) BigInteger stableAmount);


    @UPDATE
    @Name("setCollateralPrice")
    @Waiter(timeout = 30)
    CompletableFuture<TreeMap> setCollateralPrice(@Argument(Type.NAT) BigInteger newPrice);

}

