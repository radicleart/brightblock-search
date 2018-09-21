package org.brightblock.mam.ethereum.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.conf.ApplicationSettings;
import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestOperations;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

@Controller
public class Web3EthereumClient {

	private static final Logger logger = LogManager.getLogger(Web3EthereumClient.class);
	private final static String UTF8 = "UTF-8";
	@Autowired private RestOperations restTemplate;
	@Autowired private ApplicationSettings applicationSettings;

	@RequestMapping(value = "/api/ethereum/client1", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> client1() throws IOException {
		Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
		Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
		String clientVersion = web3ClientVersion.getWeb3ClientVersion();
		logger.info(clientVersion);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, clientVersion);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/client2", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> client2() throws IOException {
		HttpHeaders headers = getHeaders();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("jsonrpc", "2.0");
		map.add("method", "web3_clientVersion");
		map.add("params", "[]");
		map.add("id", "67");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity( applicationSettings.getGethHttpBase(), request , String.class );
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response.getBody());
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	private HttpHeaders getHeaders() {
		// String auth = applicationSettings.getBitcoinRpcUser() + ":" + applicationSettings.getBitcoinRpcPasssword();
		// byte[] encodedAuth = Base64Utils.encode(auth.getBytes(Charset.forName("iso-8859-1")));
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", Charset.forName(UTF8));
		headers.setContentType(mediaType);
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(mediaType);
		headers.setAccept(mediaTypes);
		// headers.set("Authorization", "Basic " + encodedAuth);
		return headers;
	}

}
