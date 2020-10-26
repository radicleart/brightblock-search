package org.brightblock.search.service.project;

import java.util.List;
import java.util.Optional;

import org.brightblock.search.service.project.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired private ProjectRepository projectRepository;

	@Override
	public Project save(Project fromJson) {
		Optional<Project> fromDb = projectRepository.findByProjectId(fromJson.getProjectId());
		Project project;
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
	public List<Project> findByOwner(String owner) {
		List<Project> projects = projectRepository.findByOwner(owner);
		return projects;
	}

	@Override
	public Optional<Project> findByProjectId(String projectId) {
		Optional<Project> o = projectRepository.findByProjectId(projectId);
		return o;
	}

	@Override
	public List<Project> findByDomain(String domain) {
		List<Project> o = projectRepository.findByDomain(domain);
		return o;
	}

	@Override
	public List<Project> findAll() {
		return projectRepository.findAll();
	}
}
