package org.brightblock.mam.services.index.names;

import java.util.List;

import org.brightblock.mam.services.blockstack.models.ZonefileModel;


public interface NamesSearchService
{
	public List<ZonefileModel> searchIndex(String inField, String query);

}
