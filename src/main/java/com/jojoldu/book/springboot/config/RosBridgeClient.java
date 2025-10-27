package com.jojoldu.book.springboot.config;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class RosBridgeClient extends WebSocketClient {

    public RosBridgeClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to rosbridge");
    }

    @Override
    public void onMessage(String message) { }

    @Override
    public void onClose(int code, String reason, boolean remote) { }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void sendRobotCommand(String orderNumber) {
        String json = String.format(
                "{ \"op\": \"publish\", \"topic\": \"/scout/assign_order\", \"msg\": { \"order_number\": \"%s\" } }",
                orderNumber
        );
        send(json);
    }
}
