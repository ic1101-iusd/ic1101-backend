package com.icp;

import static java.lang.System.getenv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
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
            Security.addProvider(new BouncyCastleProvider());
            Identity identity = BasicIdentity.fromPEMFile(new BufferedReader(new InputStreamReader(ICPContext.class.getResourceAsStream("/identity.pem"))));
            System.out.println(System.getenv("PROTOCOL_PRINCIPAL"));
            ReplicaTransport transport = ReplicaApacheHttpTransport.create(getenv("ICP_NETWORK"));
            agent = new AgentBuilder().transport(transport).identity(identity).build();
            System.out.println("Init Agent");
        }
        return agent;
    }

}
