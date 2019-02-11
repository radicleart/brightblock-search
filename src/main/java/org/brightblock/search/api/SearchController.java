package org.brightblock.search.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.search.rest.models.ApiModel;
import org.brightblock.search.rest.models.ResponseCodes;
import org.brightblock.search.service.blockstack.models.ZonefileModel;
import org.brightblock.search.service.index.DappsSearchService;
import org.brightblock.search.service.index.NamesSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@CrossOrigin(origins = { "http://localhost:8080", "https://radicle.art", "https://brightblock.org", "https://transit8.com" }, maxAge = 6000)
public class SearchController {

	@Autowired private NamesSearchService namesSearchService;
	@Autowired private DappsSearchService dappsSearchService;

	@RequestMapping(value = "/index/names/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetchAllNames(HttpServletRequest request) {
		List<ZonefileModel> records = namesSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/index/dapps/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetchAllDapps(HttpServletRequest request) {
		List<IndexableModel> records = dappsSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/index/names/query/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchNames(HttpServletRequest request, @PathVariable String field) {
		String query = request.getParameter("query");
		if (query == null) {
			query = request.getParameter("q");
		}
		List<ZonefileModel> response = namesSearchService.searchIndex(field, query);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/index/dapps/{domain}/{objType}/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String objType, @PathVariable String domain, @PathVariable String field) {
		String query = request.getParameter("query");
		if (query == null) {
			query = request.getParameter("q");
		}
		List<IndexableModel> response = dappsSearchService.searchIndex(200, objType, domain, field, query);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

}
