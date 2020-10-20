package org.brightblock.search.service.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.brightblock.search.api.KeywordModel;
import org.brightblock.search.api.ProjectModel;
import org.brightblock.search.api.SearchResultModel;
import org.springframework.stereotype.Service;

@Service
public class DappsSearchServiceImpl extends BaseIndexingServiceImpl implements DappsSearchService {

	private static final long HUNDRED_DAYS = 8640000000L;

	@Override
	public List<SearchResultModel> fetchAll() {
		try {
			initArtMarket();
			List<SearchResultModel> models = new ArrayList<SearchResultModel>();
			IndexReader reader = DirectoryReader.open(artIndex);
			Document document = null;
			for (int i = 0; i < reader.maxDoc(); i++) {
				try {
					document = reader.document(i);
					SearchResultModel model = convertToRecord(document);
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
	public List<SearchResultModel> fetchAll(String fieldName) {
		String query = "_exists_:" + fieldName;
		return doSearch(200, query, fieldName);
	}

	@Override
	public List<SearchResultModel> searchIndex(int limit, String objType, String domain, String inField, String searchTerm) {
		String query = "domain:" + domain + " AND objType:" + objType;
		if (inField.equals("title")) {
			if (searchTerm == null || searchTerm.length() == 0) {
				searchTerm = "*";
			}
			query += " AND (title:" + searchTerm + " OR category:" + searchTerm + " OR description:" + searchTerm + " OR keywords:" + searchTerm + ")";
		} else if (inField.equals("facet")) {
			if (searchTerm != null && searchTerm.length() > 0) {
				query += " AND " + searchTerm;
			}
		} else {
			query += " AND (" + inField + ":" + searchTerm + ")";
		}
		return doSearch(limit, query, inField);
	}

	@Override
	public List<SearchResultModel> searchIndex(int limit, String objType, String inField, String searchTerm) {
		String query = "objType:" + objType;
		if (inField.equals("title")) {
			if (searchTerm == null || searchTerm.length() == 0) {
				searchTerm = "*";
			}
			query += " AND (title:" + searchTerm + " OR category:" + searchTerm + " OR description:" + searchTerm + " OR keywords:" + searchTerm + ")";
		} else if (inField.equals("facet")) {
			if (searchTerm != null && searchTerm.length() > 0) {
				query += " AND " + searchTerm;
			}
		} else {
			query += " AND (" + inField + ":" + searchTerm + ")";
		}
		return doSearch(limit, query, inField);
	}

	@Override
	public List<SearchResultModel> searchIndex(int limit, String inField, String searchTerm) {
		String query = "objType:artwork";
		if (inField.equals("title")) {
			if (searchTerm == null || searchTerm.length() == 0) {
				searchTerm = "*";
			}
			query += " AND (title:" + searchTerm + " OR category:" + searchTerm + " OR description:" + searchTerm + " OR keywords:" + searchTerm + ")";
		} else if (inField.equals("facet")) {
			if (searchTerm != null && searchTerm.length() > 0) {
				query += " AND " + searchTerm;
			}
		} else {
			query += " AND (" + inField + ":" + searchTerm + ")";
		}
		return doSearch(limit, query, inField);
	}

	@Override
	public List<SearchResultModel> searchProject(int limit, String searchTerm) {
		String query = "objType:artwork AND projectId:" + searchTerm;
		return doSearch(limit, query, "projectId");
	}

	@Override
	public List<SearchResultModel> searchObjectType(int limit, String searchTerm) {
		String query = "objType:" + searchTerm;
		List<SearchResultModel> models = doSearch(limit, query, "objType");
		
		return models;
	}

	private List<SearchResultModel> doSearch(int limit, String query, String inField) {
		try {
			initArtMarket();
			QueryParser qp = new QueryParser(inField, artAnalyzer);
			Query q = qp.parse(query);
			IndexReader indexReader = DirectoryReader.open(artIndex);
			IndexSearcher searcher = new IndexSearcher(indexReader);
			TopDocs topDocs = searcher.search(q, limit);
			List<SearchResultModel> models = new ArrayList<SearchResultModel>();
			Document document = null;
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				document = searcher.doc(scoreDoc.doc);
				SearchResultModel model = convertToRecord(document);
				models.add(model);
			}
			return models;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Map<String, Object>> searchCategories(String objType, String domain, String inField, String searchTerm) {
		try {
			initArtMarket();
			String[] catIds = searchTerm.split(",");
			List<Map<String, Object>> results = new ArrayList<>();
			Map<String, Object> result = null;
			for (String catId : catIds) {
				String query = "domain:" + domain + " AND objType:" + objType + " AND (keywords:" + catId + " OR category:" + catId + ")";
				QueryParser qp = new QueryParser(inField, artAnalyzer);
				qp.setAllowLeadingWildcard(true);
				Query q = qp.parse(query);
				IndexReader indexReader = DirectoryReader.open(artIndex);
				IndexSearcher searcher = new IndexSearcher(indexReader);
				Integer hits = searcher.count(q);
				result = new HashMap<String, Object>();
				result.put("id", catId);
				result.put("hits", hits);
				results.add(result);
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SearchResultModel convertToRecord(Document document) {
		String objectType = document.get("objType");
		if (objectType.equals("artwork")) {
			return convertToArtwork(document);
		} else if (objectType.equals("project")) {
			return convertToProject(document);
		} else {
			return null;
		}
	}
	
	private SearchResultModel convertToProject(Document document) {
		ProjectModel model = new ProjectModel();
		model.setDescription(document.get("description"));
		model.setId(document.get("id"));
		model.setObjType(document.get("objType"));
		if (document.get("updated") == null) {
			model.setUpdated(new Date().getTime() - HUNDRED_DAYS); 
		} else {
			model.setUpdated(Long.parseLong(document.get("updated")));
		}
		if (document.get("created") == null) {
			model.setUpdated(new Date().getTime() - HUNDRED_DAYS); 
		} else {
			model.setUpdated(Long.parseLong(document.get("created")));
		}
		model.setOwner(document.get("owner"));
		model.setProjectId(document.get("projectId"));
		
		model.setAssetUrl(document.get("assetUrl"));
		String csKeywords = document.get("keywords");
		if (csKeywords != null) {
			List<KeywordModel> kms = new ArrayList<>();
			String[] csList = csKeywords.split(",");
			KeywordModel km = null;
			for (String keywordId : csList) {
				km = new KeywordModel(keywordId);
				kms.add(km);
			}
			model.setKeywords(kms);
		}
		String csCategory = document.get("category");
		if (csCategory != null) {
			KeywordModel km = new KeywordModel(csCategory);
			model.setCategory(km);
		}
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
	
	private SearchResultModel convertToArtwork(Document document) {
		IndexableModel model = new IndexableModel();
		model.setDescription(document.get("description"));
		model.setId(document.get("id"));
		model.setObjType("artwork");
		if (document.get("updated") == null) {
			model.setUpdated(new Date().getTime() - HUNDRED_DAYS); 
		} else {
			model.setUpdated(Long.parseLong(document.get("updated")));
		}
		if (document.get("created") == null) {
			model.setUpdated(new Date().getTime() - HUNDRED_DAYS); 
		} else {
			model.setUpdated(Long.parseLong(document.get("created")));
		}
		model.setOwner(document.get("owner"));
		model.setProjectId(document.get("projectId"));
		model.setAssetHash(document.get("assetHash"));
		model.setAssetProjectUrl(document.get("assetProjectUrl"));
		model.setAssetUrl(document.get("assetUrl"));
		model.setGallerist(document.get("gallerist"));
		model.setGalleryId(document.get("galleryId"));
		model.setArtist(document.get("artist"));
		String csKeywords = document.get("keywords");
		if (csKeywords != null) {
			List<KeywordModel> kms = new ArrayList<>();
			String[] csList = csKeywords.split(",");
			KeywordModel km = null;
			for (String keywordId : csList) {
				km = new KeywordModel(keywordId);
				kms.add(km);
			}
			model.setKeywords(kms);
		}
		String csCategory = document.get("category");
		if (csCategory != null) {
			KeywordModel km = new KeywordModel(csCategory);
			model.setCategory(km);
		}
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
