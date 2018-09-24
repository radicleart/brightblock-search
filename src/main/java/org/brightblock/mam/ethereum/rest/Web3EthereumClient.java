package org.brightblock.mam.ethereum.rest;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.ethereum.service.EthereumService;
import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(value = "/api/ethereum/deploy/{gasLimit}/{gas}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> deploy(HttpServletRequest request, @PathVariable Long gasLimit, @PathVariable Long gas) throws Exception {
		ArtMarket contract = ethereumService.deployContract(gasLimit, gas);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, contract);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/load/{gasLimit}/{gas}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> load(HttpServletRequest request, @PathVariable Long gasLimit, @PathVariable Long gas) throws Exception {
		ArtMarket contract = ethereumService.loadContract(gasLimit, gas);
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
