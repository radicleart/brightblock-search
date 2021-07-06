package org.brightblock.search.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.search.api.model.DomainIndexModel;
import org.brightblock.search.api.model.SearchResultModel;
import org.brightblock.search.rest.models.ApiModel;
import org.brightblock.search.rest.models.ResponseCodes;
import org.brightblock.search.service.blockstack.models.ZonefileModel;
import org.brightblock.search.service.index.DappsSearchService;
import org.brightblock.search.service.index.NamesSearchService;
import org.brightblock.search.service.project.DomainIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class SearchController {

	@Autowired private NamesSearchService namesSearchService;
	@Autowired private DappsSearchService dappsSearchService;
	@Autowired
	private DomainIndexService projectService;
    protected static final Logger logger = LogManager.getLogger(SearchController.class);

    @GetMapping(value = "/names/fetch")
	public ResponseEntity<ApiModel> fetchAllNames(HttpServletRequest request) {
		List<ZonefileModel> records = namesSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/names/query/{field}")
	public ResponseEntity<ApiModel> searchNames(HttpServletRequest request, @PathVariable String field) {
		List<ZonefileModel> response = namesSearchService.searchIndex(field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/projectsByProjectId/{projectId}")
	public Optional<DomainIndexModel> findByProjectId(HttpServletRequest request, @PathVariable String projectId) {
		Optional<DomainIndexModel> project = projectService.findByProjectId(projectId);
		return project;
	}
	
	@GetMapping(value = "/projectsByDomain/{domain}")
	public List<DomainIndexModel> findByDomain(HttpServletRequest request, @PathVariable String domain) {
		List<DomainIndexModel> projects = projectService.findByDomain(domain);
		return projects;
	}
	
	@GetMapping(value = "/projectsByOwner/{owner}")
	public List<DomainIndexModel> findByOwner(HttpServletRequest request, @PathVariable String owner) {
		List<DomainIndexModel> projects = projectService.findByOwner(owner);
		return projects;
	}
	
	@GetMapping(value = "/projectsAll")
	public List<DomainIndexModel> findAllProjects(HttpServletRequest request) {
		List<DomainIndexModel> projects = projectService.findAll();
		return projects;
	}

	@GetMapping(value = "/findAll")
	public ResponseEntity<ApiModel> fetchAllDapps(HttpServletRequest request) {
		List<SearchResultModel> records = dappsSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/findByProjectId")
	public ResponseEntity<ApiModel> findByProjectId(HttpServletRequest request) {
		List<SearchResultModel> records = dappsSearchService.findByProjectId(200, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/findByOwner")
	public ResponseEntity<ApiModel> findByOwner(HttpServletRequest request) {
		List<SearchResultModel> records = dappsSearchService.findByOwner(200, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/findBySaleType/{saleType}")
	public ResponseEntity<ApiModel> findBySaleType(HttpServletRequest request, @PathVariable Long saleType) {
		List<SearchResultModel> records = dappsSearchService.findBySaleType(200, saleType);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/findByDomainAndObjectTypeAndTitleOrDescriptionOrCategoryOrKeyword/{domain}/{objType}/{field}")
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String objType, @PathVariable String domain, @PathVariable String field) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, objType, domain, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/findByObjectTypeAndTitleOrDescriptionOrCategoryOrKeyword/{objType}/{field}")
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String objType, @PathVariable String field) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, objType, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@GetMapping(value = "/findByTitleOrDescriptionOrCategoryOrKeyword/{field}")
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String field) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@GetMapping(value = "/v1/asset/{assetHash}")
	public SearchResultModel getAsset(HttpServletRequest request, @PathVariable String assetHash) {
		try {
			SearchResultModel asset = dappsSearchService.findByAssetHash(assetHash);
			return asset;
		} catch (Exception e) {
			logger.info("No asset found for hash: " + assetHash);
			return null;
		}
	}
	
	@GetMapping(value = "/findByTitleOrDescriptionOrCategoryOrKeyword")
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, "title", getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@GetMapping(value = "/findByObject/{objectType}")
	public ResponseEntity<ApiModel> searchByObjectType(HttpServletRequest request, @PathVariable String objectType) {
		List<SearchResultModel> response = dappsSearchService.searchObjectType(200, objectType);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@GetMapping(value = "/countByDomainAndObjectTypeAndCategories/{domain}/{objType}/{field}")
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
