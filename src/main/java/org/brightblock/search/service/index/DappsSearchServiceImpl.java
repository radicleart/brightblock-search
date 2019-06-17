package org.brightblock.search.service.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.brightblock.search.api.IndexableModel;
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
			// title:"foo bar" AND body:"quick fox"
			String query = "domain:" + domain + " AND objType:" + objType;	
			if (inField.equals("title")) {
				if (searchTerm == null || searchTerm.length() == 0) {
					searchTerm = "*";					
				}
				query += " AND (title:" + searchTerm + " OR description:" + searchTerm + " OR keywords:" + searchTerm + ")";
			} else if (inField.equals("facet")) {
				if (searchTerm != null && searchTerm.length() > 0) {
					query += " AND " + searchTerm;
				}
			} else {
				query += " AND (" + inField + ":" + searchTerm + ")";
			}
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
		model.setId(document.get("id"));
		model.setObjType(document.get("objType"));
		model.setOwner(document.get("owner"));
		model.setGallerist(document.get("gallerist"));
		model.setGalleryId(document.get("galleryId"));
		model.setArtist(document.get("artist"));
		model.setKeywords(document.get("keywords"));
		model.setBuyer(document.get("buyer"));
		model.setStatus(document.get("status"));
		model.setTxid(document.get("txid"));
		model.setTitle(document.get("title"));
		model.setDomain(document.get("domain"));
		List<IndexableField> fields = document.getFields();
		Map<String, String> metaData = new HashMap<String, String>();
		for (IndexableField field : fields) {
			String fieldname = field.stringValue();
			String value = document.get(fieldname);
			metaData.put(fieldname, value);
		}
		return model;
	}
}
