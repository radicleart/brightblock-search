package org.brightblock.mam.services.index;

import java.util.List;
import java.util.Set;

import org.brightblock.mam.services.index.posts.OwnershipRecordModel;


public interface ArtSearchService
{
	public List<OwnershipRecordModel> searchIndex(String inField, String query);
	public Set<OwnershipRecordModel> fetchAll();

}
