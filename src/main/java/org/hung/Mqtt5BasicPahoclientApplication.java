package org.hung;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Mqtt5BasicPahoclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mqtt5BasicPahoclientApplication.class, args);
	}

	@Value("${mqtt.broker-url}")
	private String brokerUrl;

	@Value("${mqtt.username}")
	private String username;
	
	@Value("${mqtt.password}")
	private String password;

	@Value("${mqtt.topic-filter}")
	private String topicFilter;
	
	@Bean
	public CommandLineRunner runPahoClient(ApplicationContext ctx) {

		return args -> {           
			String clientId = "springboot-mock-"+UUID.randomUUID();
			
			MqttClient client = new MqttClient(brokerUrl, clientId);
			
			client.setCallback(new MqttCallback() {
				
				@Override
				public void mqttErrorOccurred(MqttException exception) {
					log.error("error occurred", exception);
				}
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					log.info("message received from "+topic);
					
					String responseTopic = message.getProperties().getResponseTopic();
					
					MqttMessage replyMsg = new MqttMessage(("I got your message [" + new String(message.getPayload()) + "]").getBytes());
					
					client.publish(responseTopic, replyMsg);
				}
				
				@Override
				public void disconnected(MqttDisconnectResponse disconnectResponse) {
					log.info("disconnected");
				}
				
				@Override
				public void deliveryComplete(IMqttToken token) {
					log.info("delivery completed");
				}
				
				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					log.info("connect completed");
				}
				
				@Override
				public void authPacketArrived(int reasonCode, MqttProperties properties) {
					log.info("authPacket arrived");
				}
			});
			
			MqttConnectionOptions options = new MqttConnectionOptions();
			options.setUserName(username);
			options.setPassword(password.getBytes(StandardCharsets.UTF_8));
			client.connect(options);
			
			client.subscribe(topicFilter,2);
			
			//client.close();
		};
	}
	
}
