package org.brightblock.mam.services.blockstack;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.conf.ApplicationSettings;
import org.brightblock.mam.services.blockstack.models.ProfileModel;
import org.brightblock.mam.services.blockstack.models.ZonefileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BlockstackApiServiceImpl implements BlockstackApiService {
    private static final Logger logger = LogManager.getLogger(BlockstackApiServiceImpl.class);
	@Autowired private ApplicationSettings applicationSettings;
	@Autowired private ObjectMapper mapper;
	@Autowired private RestOperations restTemplate1;

	@Override
	public String ping() {
		ResponseEntity<String> response = restTemplate1.getForEntity(applicationSettings.getBlockstackBase() + "/v1/node/ping", String.class);
		return response.getBody();
	}

	@Override
	public List<String> names(Integer page) {
		ResponseEntity<List<String>> response =
				restTemplate1.exchange(applicationSettings.getBlockstackBase() + "/v1/names?page=" + page,
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> names = response.getBody();
		return names;
	}

	@Override
	public ZonefileModel name(String name) {
		ResponseEntity<ZonefileModel> response = restTemplate1.getForEntity(applicationSettings.getBlockstackBase() + "/v1/names/"+name, ZonefileModel.class);
		return response.getBody();
	}

	@Override
	public String nameHistory(String name) {
		ResponseEntity<String> response = restTemplate1.getForEntity(applicationSettings.getBlockstackBase() + "/v1/names/" + name + "/history", String.class);
		return response.getBody();
	}

	@Override
	public String nameZonefile(String name, String zoneFileHash) {
		ResponseEntity<String> response = restTemplate1.getForEntity(applicationSettings.getBlockstackBase() + "/v1/names/" + name + "/zonefile/" + zoneFileHash, String.class);
		return response.getBody();
	}

	@Override
	public ProfileModel profile(String profileUrl) {
		ResponseEntity<ProfileModel> response;
		ProfileModel model = null;
		try {
			response = restTemplate1.getForEntity(profileUrl, ProfileModel.class);
			model = response.getBody();
		} catch (Exception e) {
			// call returned json but with text/plain content type!
			HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("text/plain")));
		    HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
		    String jsonFile = restTemplate1.exchange(profileUrl, HttpMethod.GET, requestEntity, String.class).getBody();
		    try {
		    		model = mapper.readValue(jsonFile, ProfileModel.class);
			} catch (Exception e1) {
				ProfileModel[] models = null;
				try {
					models = mapper.readValue(jsonFile, ProfileModel[].class);
					model = models[0];
				} catch (Exception e2) {
					logger.error("Error reading profile from: " + profileUrl);
				}
			}
		}
	    return model;
	}

}
