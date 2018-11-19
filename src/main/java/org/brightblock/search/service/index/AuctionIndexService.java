package org.brightblock.search.service.index;

import org.brightblock.search.service.index.posts.AuctionModel;
import org.brightblock.search.service.index.posts.AuctionsModel;

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
