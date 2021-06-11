package com.boot.stomp.rabbit.service;

import java.io.File;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.boot.stomp.rabbit.config.properties.JwtConfigProperties;
import com.boot.stomp.rabbit.jwt.util.PemUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenService {

    private final JwtConfigProperties jwtConfig;
    private final JWTVerifier verifier;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final Algorithm algorithm;

    public JwtTokenService(JwtConfigProperties jwtConfig) throws IOException {
        this.jwtConfig = jwtConfig;
        this.privateKey = PemUtils.readPrivateKeyFromFile(new File("src/main/resources/jwt/private_key.pem"), "RSA");
        this.publicKey = PemUtils.readPublicKeyFromFile(new File("src/main/resources/jwt/jwt_public.pem"), "RSA");
        this.algorithm = Algorithm.RSA256(publicKey, privateKey);
        this.verifier = JWT.require(algorithm)
                .withIssuer(jwtConfig.getIssuer())
                .build(); //Reusable verifier instance

    }

    public void verifyToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            if(CollectionUtils.isEmpty(decodedJWT.getClaim("permissions").asList(String.class))) {
               throw new MessageDeliveryException("Insufficient Privileges");
            }
        } catch (JWTVerificationException e) {
            log.error("Token Verification FAILED");
              throw new MessageDeliveryException(e.getMessage());
        }
    }

    public String createJwt() {
        JWTCreator.Builder jwtBuilder = JWT.create()
                .withIssuer("stomp-rabbit-spring-server")
                .withSubject("stomp-client-ng")
                .withAudience("stomp-client-ng")
                .withExpiresAt(new Date(Instant.now().plusSeconds(500000L).toEpochMilli()))
                .withIssuedAt(new Date())
                .withArrayClaim("permissions", new String[]{"ADD"})
                .withKeyId("12345")
                .withJWTId(UUID.randomUUID().toString());
        return jwtBuilder.sign(algorithm);
    }
}
