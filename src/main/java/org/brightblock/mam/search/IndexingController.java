package org.brightblock.mam.search;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.queryparser.classic.ParseException;
import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.index.posts.PostModel;
import org.brightblock.mam.services.index.posts.PostsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexingController {

	@Autowired private PostsIndexService searchIndexService;

	@RequestMapping(value = "/index/build", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> build(HttpServletRequest request) throws IOException {
		searchIndexService.buildIndex();
		//ResponseEntity<String> response = restTemplate.getForEntity(applicationSettings.getBlockstackBase() + "/v1/node/ping", String.class);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, "Built Index");
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/index/search/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<PostModel>> search(HttpServletRequest request, @PathVariable String field) throws IOException, ParseException {
		String query = request.getParameter("query");
		if (query == null) {
			query = request.getParameter("q");
		}
		List<PostModel> docs = searchIndexService.searchIndex(field, query);
		return new ResponseEntity<List<PostModel>>(docs, HttpStatus.OK);
	}

}
