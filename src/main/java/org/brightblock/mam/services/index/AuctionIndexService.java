package org.brightblock.mam.services.index;

import org.brightblock.mam.services.index.posts.AuctionModel;
import org.brightblock.mam.services.index.posts.AuctionsModel;

public interface AuctionIndexService
{
	public int getNumbDocs();
	public void buildIndex();
	public void remove(String field, String value);
	public int clear();
	public void addToIndex(AuctionsModel record);
	public void reindexOne(AuctionModel record);
	public void indexUser(String username);
}
