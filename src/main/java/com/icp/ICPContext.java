package com.icp;

import static java.lang.System.getenv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Security;
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

    @SneakyThrows
    static synchronized Agent agent() {
        if (agent == null) {
            System.out.println("IDENTITY_KEY:" +  getenv("IDENTITY_KEY"));
            System.out.println("ICP_NETWORK:" +  getenv("ICP_NETWORK"));
            Security.addProvider(new BouncyCastleProvider());
            Identity identity = BasicIdentity.fromPEMFile(new InputStreamReader(ICPContext.class.getResourceAsStream("/identity.pem")));
            System.out.println("Identity:"+identity.sender());
            ReplicaTransport transport = ReplicaApacheHttpTransport.create(getenv("ICP_NETWORK"));
            agent = new AgentBuilder().transport(transport).identity(identity).build();
            System.out.println("Status : "+ agent.status().get());
            System.out.println("Init Agent");
        }
        return agent;
    }

}
