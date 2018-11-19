package org.brightblock.search.service.index;

import java.util.List;

import org.brightblock.search.service.blockstack.models.ZonefileModel;


public interface NamesSearchService
{
	public List<ZonefileModel> searchIndex(String inField, String query);
	public List<ZonefileModel> fetchAll();
}
