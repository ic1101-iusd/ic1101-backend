package com.icp.contract;

import java.math.BigInteger;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import org.ic4j.agent.annotations.Argument;
import org.ic4j.agent.annotations.UPDATE;
import org.ic4j.agent.annotations.Waiter;
import org.ic4j.candid.annotations.Name;
import org.ic4j.candid.types.Type;
import org.ic4j.types.Principal;

public interface ICPBtcTokenProxy {
    @UPDATE
    @Name("approve")
    @Waiter(timeout = 30)
    CompletableFuture<TreeMap> approve(@Argument(Type.PRINCIPAL) Principal spender, @Argument(Type.NAT) BigInteger value);

    @UPDATE
    @Name("transfer")
    @Waiter(timeout = 30)
    CompletableFuture<TreeMap> transfer(@Argument(Type.PRINCIPAL) Principal spender, @Argument(Type.NAT) BigInteger value);

}
