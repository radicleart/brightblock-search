package org.brightblock.mam.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.index.NamesIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NamesIndexController {

	@Autowired private NamesIndexService namesIndexService;

	@RequestMapping(value = "/names/index/build/{from}/{to}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> buildInd(HttpServletRequest request, @PathVariable int from, @PathVariable int to) {
		namesIndexService.buildIndex(from, to);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building from page " + from + " to page " + to + "in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/names/index/build/{names}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> buildInd(HttpServletRequest request, @PathVariable List<String> names) {
		namesIndexService.buildIndex(names);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building names " + names.toString() + "in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/names/index/size", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> sizeOfIndex(HttpServletRequest request) {
		int sizeOfIndex = namesIndexService.getNumbDocs();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, sizeOfIndex);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
}
