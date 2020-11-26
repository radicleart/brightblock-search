package org.brightblock.search.service.project;

import java.util.List;
import java.util.Optional;

import org.brightblock.search.api.model.DomainIndexModel;
import org.brightblock.search.api.model.RatesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainIndexServiceImpl implements DomainIndexService {

	@Autowired private DomainIndexRepository projectRepository;
	@Autowired private RatesRepository ratesRepository;

	@Override
	public DomainIndexModel save(DomainIndexModel fromJson) {
		Optional<DomainIndexModel> fromDb = projectRepository.findByProjectId(fromJson.getProjectId());
		DomainIndexModel project;
		if (fromDb.isPresent()) {
			project = fromDb.get();
			project.setDomain(fromJson.getDomain());
			project.setOwner(fromJson.getOwner());
			project.setStoreageModel(fromJson.getStoreageModel());
			project.setIndexFiles(fromJson.getIndexFiles());
			// project.setProjectId(fromJson.getProjectId());
		} else {
			project = fromJson;
		}
		return projectRepository.save(project);
	}

	@Override
	public List<DomainIndexModel> findByOwner(String owner) {
		List<DomainIndexModel> projects = projectRepository.findByOwner(owner);
		return projects;
	}

	@Override
	public Optional<DomainIndexModel> findByProjectId(String projectId) {
		Optional<DomainIndexModel> o = projectRepository.findByProjectId(projectId);
		return o;
	}

	@Override
	public List<DomainIndexModel> findByDomain(String domain) {
		List<DomainIndexModel> o = projectRepository.findByDomain(domain);
		return o;
	}

	@Override
	public List<DomainIndexModel> findAll() {
		return projectRepository.findAll();
	}

	@Override
	public RatesModel save(RatesModel ratesModel) {
		ratesRepository.save(ratesModel);
		return ratesModel;
	}
}
