package org.brightblock.mam.search;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.index.ArtIndexService;
import org.brightblock.mam.services.index.posts.OwnershipRecordModel;
import org.brightblock.mam.services.index.posts.OwnershipRecordsModel;
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
public class ArtIndexController {

	@Autowired private ArtIndexService artIndexService;

	@RequestMapping(value = "/art/index/root", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> indexRootFile(HttpServletRequest request, @RequestBody OwnershipRecordsModel record) {
		artIndexService.addToIndex(record);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/art/index/indexData", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> indexRecord(HttpServletRequest request, @RequestBody OwnershipRecordModel indexData) {
		artIndexService.addToIndex(indexData);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/art/index/user/{username}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> indexUser(HttpServletRequest request, @PathVariable String username) {
		artIndexService.indexUser(username);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/art/index/build", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> build(HttpServletRequest request) {
		artIndexService.buildIndex();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/art/index/clear", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> clear(HttpServletRequest request) {
		int sizeOfIndex = artIndexService.clear();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, sizeOfIndex);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/art/index/size", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> sizeOfIndex(HttpServletRequest request) {
		int sizeOfIndex = artIndexService.getNumbDocs();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, sizeOfIndex);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

}
