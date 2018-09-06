package org.brightblock.mam.bitcoin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestOperations;

@Configuration
@EnableAsync
@EnableScheduling
public class MyScheduler {

    private static final Logger logger = LogManager.getLogger(MyScheduler.class);
	@Autowired private RestOperations restTemplate;
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	//private Gson gson = new Gson();

	@Scheduled(fixedDelay=43200000)
	public void fetchExchangeRates() {
		logger.info(this.getClass() + ": fetchExchangeRates");
		String uri = "https://blockchain.info/ticker";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
		//String jsonString = gson.toJson(response.getBody());
	    simpMessagingTemplate.convertAndSend("/topic/exchanges", response.getBody());
	}
}
