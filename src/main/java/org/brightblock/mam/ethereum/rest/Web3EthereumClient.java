package org.brightblock.mam.ethereum.rest;

import java.io.IOException;
import java.math.BigInteger;

import org.brightblock.mam.ethereum.service.EthereumService;
import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Web3EthereumClient {

	@Autowired private EthereumService ethereumService;

	@RequestMapping(value = "/api/ethereum/client", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> client1() throws IOException {
		String clientVersion = ethereumService.getWeb3ClientVersion();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK,clientVersion);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/deploy", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> deploy() throws Exception {
		ArtMarket contract = ethereumService.deployContract();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, contract);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/load", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> load() throws Exception {
		ArtMarket contract = ethereumService.loadContract();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, contract);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/ethereum/numberOfItems", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> items() throws Exception {
		BigInteger result = ethereumService.numbItems();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, result);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
}
