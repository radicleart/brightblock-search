package org.brightblock.mam.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.index.AuctionSearchService;
import org.brightblock.mam.services.index.posts.AuctionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuctionSearchController {

	@Autowired
	private AuctionSearchService auctionSearchService;

	@RequestMapping(value = "/auction/search/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> search(HttpServletRequest request, @PathVariable String field) {
		String query = request.getParameter("query");
		if (query == null) {
			query = request.getParameter("q");
		}
		List<AuctionModel> records = auctionSearchService.searchIndex(100, field, query);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/auction/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetch(HttpServletRequest request) {
		List<AuctionModel> records = auctionSearchService.fetchAll();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
}
