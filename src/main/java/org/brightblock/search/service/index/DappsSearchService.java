package org.brightblock.search.service.index;

import java.util.List;
import java.util.Map;

import org.brightblock.search.api.IndexableModel;


public interface DappsSearchService
{
	public List<Map<String, Object>> searchCategories(String objType, String domain, String inField, String query);
	public List<IndexableModel> searchIndex(int limit, String objType, String domain, String inField, String query);
	public List<IndexableModel> fetchAll();

}
