package org.brightblock.search.service.index;

import java.util.List;

import org.brightblock.search.service.IndexableModel;


public interface DappsSearchService
{
	public List<IndexableModel> searchIndex(int limit, String objType, String domain, String inField, String query);
	public List<IndexableModel> fetchAll();

}
