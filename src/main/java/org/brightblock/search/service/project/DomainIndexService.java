package org.brightblock.search.service.project;

import java.util.List;
import java.util.Optional;

import org.brightblock.search.api.model.DomainIndexModel;
import org.brightblock.search.api.model.RatesModel;

public interface DomainIndexService
{
	public RatesModel save(RatesModel ratesModel);
	public DomainIndexModel save(DomainIndexModel domainIndexModel);
    public List<DomainIndexModel> findByDomain(String domain);
    public List<DomainIndexModel> findByOwner(String owner);
    public List<DomainIndexModel> findAll();
	public Optional<DomainIndexModel> findByProjectId(String projectId);
}
