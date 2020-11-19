package org.brightblock.search.api;

import java.util.HashSet;
import java.util.Set;

import org.brightblock.search.api.model.ClarityApplicationModel;
import org.brightblock.search.api.model.ClarityAssetModel;
import org.brightblock.search.api.model.IndexableModel;
import org.brightblock.search.service.index.DappsIndexService;
import org.brightblock.search.service.index.DappsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class ClarityCacheController {

	@Autowired private DappsIndexService dappsIndexService;
	@Autowired private DappsSearchService dappsSearchService;
	private Set<ClarityApplicationModel> applications = new HashSet<ClarityApplicationModel>();

	@PostMapping(value = "/v1/application")
	public Boolean application(@RequestBody ClarityApplicationModel application) {
		IndexableModel asset = null;
		for (ClarityAssetModel cam : application.getClarityAssets()) {
			asset = (IndexableModel) dappsSearchService.findByAssetHash(cam.getAssetHash());
			asset.setNftIndex(cam.getNftIndex());
			asset.setTradeInfo(cam.getTradeInfo());
			dappsIndexService.indexSingleRecord(asset);
		}
		if (!applications.contains(application)) {
			applications.add(application);
		} else {
			applications.remove(application);
			applications.add(application);
		}
		return true;
	}

}
