package com.paymentconcentrator.paypal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class PaypalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypalApplication.class, args);
	}

}
