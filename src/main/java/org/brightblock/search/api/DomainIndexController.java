package org.brightblock.search.api;

import org.brightblock.search.api.model.DomainIndexModel;
import org.brightblock.search.service.index.DappsSearchService;
import org.brightblock.search.service.project.DomainIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = { "http://localhost:8085", "http://localhost:8082", "http://localhost:8080", "https://prom.risidio.com", "https://thisisnumberone.com", "https://staging.thisisnumberone.com", "https://tchange.risidio.com", "https://tchange.risidio.com", "https://xchange.risidio.com", "https://truma.risidio.com", "https://ruma.risidio.com", "https://loopbomb.risidio.com", "https://stacks.loopbomb.com", "https://stacksmate.com", "https://test.stacksmate.com" }, maxAge = 6000)
public class DomainIndexController {

	@Autowired private DomainIndexService projectService;
	@Autowired private DappsSearchService dappsSearchService;

	@PostMapping(value = "/v1/domain/register")
	public DomainIndexModel application(@RequestBody DomainIndexModel domainIndexModel) {
		return projectService.save(domainIndexModel);
	}

	@GetMapping(value = "/v1/persist")
	public void owners() {
		dappsSearchService.distinctOwners();
		return;
	}

	@GetMapping(value = "/v1/creators")
	public void fetchDistinctCreators() {
		dappsSearchService.fetchDistinctCreators();
		return;
	}

	@GetMapping(value = "/v1/owners")
	public void fetchDistinctOwners() {
		dappsSearchService.fetchDistinctOwners();
		return;
	}

}
