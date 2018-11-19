package org.brightblock.search.service.index;

import org.brightblock.search.service.index.posts.OwnershipRecordModel;
import org.brightblock.search.service.index.posts.OwnershipRecordsModel;

public interface ArtIndexService
{
	public int getNumbDocs();
	public void buildIndex();
	public void remove(String field, String value);
	public int clear();
	public void addToIndex(OwnershipRecordsModel record);
	public void indexSingleRecord(OwnershipRecordModel record);
	public void indexUser(String username);
}
