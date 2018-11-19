package org.brightblock.search.service.index;

import java.util.List;

public interface NamesIndexService
{
	public int clearAll();
	public int getNumbDocs();
	public void indexPages(int from, int to);
	public void indexUsers(List<String> names);
}
