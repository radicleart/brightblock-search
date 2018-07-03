package org.brightblock.mam.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<ApiModel> handleResourceAccessException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		logger.error("bitcoin: error = " + e.getMessage(), e);
		ApiModel model = ApiModel.getFailure(ResponseCodes.SERVICE_UNAVAILABLE, e.getMessage());
		return new ResponseEntity<ApiModel>(model, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler
	public ResponseEntity<ApiModel> handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		logger.error("reporting-error: [request = " + request.toString() + "] error = " + e.getMessage(), e);
		ApiModel model = ApiModel.getFailure(ResponseCodes.BAD_REQUEST, e.getMessage());
		return new ResponseEntity<ApiModel>(model, HttpStatus.BAD_REQUEST);
	}

}
