package org.brightblock.mam.services.index;

import java.util.List;

import org.brightblock.mam.services.index.posts.AuctionModel;


public interface AuctionSearchService
{
	public List<AuctionModel> searchIndex(int limit, String inField, String query);
	public List<AuctionModel> fetchAll();

}
