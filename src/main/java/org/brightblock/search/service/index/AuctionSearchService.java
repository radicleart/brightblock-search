package org.brightblock.search.service.index;

import java.util.List;

import org.brightblock.search.service.index.posts.AuctionModel;


public interface AuctionSearchService
{
	public List<AuctionModel> searchIndex(int limit, String inField, String query);
	public List<AuctionModel> fetchAll();

}
