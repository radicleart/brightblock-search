package org.brightblock.search.api;

import java.util.List;
import java.util.Map;

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
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class SearchController {

	@Autowired private NamesSearchService namesSearchService;
	@Autowired private DappsSearchService dappsSearchService;

	@RequestMapping(value = "/names/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetchAllNames(HttpServletRequest request) {
		List<ZonefileModel> records = namesSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/names/query/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchNames(HttpServletRequest request, @PathVariable String field) {
		List<ZonefileModel> response = namesSearchService.searchIndex(field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetchAllDapps(HttpServletRequest request) {
		List<SearchResultModel> records = dappsSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/findBy/{fieldName}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetchAllDapps(HttpServletRequest request, @PathVariable String fieldName) {
		List<SearchResultModel> records = dappsSearchService.fetchAll(fieldName);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/findByDomainAndObjectTypeAndTitleOrDescriptionOrCategoryOrKeyword/{domain}/{objType}/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String objType, @PathVariable String domain, @PathVariable String field) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, objType, domain, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/findByObjectTypeAndTitleOrDescriptionOrCategoryOrKeyword/{objType}/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String objType, @PathVariable String field) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, objType, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/findArtworkByTitleOrDescriptionOrCategoryOrKeyword/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String field) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/asset/{assetHash}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> getAsset(HttpServletRequest request, @PathVariable String assetHash) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, "id", assetHash);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/findArtworkByTitleOrDescriptionOrCategoryOrKeyword", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, "title", getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/findByProject/{projectId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchProjects(HttpServletRequest request, @PathVariable String projectId) {
		List<SearchResultModel> response = dappsSearchService.searchProject(200, projectId);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/findByObject/{objectType}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchByObjectType(HttpServletRequest request, @PathVariable String objectType) {
		List<SearchResultModel> response = dappsSearchService.searchObjectType(200, objectType);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/countByDomainAndObjectTypeAndCategories/{domain}/{objType}/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> categories(HttpServletRequest request, @PathVariable String objType, @PathVariable String domain, @PathVariable String field) {
		List<Map<String, Object>> response = dappsSearchService.searchCategories(objType, domain, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	private String getQuery(HttpServletRequest request) {
		String query = request.getParameter("query");
		if (query == null) {
			query = request.getParameter("q");
		}
		return query;
	}

}
