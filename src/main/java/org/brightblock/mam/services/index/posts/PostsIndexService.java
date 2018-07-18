package org.brightblock.mam.services.index.posts;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

public interface PostsIndexService
{
	public void buildIndex() throws IOException;
	
	public List<PostModel> searchIndex(String inField, String queryString) throws ParseException, IOException;

}
