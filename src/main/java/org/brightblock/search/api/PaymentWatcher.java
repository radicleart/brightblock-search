package org.brightblock.search.api;

import org.brightblock.search.service.index.DappsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;

@Configuration
@EnableAsync
@EnableScheduling
public class PaymentWatcher {

	@Autowired private DappsSearchService dappsSearchService;

	@Scheduled(fixedDelay=120000)
	public void distinctOwners() throws JsonProcessingException {
		dappsSearchService.distinctOwners();
	}
	
}
