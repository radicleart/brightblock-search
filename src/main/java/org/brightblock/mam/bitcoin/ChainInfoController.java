package org.brightblock.mam.bitcoin;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.conf.ApplicationSettings;
import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestOperations;

@Controller
public class ChainInfoController {

	private final static String UTF8 = "UTF-8";
	@Autowired private RestOperations restTemplate;
	@Autowired private ApplicationSettings applicationSettings;

	@RequestMapping(value = "/bitcoin/{endpoint}/{extension}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> getinfo(HttpServletRequest request, @PathVariable String endpoint, @PathVariable String extension) throws UnsupportedEncodingException {
		HttpEntity<String> requestEntity = new HttpEntity<String>(getHeaders());
		ResponseEntity<String> response = restTemplate.exchange(applicationSettings.getBitcoinBase() + "/rest/" + endpoint + "." + extension, HttpMethod.POST, requestEntity, String.class);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, response.getBody());
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	private HttpHeaders getHeaders() {
		String auth = applicationSettings.getBitcoinRpcUser() + ":" + applicationSettings.getBitcoinRpcPasssword();
		byte[] encodedAuth = Base64Utils.encode(auth.getBytes(Charset.forName("iso-8859-1")));
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", Charset.forName(UTF8));
		headers.setContentType(mediaType);
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(mediaType);
		headers.setAccept(mediaTypes);
		headers.set("Authorization", "Basic " + encodedAuth);
		return headers;
	}
}
