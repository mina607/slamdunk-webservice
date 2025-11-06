package com.jojoldu.book.springboot.config;

import net.minidev.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class RosBridgeClient extends WebSocketClient {

    private boolean advertised = false;  // advertise 여부 체크

    public RosBridgeClient() throws URISyntaxException {
        super(new URI("ws://192.168.123.250:9090"));
        this.connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("[ROSBRIDGE] Connected");
        // 연결되자마자 토픽 advertise
        advertiseTopic();
    }

    @Override
    public void onMessage(String message) {
        System.out.println("[ROS] Message received: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("[ROSBRIDGE] Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("[ROSBRIDGE] Error: " + ex.getMessage());
    }

    // 토픽 advertise
    private void advertiseTopic() {
        if (!advertised) {
            JSONObject advertiseMsg = new JSONObject();
            advertiseMsg.put("op", "advertise");
            advertiseMsg.put("topic", "/room_command ");
            advertiseMsg.put("type", "std_msgs/String");
            this.send(advertiseMsg.toString());
            advertised = true;
            System.out.println("[ROSBRIDGE] Topic advertised: /room_command");
        }
    }

    // 주문 publish
    public void publishOrder(String roomNumber) {
        // advertise가 되어있지 않으면 먼저 advertise
        if (!advertised) {
            advertiseTopic();
        }
        System.out.println("[SPRING]hihi " + roomNumber);

        // ROS2 std_msgs/String 메시지 포맷에 맞춰 JSON 구성
        JSONObject msg = new JSONObject();
        msg.put("op", "publish");
        msg.put("topic", "/room_command");

        String command = "go_room" + roomNumber;

        // 메시지 데이터 구성
        JSONObject msgData = new JSONObject();
        msgData.put("data", command);

        msg.put("msg", msgData);

        this.send(msg.toString());
        System.out.println("[ROSBRIDGE] Sent order: " + msgData);
    }
}
