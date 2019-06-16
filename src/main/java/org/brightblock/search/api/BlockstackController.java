package org.brightblock.search.api;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.search.rest.models.ApiModel;
import org.brightblock.search.rest.models.ResponseCodes;
import org.brightblock.search.service.blockstack.BlockstackApiService;
import org.brightblock.search.service.blockstack.models.ZonefileModel;
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
@CrossOrigin(origins = { "http://localhost:8080", "https://radicle.art", "https://dbid.io", "https://brightblock.org" }, maxAge = 6000)
public class BlockstackController {

	@Autowired private BlockstackApiService blockstackService;

	@RequestMapping(value = "/blockstack/ping", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> ping(HttpServletRequest request) throws UnsupportedEncodingException {
		String response = blockstackService.ping();
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/blockstack/names", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> names(HttpServletRequest request) throws UnsupportedEncodingException {
		Integer page;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
			page = 1;
		}
		List<String> response = blockstackService.names(page);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/blockstack/names/{name}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> name(HttpServletRequest request, @PathVariable String name) throws UnsupportedEncodingException {
		if (!name.endsWith(".id")) {
			name += ".id";
		}
		ZonefileModel response = blockstackService.getZonefile(name, false);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/blockstack/names/{name}/history", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> history(HttpServletRequest request, @PathVariable String name) throws UnsupportedEncodingException {
		if (!name.endsWith(".id")) {
			name += ".id";
		}
		String response = blockstackService.nameHistory(name);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/blockstack/names/{name}/zonefile/{zoneFileHash}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> zonefile(HttpServletRequest request, @PathVariable String name, @PathVariable String zoneFileHash) throws UnsupportedEncodingException {
		if (!name.endsWith(".id")) {
			name += ".id";
		}
		String response = blockstackService.nameZonefile(name, zoneFileHash);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response);
		model.setHeaders(request);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
}
