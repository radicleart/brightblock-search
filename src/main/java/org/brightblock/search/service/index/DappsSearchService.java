package org.brightblock.search.service.index;

import java.util.List;
import java.util.Map;

import org.brightblock.search.api.model.SearchResultModel;


public interface DappsSearchService
{
	public List<Map<String, Object>> searchCategories(String objType, String domain, String inField, String query);
	public List<SearchResultModel> searchIndex(int limit, String objType, String domain, String inField, String query);
	public List<SearchResultModel> searchIndex(int limit, String objType, String inField, String query);
	public List<SearchResultModel> searchIndex(int limit, String inField, String query);
	public List<SearchResultModel> findByProjectId(int limit, String projectId);
	public SearchResultModel findByAssetHash(String assetHash);
	public List<SearchResultModel> fetchAll();
	public List<SearchResultModel> fetchAll(String fieldName);
	public List<SearchResultModel> searchObjectType(int limit, String searchTerm);
	public List<SearchResultModel> findByOwner(int limit, String query);
	public List<SearchResultModel> findBySaleType(int limit, Long query);

}
