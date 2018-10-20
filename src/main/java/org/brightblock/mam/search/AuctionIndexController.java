package org.brightblock.mam.search;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.index.AuctionIndexService;
import org.brightblock.mam.services.index.posts.AuctionModel;
import org.brightblock.mam.services.index.posts.AuctionsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuctionIndexController {

	@Autowired private AuctionIndexService auctionIndexService;

	@RequestMapping(value = "/auction/index", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> indexRootFile(HttpServletRequest request, @RequestBody AuctionsModel records) {
		auctionIndexService.addToIndex(records);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/auction/reindexOne", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> reindexOne(HttpServletRequest request, @RequestBody AuctionModel record) {
		auctionIndexService.reindexOne(record);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Indexed: " + record.getTitle());
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/auction/index/user/{username}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> indexUser(HttpServletRequest request, @PathVariable String username) {
		auctionIndexService.indexUser(username);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/auction/index/remove/{field}/{value}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> remove(HttpServletRequest request, @PathVariable String field, @PathVariable String value) {
		auctionIndexService.remove(field, value);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Removing field " + field + " with value " + value + " in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/auction/index/build", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> build(HttpServletRequest request) {
		auctionIndexService.buildIndex();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/auction/index/clear", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> clear(HttpServletRequest request) {
		int sizeOfIndex = auctionIndexService.clear();
		sizeOfIndex = auctionIndexService.getNumbDocs();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, sizeOfIndex);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/auction/index/size", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> sizeOfIndex(HttpServletRequest request) {
		int sizeOfIndex = auctionIndexService.getNumbDocs();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, sizeOfIndex);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

}
