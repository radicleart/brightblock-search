package org.brightblock.mam.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.ethereum.service.EthereumService;
import org.brightblock.mam.ethereum.service.Item;
import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.index.ArtSearchService;
import org.brightblock.mam.services.index.posts.OwnershipRecordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ArtSearchController {

	@Autowired
	private ArtSearchService artSearchService;
	@Autowired
	private EthereumService ethereumService;

	@RequestMapping(value = "/art/search/{field}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> search(HttpServletRequest request, @PathVariable String field) {
		String query = request.getParameter("query");
		if (query == null) {
			query = request.getParameter("q");
		}
		List<OwnershipRecordModel> records = artSearchService.searchIndex(100, field, query);
		setBlockchainIndex(records);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/art/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> fetch(HttpServletRequest request) {
		List<OwnershipRecordModel> records = artSearchService.fetchAll();
		setBlockchainIndex(records);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, records);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
	
	private void setBlockchainIndex(List<OwnershipRecordModel> records) {
		List<Item> items = ethereumService.fetchItems();
		for (OwnershipRecordModel model : records) {
			for (Item item : items) {
				if (item.getTitle().equals(model.getTitle())) {
					model.setItem(item);
				}
			}
		}
	}

}
