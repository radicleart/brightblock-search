package org.brightblock.mam.conf;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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


	@Bean
	public RestOperations restTemplate1() {
		return createRestTemplate();
	}

	/**
	 * @see https://github.com/FasterXML/jackson-databind
	 * @return
	 */
	@Bean
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		return mapper;
	}

	public static RestTemplate createRestTemplate() {
		RestTemplate template = new RestTemplate();
		template.getMessageConverters().add(new StringHttpMessageConverter());
		return template;
	}
}
