package org.brightblock.search.service.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.brightblock.search.api.model.CreatorsModel;
import org.brightblock.search.api.model.IndexableModel;
import org.brightblock.search.api.model.KeywordModel;
import org.brightblock.search.api.model.OwnersModel;
import org.brightblock.search.api.model.SearchResultModel;
import org.brightblock.search.api.v2.Attributes;
import org.brightblock.search.api.v2.MediaObject;
import org.brightblock.search.service.project.CreatorsRepository;
import org.brightblock.search.service.project.OwnersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DappsSearchServiceImpl extends BaseIndexingServiceImpl implements DappsSearchService {

	private static final long HUNDRED_DAYS = 8640000000L;
	@Autowired private OwnersRepository ownersRepository;
	@Autowired private CreatorsRepository creatorsRepository;

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
	public Set<String> distinctOwners() {
		try {
			initArtMarket();
			Set<String> owners = new TreeSet<String>();
			Set<String> creators = new TreeSet<String>();
			IndexReader reader = DirectoryReader.open(artIndex);
			Document document = null;
			for (int i = 0; i < reader.maxDoc(); i++) {
				try {
					document = reader.document(i);
					String owner = document.get("owner");
					String creator = document.get("artist");
					if (!owners.contains(owner)) owners.add(owner);
					if (!creators.contains(creator)) creators.add(creator);
				} catch (IOException e) {
					logger.error("Error reading document at: " + i + " Error thrown: " + e.getMessage());
				}
			}
			List<OwnersModel> allOwners = ownersRepository.findAll();
			if (allOwners == null || allOwners.isEmpty()) {
				OwnersModel om = new OwnersModel();
				om.setOwners(owners);
				ownersRepository.save(om);
			} else {
				OwnersModel om = allOwners.get(0);
				om.setOwners(owners);
				ownersRepository.save(om);
			}
			
			List<CreatorsModel> allCreators = creatorsRepository.findAll();
			if (allCreators == null || allCreators.isEmpty()) {
				CreatorsModel om = new CreatorsModel();
				om.setCreators(owners);
				creatorsRepository.save(om);
			} else {
				CreatorsModel om = allCreators.get(0);
				om.setCreators(creators);
				creatorsRepository.save(om);
			}
			
			return owners;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public OwnersModel fetchDistinctOwners() {
		try {
			return ownersRepository.findAll().get(0);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CreatorsModel fetchDistinctCreators() {
		try {
			return creatorsRepository.findAll().get(0);
		} catch (Exception e) {
			return null;
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
		if (inField.equals("name")) {
			if (searchTerm == null || searchTerm.length() == 0) {
				searchTerm = "*";
			}
			query += " AND (name:" + searchTerm + " OR category:" + searchTerm + " OR description:" + searchTerm + " OR keywords:" + searchTerm + ")";
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
		if (inField.equals("name")) {
			if (searchTerm == null || searchTerm.length() == 0) {
				searchTerm = "*";
			}
			query += " AND (name:" + searchTerm + " OR category:" + searchTerm + " OR description:" + searchTerm + " OR keywords:" + searchTerm + ")";
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
	public List<SearchResultModel> searchIndex(String defaultField, int limit, String searchTerm) {
		return doSearch(limit, searchTerm, defaultField);
	}

	@Override
	public List<SearchResultModel> searchIndex(int limit, String inField, String searchTerm) {
		if (inField.equals("name")) {
			return doSearch(limit, "name:" + searchTerm, inField);
		} else {
			String query = "objType:artwork OR objType:trading_cards OR objType:certificates OR objType:digital_property OR objType:written_word OR objType:news_media";
			if (inField.equals("name")) {
				if (searchTerm == null || searchTerm.length() == 0) {
					searchTerm = "*";
				}
				query += " AND (name:" + searchTerm + " OR category:" + searchTerm + " OR description:" + searchTerm + " OR keywords:" + searchTerm + ")";
			} else if (inField.equals("facet")) {
				if (searchTerm != null && searchTerm.length() > 0) {
					query += " AND " + searchTerm;
				}
			} else {
				query += " AND (" + inField + ":" + searchTerm + ")";
			}
			return doSearch(limit, query, inField);
		}
	}

	@Override
	public List<SearchResultModel> findByProjectId(int limit, String searchTerm) {
		String query = "projectId:" + searchTerm;
		return doSearch(limit, query, "projectId");
	}

	@Override
	public SearchResultModel findByAssetHash(String assetHash) {
		String query = "assetHash:" + assetHash;
		List<SearchResultModel> results = doSearch(1, query, "assetHash");
		if (results == null || results.isEmpty()) return null;
		return results.get(0);
	}

	@Override
	public List<SearchResultModel> findByOwner(int limit, String searchTerm) {
		String query = "owner:" + searchTerm;
		return doSearch(limit, query, "owner");
	}

	@Override
	public List<SearchResultModel> findBySaleType(int limit, Long searchTerm) {
		List<Long> points = new ArrayList<Long>();
		points.add(searchTerm);
		Query query = LongPoint.newSetQuery("saleType", points);
		return doGeneralisedSearch(limit, query, "saleType");
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
			IndexReader indexReader = DirectoryReader.open(artIndex);
			QueryParser qp = new QueryParser(inField, artAnalyzer);
			Query q = qp.parse(query);
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

	private List<SearchResultModel> doGeneralisedSearch(int limit, Query query, String inField) {
		try {
			initArtMarket();
			IndexReader indexReader = DirectoryReader.open(artIndex);
			IndexSearcher searcher = new IndexSearcher(indexReader);
			TopDocs topDocs = searcher.search(query, limit);
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
		} else {
			return convertToArtwork(document);
		}
	}
	
	private SearchResultModel convertToArtwork(Document document) {
		IndexableModel model = new IndexableModel();
		model.setDescription(document.get("description"));
		model.setAssetHash(document.get("assetHash"));
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

		model.setUploader(document.get("uploader"));
		model.setOwner(document.get("owner"));
		model.setProjectId(document.get("projectId"));
		model.setImage(document.get("image"));
		model.setMetaDataUrl(document.get("metaDataUrl"));
		model.setExternalUrl(document.get("externalUrl"));
		model.setArtist(document.get("artist"));
		
		Attributes nfm = new Attributes();
		nfm.setCoverArtist(document.get("coverArtist"));
		
		MediaObject awf = new MediaObject();
		awf.setSize(document.get("awfSize"));
		awf.setFileUrl(document.get("awfFileUrl"));
		awf.setStorage(document.get("awfStorage"));
		awf.setType(document.get("awfType"));
		nfm.setArtworkFile(awf);
		
		awf = new MediaObject();
		awf.setSize(document.get("awcSize"));
		awf.setFileUrl(document.get("awcFileUrl"));
		awf.setStorage(document.get("awcStorage"));
		awf.setType(document.get("awcType"));
		nfm.setArtworkClip(awf);
		
		awf = new MediaObject();
		awf.setSize(document.get("aciSize"));
		awf.setFileUrl(document.get("aciFileUrl"));
		awf.setStorage(document.get("aciStorage"));
		awf.setType(document.get("aciType"));
		nfm.setCoverImage(awf);
		
		model.setAttributes(nfm);
		
		String csKeywords = document.get("keywords");
		if (csKeywords != null) {
			List<KeywordModel> kms = new ArrayList<>();
			String[] csList = csKeywords.split(",");
			KeywordModel km = null;
			for (String keywordId : csList) {
				km = new KeywordModel(null, keywordId, 0, null);
				kms.add(km);
			}
			model.setKeywords(kms);
		}
		String csCategory = document.get("category");
		if (csCategory != null) {
			KeywordModel km = new KeywordModel(null, csCategory, 0, null);
			model.setCategory(km);
		}
		model.setStatus(document.get("status"));
		model.setName(document.get("name"));
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
