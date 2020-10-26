package org.brightblock.search.service.project;

import java.util.List;
import java.util.Optional;

import org.brightblock.search.service.project.domain.Project;

public interface ProjectService
{
	public Project save(Project project);
    public List<Project> findByDomain(String domain);
    public List<Project> findByOwner(String owner);
    public List<Project> findAll();
	public Optional<Project> findByProjectId(String projectId);
}
