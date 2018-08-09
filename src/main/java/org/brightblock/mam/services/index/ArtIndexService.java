package org.brightblock.mam.services.index;

import org.brightblock.mam.services.index.posts.OwnershipRecordModel;
import org.brightblock.mam.services.index.posts.OwnershipRecordsModel;

public interface ArtIndexService
{
	public int getNumbDocs();
	public void buildIndex();
	public int clear();
	public void addToIndex(OwnershipRecordsModel record);
	public void addToIndex(OwnershipRecordModel record);
	public void indexUser(String username);
}
