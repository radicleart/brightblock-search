package org.brightblock.search.service.project;

import java.util.List;
import java.util.Optional;

import org.brightblock.search.service.project.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    public List<Project> findByOwner(String owner);
    public List<Project> findByDomain(String domain);
    public Optional<Project> findByProjectId(String projectId);
}
