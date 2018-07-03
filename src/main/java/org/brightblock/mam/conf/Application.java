package org.brightblock.mam.conf;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("org.brightblock")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ProtobufHttpMessageConverter protobufHttpMessageConverter() {
		return new ProtobufHttpMessageConverter();
	}

	@Bean
	RestTemplate restTemplate(ProtobufHttpMessageConverter hmc) {
		//return new RestTemplate(Arrays.asList(hmc));
		RestTemplate template = new RestTemplate(Arrays.asList(hmc));
		template.getMessageConverters().add(new StringHttpMessageConverter());
		return template;
	}
}
