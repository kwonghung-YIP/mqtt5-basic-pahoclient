package org.hung;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import javax.annotation.PreDestroy;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MeasureLatencySchedTask {

	@Configuration
	public class MeasureLatencyConfig {
		
		@Value("${measure-latency.client-id}")
		private String clientId;
		
		@Value("${mqtt.broker-url}")
		private String brokerUrl;

		@Value("${mqtt.username:}")
		private String username;
		
		@Value("${mqtt.password:}")
		private String password;

		@Bean(name="latencyMqttClient")
		public MqttAsyncClient mqttAsyncClient() throws MqttException {

			MqttAsyncClient client = new MqttAsyncClient(brokerUrl, clientId);
			
			MqttConnectionOptions options = new MqttConnectionOptions();
			if (StringUtils.hasText(username) || StringUtils.hasText(password)) {
				options.setUserName(username);
				options.setPassword(password.getBytes(StandardCharsets.UTF_8));
			}
			
			client.setCallback(new MqttCallback() {
				
				@Override
				public void mqttErrorOccurred(MqttException exception) {
					log.error("MQTT Client error occurred",exception);
				}
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void disconnected(MqttDisconnectResponse disconnectResponse) {
					log.info("MQTT Client disconnected");	
				}
				
				@Override
				public void deliveryComplete(IMqttToken token) {
					log.info("MQTT Client delivery completed");
				}
				
				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					log.info("MQTT Client connect completed");
				}
				
				@Override
				public void authPacketArrived(int reasonCode, MqttProperties properties) {
					// TODO Auto-generated method stub
					
				}
			});
			
			IMqttToken token = client.connect(options);
			token.waitForCompletion();
			
			return client;
		}
	}
	
	@Data
	@RequiredArgsConstructor
	public class SentTimePayload {
		private final long count;
		private LocalDateTime sentTime = LocalDateTime.now();
	}
	
	@Component
	public class Scheduler {

		@Autowired
		@Qualifier("latencyMqttClient")
		private MqttAsyncClient client;
		
		@Autowired
		private ObjectMapper objectMapper;
		
		@Value("${measure-latency.topic}")
		private String topic;
		
		private long counter = 1;
		
		@Scheduled(fixedRateString="${measure-latency.fixed-rate}")
		public void pushMessage() {
			log.info("push the {}th message...",counter);
			
			try {
				MqttProperties props = new MqttProperties();
				props.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				
				MqttMessage msg = new MqttMessage();
				msg.setRetained(true);
				msg.setQos(0);
				msg.setProperties(props);
				
				SentTimePayload payload = new SentTimePayload(counter);
				
				msg.setPayload(objectMapper.writeValueAsBytes(payload));
				
				client.publish(topic, msg);
				
				counter++;
			} catch (MqttException | JsonProcessingException e) {
				log.error("fail to publish message",e);
			}
		}
		
		@PreDestroy
		public void killScheduler() {
			try {
				log.info("kill MQTT client before shutdown the springboot application...");
				client.disconnect();
				client.close();
			} catch (MqttException e) {
				log.error("error raised when closing the MQTT client",e);
			}
		}
	}

}
