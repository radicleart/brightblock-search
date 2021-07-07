package org.brightblock.search.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.search.api.model.IndexableModel;
import org.brightblock.search.api.v2.JsonRootFile;
import org.brightblock.search.api.v2.RootFile;
import org.brightblock.search.rest.models.ApiModel;
import org.brightblock.search.rest.models.ResponseCodes;
import org.brightblock.search.service.index.DappsIndexService;
import org.brightblock.search.service.index.NamesIndexService;
import org.brightblock.search.service.project.DomainIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class IndexController {

	@Autowired
	private NamesIndexService namesIndexService;
	@Autowired
	private DappsIndexService dappsIndexService;
	@Autowired
	private DomainIndexService projectService;
	@Autowired
	private ObjectMapper mapper;

	@GetMapping(value = "/dapps/clear")
	public ResponseEntity<ApiModel> clearDapps(HttpServletRequest request) {
		dappsIndexService.clearAll();
		return sizeOfIndex(request);
	}

	@GetMapping(value = "/names/clear")
	public ResponseEntity<ApiModel> clearNames(HttpServletRequest request) {
		namesIndexService.clearAll();
		return sizeOfIndex(request);
	}

	@GetMapping(value = "/pages/{from}/{to}")
	public ResponseEntity<ApiModel> indexPages(HttpServletRequest request, @PathVariable int from,
			@PathVariable int to) {
		namesIndexService.indexPages(from, to);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK,
				"Indexing from page " + from + " to page " + to + " in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/users/{names}")
	public ResponseEntity<ApiModel> indexUsers(HttpServletRequest request, @PathVariable List<String> names) {
		namesIndexService.indexUsers(names);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK,
				"Indexing users: " + names.toString() + " in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/size")
	public ResponseEntity<ApiModel> sizeOfIndex(HttpServletRequest request) {
		Map<String, Integer> indexSize = new HashMap<String, Integer>();
		indexSize.put("names", namesIndexService.getNumbDocs());
		indexSize.put("dapps", dappsIndexService.getNumbDocs());
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, indexSize);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@PostMapping(value = "/indexJsonRootFile")
	public ResponseEntity<ApiModel> indexJsonRootFile(HttpServletRequest request,
			@RequestBody JsonRootFile jsonRootFile) throws JsonMappingException, JsonProcessingException {

		RootFile rootFile = convert(jsonRootFile.getJsonRootFile());
		dappsIndexService.indexRecords(rootFile.getRecords());
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	private RootFile convert(String jsonResp) throws JsonMappingException, JsonProcessingException {
		RootFile rates = mapper.readValue(jsonResp, new TypeReference<RootFile>() {
		});
		return rates;
	}

	@PostMapping(value = "/indexRootFile")
	public ResponseEntity<ApiModel> indexRootFile(HttpServletRequest request, @RequestBody RootFile rootFile) {
		dappsIndexService.indexRecords(rootFile.getRecords());
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@PostMapping(value = "/addRecord")
	public ResponseEntity<ApiModel> addRecord(HttpServletRequest request, @RequestBody IndexableModel indexData) {
		dappsIndexService.indexSingleRecord(indexData);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@PostMapping(value = "/indexMetaData/{projectId}")
	public ResponseEntity<ApiModel> indexMetaData(HttpServletRequest request, @PathVariable String projectId, @RequestBody IndexableModel indexData) {
		indexData.setProjectId(projectId);
		dappsIndexService.indexSingleRecord(indexData);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Building in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@GetMapping(value = "/removeRecord/{field}/{value}")
	public ResponseEntity<ApiModel> removeRecord(HttpServletRequest request, @PathVariable String field,
			@PathVariable String value) {
		dappsIndexService.remove(field, value);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK,
				"Removing field " + field + " with value " + value + " in background.");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

}
