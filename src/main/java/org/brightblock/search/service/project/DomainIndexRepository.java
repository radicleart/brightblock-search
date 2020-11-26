package org.brightblock.search.service.project;

import java.util.List;
import java.util.Optional;

import org.brightblock.search.api.model.DomainIndexModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DomainIndexRepository extends MongoRepository<DomainIndexModel, String> {

    public List<DomainIndexModel> findByOwner(String owner);
    public List<DomainIndexModel> findByDomain(String domain);
    public Optional<DomainIndexModel> findByProjectId(String projectId);
}
