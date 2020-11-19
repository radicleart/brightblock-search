package org.brightblock.search.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.search.api.model.SearchResultModel;
import org.brightblock.search.rest.models.ApiModel;
import org.brightblock.search.rest.models.ResponseCodes;
import org.brightblock.search.service.blockstack.models.ZonefileModel;
import org.brightblock.search.service.index.DappsSearchService;
import org.brightblock.search.service.index.NamesSearchService;
import org.brightblock.search.service.project.ProjectService;
import org.brightblock.search.service.project.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class SearchController {

	@Autowired private NamesSearchService namesSearchService;
	@Autowired private DappsSearchService dappsSearchService;
	@Autowired
	private ProjectService projectService;

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

	@RequestMapping(value = "/projectsByProjectId/{projectId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Optional<Project> findByProjectId(HttpServletRequest request, @PathVariable String projectId) {
		Optional<Project> project = projectService.findByProjectId(projectId);
		return project;
	}
	
	@RequestMapping(value = "/projectsByDomain/{domain}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Project> findByDomain(HttpServletRequest request, @PathVariable String domain) {
		List<Project> projects = projectService.findByDomain(domain);
		return projects;
	}
	
	@RequestMapping(value = "/projectsByOwner/{owner}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Project> findByOwner(HttpServletRequest request, @PathVariable String owner) {
		List<Project> projects = projectService.findByOwner(owner);
		return projects;
	}
	
	@GetMapping(value = "/projectsAll")
	public List<Project> findAllProjects(HttpServletRequest request) {
		List<Project> projects = projectService.findAll();
		return projects;
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetchAllDapps(HttpServletRequest request) {
		List<SearchResultModel> records = dappsSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/findByProjectId", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> findByProjectId(HttpServletRequest request) {
		List<SearchResultModel> records = dappsSearchService.findByProjectId(200, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/findByOwner", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> findByOwner(HttpServletRequest request) {
		List<SearchResultModel> records = dappsSearchService.findByOwner(200, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/findBySaleType/{saleType}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> findBySaleType(HttpServletRequest request, @PathVariable Long saleType) {
		List<SearchResultModel> records = dappsSearchService.findBySaleType(200, saleType);
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
	
	@RequestMapping(value = "/findByTitleOrDescriptionOrCategoryOrKeyword/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request, @PathVariable String field) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, field, getQuery(request));
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/asset/{assetHash}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public SearchResultModel getAsset(HttpServletRequest request, @PathVariable String assetHash) {
		SearchResultModel asset = dappsSearchService.findByAssetHash(assetHash);
		return asset;
	}
	
	@RequestMapping(value = "/findByTitleOrDescriptionOrCategoryOrKeyword", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> searchDapps(HttpServletRequest request) {
		List<SearchResultModel> response = dappsSearchService.searchIndex(200, "title", getQuery(request));
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
