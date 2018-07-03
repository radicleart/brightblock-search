package org.brightblock.mam.bitcoin.blockchaininfo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Controller
public class ExchangeRatesController {
	@Autowired private RestOperations restTemplate;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/api/exchange/rates", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> exchanges(HttpServletRequest request) {
		String uri = "https://blockchain.info/ticker";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Map.class);
		ExchangeRateContainerModel ratesModel = new ExchangeRateContainerModel();
		ratesModel.setRates(response.getBody());
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, ratesModel);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK); 
	}

}
