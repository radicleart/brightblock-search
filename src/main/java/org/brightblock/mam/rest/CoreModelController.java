package org.brightblock.mam.rest;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.rest.models.ConfigModel;
import org.brightblock.mam.rest.models.IApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class CoreModelController {

    private static final Logger logger = LogManager.getLogger(CoreModelController.class);
	@Autowired private ObjectMapper objectMapper;

	@RequestMapping(value = "/models", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<IApiModel> blogs(HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		ConfigModel model = new ConfigModel();
		model.setJsonRepresentation(convertToJSON(model));
		return new ResponseEntity<IApiModel>(model, HttpStatus.OK);
	}
	
	public String convertToJSON(Object value) throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter stringWriter = new StringWriter();
		try {
			objectMapper.writeValue(stringWriter, value);
			stringWriter.flush();
			return stringWriter.toString();
		} finally {
			try {
				stringWriter.close();
			} catch (IOException e) {
				logger.error("Error during finally: " + value, e);
			}
		}
	}

}
