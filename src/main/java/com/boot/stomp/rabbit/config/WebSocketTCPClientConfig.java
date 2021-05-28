package com.boot.stomp.rabbit.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;

import com.boot.stomp.rabbit.config.properties.BrokerProperties;
import com.boot.stomp.rabbit.config.properties.ClientTLSProperties;
import com.boot.stomp.rabbit.config.properties.ServerTLSProperties;
import com.boot.stomp.rabbit.config.properties.TLSProperties;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Configuration
public class WebSocketTCPClientConfig {

    private final TLSProperties tlsProperties;
    private final ClientTLSProperties clientTLSProperties;
    private final ServerTLSProperties serverTLSProperties;
    private final BrokerProperties brokerProperties;

    @Bean
    public ReactorNettyTcpClient<byte[]> tcpClient(SslContext sslContext) {
        return new ReactorNettyTcpClient<>(tcpClient -> tcpClient
                .host(brokerProperties.getHost())
                .port(brokerProperties.getPort())
                .secure(spec -> spec.sslContext(sslContext))
                .metrics(true), new StompReactorNettyCodec());
    }

    @Bean
    public SslContext sslContext(KeyManagerFactory kmf, TrustManagerFactory tmf) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(tlsProperties.getVersion());
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SslContext defaultSslContext = null;
        try {
            defaultSslContext = SslContextBuilder.forClient().keyManager(kmf).trustManager(tmf).startTls(true).build();
        } catch (SSLException e) {
            log.error("error occurred : {}", e.getMessage());
        }
        return defaultSslContext;
    }

    @Bean
    public TrustManagerFactory trustManagerFactory() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        char[] trustPassphrase = serverTLSProperties.getTrustPassword().toCharArray();
        KeyStore tks = KeyStore.getInstance(serverTLSProperties.getTrustStoreType());
        tks.load(new FileInputStream(serverTLSProperties.getCertFile()), trustPassphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(serverTLSProperties.getTrustManagerType());
        tmf.init(tks);
        return tmf;
    }

    @Bean
    public KeyManagerFactory keyManagerFactory() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        char[] keyPassphrase = clientTLSProperties.getKeyPassword().toCharArray();
        KeyStore ks = KeyStore.getInstance(clientTLSProperties.getKeyStoreType());
        ks.load(new FileInputStream(clientTLSProperties.getKeyLocation()), keyPassphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(serverTLSProperties.getTrustManagerType());
        kmf.init(ks, keyPassphrase);
        return kmf;
    }
}
