package org.brightblock.search.service.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.brightblock.search.service.IndexableModel;
import org.springframework.stereotype.Service;

@Service
public class DappsSearchServiceImpl extends BaseIndexingServiceImpl implements DappsSearchService {

	@Override
	public List<IndexableModel> fetchAll() {
		try {
			initArtMarket();
			List<IndexableModel> models = new ArrayList<IndexableModel>();
			IndexReader reader = DirectoryReader.open(artIndex);
			Document document = null;
			for (int i=0; i < reader.maxDoc(); i++) {
				try {
					document = reader.document(i);
					IndexableModel model = convertToRecord(document);
					models.add(model);
				} catch (IOException e) {
					logger.error("Error reading document at: " + i + " Error thrown: " + e.getMessage());
				}
			}
			return models;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<IndexableModel> searchIndex(int limit, String objType, String domain, String inField, String searchTerm) {
		try {
			initArtMarket();
			if (searchTerm == null || searchTerm.length() == 0) {
				searchTerm = "*";
			}
			String query = "domain:" + domain + " AND objType:" + objType + " AND " + inField + ":" + searchTerm;			
			QueryParser qp = new QueryParser(inField, artAnalyzer);
			if (inField.equals("description") || inField.equals("title") || inField.equals("keywords")) {
				qp.setAllowLeadingWildcard(true);
			}
			Query q = qp.parse(query);
			IndexReader indexReader = DirectoryReader.open(artIndex);
			IndexSearcher searcher = new IndexSearcher(indexReader);
			TopDocs topDocs = searcher.search(q, limit);
			List<IndexableModel> models = new ArrayList<IndexableModel>();
			Document document = null;
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				document = searcher.doc(scoreDoc.doc);
				IndexableModel model = convertToRecord(document);
				models.add(model);
			}
			return models;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private IndexableModel convertToRecord(Document document) {
		IndexableModel model = new IndexableModel();
		model.setDescription(document.get("description"));
		model.setId(Long.valueOf(document.get("id")));
		model.setObjType(document.get("objType"));
		model.setOwner(document.get("owner"));
		model.setKeywords(document.get("keywords"));
		model.setTitle(document.get("title"));
		model.setDomain(document.get("domain"));
		return model;
	}
}
