package org.brightblock.mam.ethereum.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.ethereum.service.ArtMarket;
import org.brightblock.mam.ethereum.service.ArtMarketJson;
import org.brightblock.mam.ethereum.service.EthereumService;
import org.brightblock.mam.ethereum.service.Item;
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

	@RequestMapping(value = "/api/ethereum/load/{gasLimit}/{gas}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> loadWithGas(HttpServletRequest request, @PathVariable Long gasLimit, @PathVariable Long gas) throws Exception {
		ArtMarket contract = ethereumService.loadContract(gasLimit, gas);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, contract);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/ethereum/load/{contractAddress}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> load(HttpServletRequest request, @PathVariable String contractAddress) throws Exception {
		ArtMarket contract = ethereumService.loadContract(contractAddress, EthereumService.remixGasLimit, EthereumService.remixGas);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, contract);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/ethereum/subscribe/blocks", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> subscribeBlocks() throws IOException {
		ethereumService.subscribeBlocks();;
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Subscribed to block events");
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/unsubscribe/blocks", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> unsubscribeBlocks() throws IOException {
		ethereumService.unSubscribeBlocks();;
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Unsubscribed to block events");
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/client", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> client1() throws IOException {
		ArtMarketJson amj = ethereumService.getContractInfo();
		
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, amj);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/deploy/{gasLimit}/{gas}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> deployWithGas(HttpServletRequest request, @PathVariable Long gasLimit, @PathVariable Long gas) throws Exception {
		String contractAddress = ethereumService.deployContract(gasLimit, gas);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Deployed contract: " + contractAddress);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/deploy", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> deploy(HttpServletRequest request) throws Exception {
		String contractAddress = ethereumService.deployContract(EthereumService.remixGasLimit, EthereumService.remixGas);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Deployed contract: " + contractAddress);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/ethereum/numberOfItems", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> items() throws Exception {
		BigInteger result = ethereumService.numbItems();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, result);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/ethereum/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetch() throws Exception {
		List<Item> items = ethereumService.fetchItems();
		Collections.sort(items, Collections.reverseOrder());
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, items);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/ethereum/fetch/{hash}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> items(HttpServletRequest request, @PathVariable String hash) throws Exception {
		Item item = ethereumService.lookupItemByHash(hash);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, item);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
}
