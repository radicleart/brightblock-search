package org.brightblock.mam.services.index;

import java.util.List;

import org.brightblock.mam.services.index.posts.OwnershipRecordModel;


public interface ArtSearchService
{
	public List<OwnershipRecordModel> searchIndex(int limit, String inField, String query);
	public List<OwnershipRecordModel> fetchAll();

}
