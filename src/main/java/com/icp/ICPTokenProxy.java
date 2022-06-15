package com.icp;

import java.math.BigInteger;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import org.ic4j.agent.annotations.Argument;
import org.ic4j.agent.annotations.UPDATE;
import org.ic4j.agent.annotations.Waiter;
import org.ic4j.candid.annotations.Name;
import org.ic4j.candid.types.Type;
import org.ic4j.types.Principal;

public interface ICPTokenProxy {
    @UPDATE
    @Name("approve")
    @Waiter(timeout = 30)
    CompletableFuture<TreeMap> approve(@Argument(Type.PRINCIPAL) Principal spender, @Argument(Type.NAT) BigInteger value);
}
