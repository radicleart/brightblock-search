package org.brightblock.search.service.index;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.brightblock.search.api.model.CreatorsModel;
import org.brightblock.search.api.model.IndexableModel;
import org.brightblock.search.api.model.OwnersModel;
import org.brightblock.search.api.model.SearchResultModel;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface DappsSearchService
{
	public List<Map<String, Object>> searchCategories(String objType, String domain, String inField, String query);
	public List<IndexableModel> searchIndex(int limit, String objType, String domain, String inField, String query);
	public List<IndexableModel> searchIndex(int limit, String objType, String inField, String query);
	public List<IndexableModel> searchIndex(int limit, String inField, String query);
	public List<IndexableModel> searchIndex(String defaultField, int limit, String query);
	public List<IndexableModel> findByProjectId(int limit, String projectId) throws JsonProcessingException;
	public IndexableModel findByAssetHash(String assetHash);
	public List<SearchResultModel> fetchAll();
	public List<IndexableModel> fetchAll(String fieldName);
	public List<IndexableModel> searchObjectType(int limit, String searchTerm);
	public List<IndexableModel> findByOwner(int limit, String query);
	public List<SearchResultModel> findBySaleType(int limit, Long query);
	public Set<String> distinctOwners();
	public OwnersModel fetchDistinctOwners();
	public CreatorsModel fetchDistinctCreators();

}
