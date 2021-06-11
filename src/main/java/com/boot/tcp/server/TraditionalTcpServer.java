package com.boot.tcp.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PreDestroy;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.boot.tcp.ConnectionDetailsService;
import com.boot.tcp.model.DeviceConnectionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TraditionalTcpServer {
    private Socket socket = null;
    private ServerSocket serverSock = null;
    private DataInputStream streamIn = null;
    private final ObjectMapper jsonMapper;
    private final ConnectionDetailsService connectionDetailsService;

    public TraditionalTcpServer(ObjectMapper jsonMapper, ConnectionDetailsService connectionDetailsService) {
        this.jsonMapper = jsonMapper;
        this.connectionDetailsService = connectionDetailsService;
    }

    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent ctxStartEvt) {
        log.info("Context Started. Starting TCP Server");
        try {
            serverSock = new ServerSocket(10012);
            log.info("Server started: Waiting for a client ...");
            socket = serverSock.accept();
            log.info("Client accepted: " + socket);
            open();
            boolean done = false;
            while (!done) {
                log.info("Received Message from TCP Client");
                try {
                    DeviceConnectionRequest deviceConnectionRequest = jsonMapper.readValue(streamIn.readUTF(), DeviceConnectionRequest.class);
                    connectionDetailsService.updateConnectionDetails(deviceConnectionRequest);
                } catch (Exception ioe) {
                    log.error("TCP Server Error Handled : " + ioe.getMessage());
                    done = true;
                }
            }
            close();
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        }
    }


    public void open() throws IOException {
        log.info("Stream Opened ..!!!");
        streamIn = new DataInputStream(new BufferedInputStream(
                socket.getInputStream()));
    }

    @PreDestroy
    public void close() throws IOException {
        log.info("Shutting Down TCP Server .... ");
        if (socket != null)
            socket.close();
        if (streamIn != null)
            streamIn.close();
    }

}