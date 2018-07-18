package org.brightblock.mam.blockstack.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.blockstack.models.ZonefileModel;
import org.brightblock.mam.services.index.names.NamesIndexService;
import org.brightblock.mam.services.index.names.NamesSearchService;
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
	@Autowired private NamesSearchService namesSearchService;

	@RequestMapping(value = "/blockstack/index/build/{from}/{to}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> buildInd(HttpServletRequest request, @PathVariable int from, @PathVariable int to) {
		namesIndexService.buildIndex(from, to);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/blockstack/index/build/{names}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> buildInd(HttpServletRequest request, @PathVariable List<String> names) {
		namesIndexService.buildIndex(names);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/blockstack/index/size", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> sizeOfIndex(HttpServletRequest request) {
		int sizeOfIndex = namesIndexService.getNumbDocs();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, sizeOfIndex);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/blockstack/search/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> search(HttpServletRequest request, @PathVariable String field) {
		String query = request.getParameter("query");
		if (query == null) {
			query = request.getParameter("q");
		}
		List<ZonefileModel> response = namesSearchService.searchIndex(field, query);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

}
