package org.brightblock.search.service.index;

import org.brightblock.search.api.model.IndexableContainerModel;
import org.brightblock.search.api.model.IndexableModel;
import org.brightblock.search.service.blockstack.models.ZonefileModel;

public interface DappsIndexService
{
	public void remove(String field, String value);
	public void indexSingleRecord(IndexableModel record);
	public int getNumbDocs();
	public int clearAll();
	public void addToIndex(IndexableContainerModel records);
	public void indexUser(ZonefileModel zonefile);
}
