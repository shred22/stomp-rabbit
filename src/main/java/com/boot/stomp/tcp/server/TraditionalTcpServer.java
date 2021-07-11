package com.boot.stomp.tcp.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.boot.Constants;
import com.boot.stomp.tcp.model.DeviceConnectionResponse;
import com.boot.stomp.tcp.service.ConnectionDetailsService;
import com.boot.stomp.tcp.config.properties.TcpServerProperties;
import com.boot.stomp.tcp.model.DeviceConnectionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.boot.Constants.ACTIVE;

@Component
public class TraditionalTcpServer {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TraditionalTcpServer.class);

    private Socket socket;
    private final ServerSocket serverSock;
    private BufferedReader streamIn = null;
    private final ObjectMapper jsonMapper;
    private final ConnectionDetailsService connectionDetailsService;
    BufferedWriter bufferedWriter;

    public TraditionalTcpServer(ObjectMapper jsonMapper, ConnectionDetailsService connectionDetailsService,
                                TcpServerProperties tcpServerProperties) throws IOException {
        this.jsonMapper = jsonMapper;
        this.connectionDetailsService = connectionDetailsService;
        log.info("TCP Server will be started on Port : "+ tcpServerProperties.getPort());
        serverSock = new ServerSocket(tcpServerProperties.getPort());
    }

    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent ctxStartEvt) {
        log.info("Context Started. Starting TCP Server");
        boolean done = false;
        while (!done) {
            try {
                log.info("Server started: Waiting for a client ...");
                socket = serverSock.accept();
                open();
                log.info("Client accepted: " + socket);
                String tcpMessage = streamIn.readLine();
                log.info("Line Read : " + tcpMessage);
                if (tcpMessage != null) {
                    DeviceConnectionRequest deviceConnectionRequest = jsonMapper.readValue(tcpMessage, DeviceConnectionRequest.class);
                    log.info("Received Message from TCP Client : " + deviceConnectionRequest);
                    connectionDetailsService.updateConnectionDetails(deviceConnectionRequest);
                    //sendTCPResponse(deviceConnectionRequest.getDeviceId());
                }
            } catch (Exception ioe) {
                log.error("TCP Server Error Handled : " + ioe.getMessage());
            }
        }
    }

    public void sendTCPResponse(String deviceId) throws IOException {
        DeviceConnectionResponse response = DeviceConnectionResponse.builder()
                .deviceId(deviceId)
                .deviceConnectionStatus(ACTIVE)
                .build();
        if(socket != null) {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(jsonMapper.writeValueAsString(response));
            bufferedWriter.flush();
        }

    }


    public void open() throws IOException {
        log.info("Stream Opened ..!!!");
        streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @PreDestroy
    public void close() throws IOException {
        log.info("Shutting Down TCP Server .... ");
        if (socket != null)
            socket.close();
        if (streamIn != null)
            streamIn.close();
        if(bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

}