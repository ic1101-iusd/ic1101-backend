package com.icp.contract;

import static org.ic4j.types.Principal.fromString;

import com.icp.core.ICPContext;
import org.ic4j.agent.ProxyBuilder;

public class ICPProxyFactory {

    public static ICPProtocolProxy buildICPProtocolProxy() {
        return ProxyBuilder.create(ICPContext.agent(),
                fromString(System.getenv("PROTOCOL_PRINCIPAL")))
                .getProxy(ICPProtocolProxy.class);
    }

    public static ICPBtcTokenProxy buildICPBtcTokenProxy() {
        return ProxyBuilder.create(ICPContext.agent(),
                fromString(System.getenv(System.getenv("BTC_TOKEN_PRINCIPAL"))))
                .getProxy(ICPBtcTokenProxy.class);
    }

    public static ICPTokenProxy buildICPTokenProxy() {
        return ProxyBuilder.create(ICPContext.agent(),
                fromString(System.getenv(System.getenv("MINT_TOKEN_PRINCIPAL"))))
                .getProxy(ICPTokenProxy.class);
    }
}
