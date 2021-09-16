package org.hung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Mqtt5BasicPahoclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mqtt5BasicPahoclientApplication.class, args);
	}


	
}
