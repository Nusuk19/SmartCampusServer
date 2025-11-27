package com.smartcampus.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

/**
 * 🆕 Сервіс для роботи з MQTT (керування замками)
 */
@Service
public class MqttService {

    @Value("${mqtt.broker-url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${mqtt.client-id:smartcampus-server}")
    private String clientId;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    private MqttClient mqttClient;

    /**
     * Підключення до MQTT брокера при старті
     */
    @PostConstruct
    public void connect() {
        try {
            mqttClient = new MqttClient(brokerUrl, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            if (!username.isEmpty()) {
                options.setUserName(username);
                options.setPassword(password.toCharArray());
            }

            mqttClient.connect(options);
            System.out.println("✅ Connected to MQTT broker: " + brokerUrl);

        } catch (MqttException e) {
            System.err.println("❌ Failed to connect to MQTT broker: " + e.getMessage());
        }
    }

    /**
     * Опублікувати повідомлення
     */
    public void publish(String topic, String message) {
        try {
            if (mqttClient == null || !mqttClient.isConnected()) {
                connect();
            }

            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1);  // At least once delivery
            mqttMessage.setRetained(false);

            mqttClient.publish(topic, mqttMessage);
            System.out.println("📤 Published to " + topic + ": " + message);

        } catch (MqttException e) {
            System.err.println("❌ Failed to publish message: " + e.getMessage());
        }
    }

    /**
     * Підписатись на топік
     */
    public void subscribe(String topic, IMqttMessageListener listener) {
        try {
            if (mqttClient == null || !mqttClient.isConnected()) {
                connect();
            }

            mqttClient.subscribe(topic, listener);
            System.out.println("📥 Subscribed to topic: " + topic);

        } catch (MqttException e) {
            System.err.println("❌ Failed to subscribe: " + e.getMessage());
        }
    }

    /**
     * Відключення при зупинці сервера
     */
    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                System.out.println("🔌 Disconnected from MQTT broker");
            }
        } catch (MqttException e) {
            System.err.println("❌ Failed to disconnect: " + e.getMessage());
        }
    }
}
