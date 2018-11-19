package org.brightblock.search.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.search.rest.models.ApiModel;
import org.brightblock.search.rest.models.ResponseCodes;
import org.brightblock.search.service.index.DappsIndexService;
import org.brightblock.search.service.index.NamesIndexService;
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
public class IndexController {

	@Autowired private NamesIndexService namesIndexService;
	@Autowired private DappsIndexService dappsIndexService;

	@RequestMapping(value = "/index/dapps/clear", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> clearDapps(HttpServletRequest request) {
		int size = dappsIndexService.clearAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, size);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/index/names/clear", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> clearNames(HttpServletRequest request) {
		int size = namesIndexService.clearAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, size);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/index/pages/{from}/{to}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> indexPages(HttpServletRequest request, @PathVariable int from, @PathVariable int to) {
		namesIndexService.indexPages(from, to);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Indexing from page " + from + " to page " + to + " in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/index/users/{names}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> indexUsers(HttpServletRequest request, @PathVariable List<String> names) {
		namesIndexService.indexUsers(names);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Indexing users: " + names.toString() + " in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/index/size", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> sizeOfIndex(HttpServletRequest request) {
		int sizeOfNamesIndex = namesIndexService.getNumbDocs();
		int sizeOfDappsIndex = dappsIndexService.getNumbDocs();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, new Integer[] {sizeOfNamesIndex, sizeOfDappsIndex});
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/index/addRecord", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> addRecord(HttpServletRequest request, @RequestBody IndexableModel indexData) {
		dappsIndexService.indexSingleRecord(indexData);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/index/removeRecord/{field}/{value}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> removeRecord(HttpServletRequest request, @PathVariable String field, @PathVariable String value) {
		dappsIndexService.remove(field, value);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Removing field " + field + " with value " + value + " in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	

}
