package com.icp.core;

import static java.lang.System.getenv;

import java.io.StringReader;
import java.security.Security;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ic4j.agent.Agent;
import org.ic4j.agent.AgentBuilder;
import org.ic4j.agent.ReplicaTransport;
import org.ic4j.agent.http.ReplicaApacheHttpTransport;
import org.ic4j.agent.identity.BasicIdentity;
import org.ic4j.agent.identity.Identity;

public class ICPContext {

    private static Agent agent;
    private static final Lock lock = new ReentrantLock();

    public static Agent agent() {
        if (agent == null) {
            lock.lock();
            if (agent == null) {
                agent = buildAgent();
            }
            lock.unlock();
        }
        return agent;
    }


    public static void init() {
        lock.lock();
        System.out.println("Init Agent");
        agent = buildAgent();
        lock.unlock();
    }

    @SneakyThrows
    private static Agent buildAgent() {
        System.out.println("ICP_NETWORK:" + getenv("ICP_NETWORK"));
        Security.addProvider(new BouncyCastleProvider());
        Identity identity = BasicIdentity.fromPEMFile(new StringReader(System.getenv("IDENTITY_KEY")));
        ReplicaTransport transport = ReplicaApacheHttpTransport.create(getenv("ICP_NETWORK"));
        agent = new AgentBuilder().transport(transport).identity(identity).build();
        System.out.println("Init Agent");
        return agent;
    }


}
