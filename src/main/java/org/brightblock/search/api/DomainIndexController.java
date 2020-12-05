package org.brightblock.search.api;

import java.util.List;

import org.brightblock.search.api.model.DomainIndexModel;
import org.brightblock.search.api.model.RatesModel;
import org.brightblock.search.service.index.DappsSearchService;
import org.brightblock.search.service.project.DomainIndexService;
import org.brightblock.search.service.project.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class DomainIndexController {

	@Autowired private DomainIndexService projectService;
	@Autowired private RatesRepository ratesRepository;
	@Autowired private DappsSearchService dappsSearchService;

	@PostMapping(value = "/v1/domain/register")
	public DomainIndexModel application(@RequestBody DomainIndexModel domainIndexModel) {
		return projectService.save(domainIndexModel);
	}

	@PostMapping(value = "/v1/rates")
	public RatesModel rates(@RequestBody RatesModel ratesModel) {
		return ratesRepository.save(ratesModel);
	}

	@GetMapping(value = "/v1/rates")
	public RatesModel rates() {
		List<RatesModel> rm = ratesRepository.findAll();
		if (rm != null) return rm.get(0);
		return null;
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
