package org.brightblock.mam.webrtc;

import javax.servlet.http.HttpServletRequest;

import org.brightblock.mam.rest.models.ApiModel;
import org.brightblock.mam.rest.models.ResponseCodes;
import org.brightblock.mam.services.webrtc.TimestampModel;
import org.brightblock.mam.services.webrtc.TokenModel;
import org.brightblock.mam.services.webrtc.WebrtcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.opentok.Session;
import com.opentok.exception.OpenTokException;

@Controller
public class TokBoxSessionController {
	@Autowired private WebrtcService webrtcService;

	@RequestMapping(value = "/token/{username}/{recordId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> token(HttpServletRequest request, @PathVariable String username, @PathVariable String recordId) throws OpenTokException {
		Session session = webrtcService.getSession(recordId);
		TokenModel tm = webrtcService.getToken(session, username, recordId);
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, tm);
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/server/time", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiModel> servertime() {
		ApiModel model = ApiModel.getSuccess(ResponseCodes.OK, new TimestampModel());
		return new ResponseEntity<ApiModel>(model, HttpStatus.OK);
	}
}
