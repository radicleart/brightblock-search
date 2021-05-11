package org.brightblock.search.service.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.brightblock.search.api.model.DomainIndexFileModel;
import org.brightblock.search.api.model.DomainIndexModel;
import org.brightblock.search.api.model.IndexableContainerModel;
import org.brightblock.search.api.model.IndexableModel;
import org.brightblock.search.api.model.KeywordModel;
import org.brightblock.search.api.v2.NftMedia;
import org.brightblock.search.service.blockstack.models.ZonefileModel;
import org.brightblock.search.service.project.DomainIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DappsIndexServiceImpl extends BaseIndexingServiceImpl implements DappsIndexService {

	private static final Logger logger = LogManager.getLogger(DappsIndexServiceImpl.class);
	@Autowired
	private RestOperations restTemplate1;
	@Autowired
	private DomainIndexService projectService;
	@Autowired
	private ObjectMapper mapper;

	@Override
	public int clearAll() {
		IndexWriter writer = null;
		try {
			logger.info("-----------------------------------------------------------------------------------------");
			logger.info("Clear index - current size: " + getNumbDocs() + " documents");
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
			indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			writer = new IndexWriter(artIndex, indexWriterConfig);
			writer.deleteAll();
			int numbDocs = getNumbDocs();
			logger.info("Clear index - new size: " + numbDocs + " documents");
			logger.info("-----------------------------------------------------------------------------------------");
			return numbDocs;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public int getNumbDocs() {
		try {
			initArtMarket();
			IndexReader indexReader = DirectoryReader.open(artIndex);
			return indexReader.numDocs();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addToIndex(IndexableContainerModel userRecords) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		try {
			logger.info("-----------------------------------------------------------------------------------------");
			logger.info("Current index size: " + getNumbDocs() + " documents");
			initArtMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
			writer = new IndexWriter(artIndex, indexWriterConfig);
			int userIndexCount = 0;
			int indexCount = 0;
			for (IndexableModel record : userRecords.getRecords()) {
				addToIndex(writer, record);
				indexCount++;
			}
			userIndexCount++;
			logger.info("Indexed " + userIndexCount + " users and " + indexCount + " user records.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
				long timeEnd = new Date().getTime();
				long freeMemAfter = Runtime.getRuntime().freeMemory();
				logger.info("New indexed size " + getNumbDocs() + " documents");
				logger.info("Time to build index = " + (timeEnd - timeStart) / 1000);
				logger.info("Memory use (free memory) - before: " + freeMemBefore + " after: " + freeMemAfter);
				logger.info(
						"-----------------------------------------------------------------------------------------");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Async
	@Override
	public void indexUser(ZonefileModel zonefile) {
		buildIndex(zonefile);
	}

	@Override
	public void indexRecords(List<IndexableModel> indexData) {
		if (indexData == null || indexData.size() == 0) {
			return;
		}
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		int indexCount = 0;
		try {
			initArtMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
			writer = new IndexWriter(artIndex, indexWriterConfig);
			for (IndexableModel model : indexData) {
				addToIndex(writer, model);
				indexCount++;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			wrapUp(writer, indexCount, freeMemBefore, timeStart);
		}
	}

	@Override
	public void indexSingleRecord(IndexableModel indexData) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		int indexCount = 0;
		try {
			initArtMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
			writer = new IndexWriter(artIndex, indexWriterConfig);
			addToIndex(writer, indexData);
			indexCount++;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			wrapUp(writer, indexCount, freeMemBefore, timeStart);
		}
	}

	@Override
	public void remove(String field, String value) {
		clearUserDocuments(field, value);
	}

	private void clearUserDocuments(String field, String value) {
		IndexWriter writer = null;
		try {
			initArtMarket();
			QueryParser qp = new QueryParser(field, artAnalyzer);
			Query q = qp.parse(value);
			initArtMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
			writer = new IndexWriter(artIndex, indexWriterConfig);
			long size = writer.deleteDocuments(q);
			logger.info("Number of items removed from search index: " + size);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void buildIndex(ZonefileModel zonefile) {
		// List<DomainModel> domains = applicationSettings.getDomains();
		List<DomainIndexModel> projects = projectService.findAll();
		List<IndexableContainerModel> userRecords = new ArrayList<>();
		String[] appsVisited = zonefile.getDomainGaiaPairs();
		if (appsVisited == null) return;
		logger.info("Zonefile info: " + zonefile.getZonefile());
		for (String appVisited : appsVisited) {
			String[] appParts = appVisited.split("=");
			try {
				String appUrl = appParts[0];
				for (DomainIndexModel project : projects) {
					logger.info("---------------------------------");
					logger.info("matching: " + project.getDomain() + " against " + appUrl);
					if (appUrl.indexOf(project.getDomain()) > -1 || project.getDomain().indexOf(appUrl) > -1) {
						for (DomainIndexFileModel indexFileModel : project.getIndexFiles()) {
							IndexableContainerModel indexableContainerModel = fetchUserFile(
									appParts[1], 
									indexFileModel.getIndexFileName(),
									indexFileModel.getCategory(),
									project.getDomain());
							logger.info("domain getIndexFileName: " + indexFileModel.getIndexFileName());
							logger.info("domain category: " + indexFileModel.getCategory());
							if (indexableContainerModel != null && indexableContainerModel.getRecords() != null && indexableContainerModel.getRecords().size() > 0) {
								userRecords.add(indexableContainerModel);
								logger.info("domain indexing: " + indexableContainerModel.getRecords().size());
							}
						}
					}
					logger.info("---------------------------------");
				}
			} catch (Exception e) {
				// continue
			}
		}
		reindex(userRecords);
	}

	private IndexableContainerModel fetchUserFile(String gaiaIndexFileUrl, String fileName, String objType, String domain) {
		IndexableContainerModel model = null;
		String jsonFile = null;
		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("text/plain")));
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
			jsonFile = restTemplate1.exchange(gaiaIndexFileUrl + "/" + fileName, HttpMethod.GET, requestEntity, String.class).getBody();
			logger.info("Index fetching from: " + gaiaIndexFileUrl + "/" + fileName);
//			if (fileName.equals("auctions_v01.json")) {
//				logger.info(jsonFile);
//			}
			model = mapper.readValue(jsonFile, IndexableContainerModel.class);
			for (IndexableModel indexable : model.getRecords()) {
				indexable.setDomain(domain);
				indexable.setObjType(objType);
			}
		} catch (Exception e) {
			// skip - bad record or format
		}
		return model;
	}

	/**
	 * 1. Find the users who have visited the application. 2. Fetch their gaia url
	 * 3. Read the art market app specific data.
	 * @param indexData TODO
	 */
	private void reindex(List<IndexableContainerModel> userRecords) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		int userIndexCount = 0;
		int indexCount = 0;
		try {
			initArtMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
			writer = new IndexWriter(artIndex, indexWriterConfig);
			for (IndexableContainerModel userControlRecord : userRecords) { // index a portion of the namespace for test  purposes..
				indexCount = 0;
				for (IndexableModel record : userControlRecord.getRecords()) {
					addToIndex(writer, record);
					indexCount++;
				}
				userIndexCount++;
				logger.info("Indexed " + userIndexCount + " users and " + indexCount + " user records.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			wrapUp(writer, indexCount, freeMemBefore, timeStart);
		}
	}

	private void addToIndex(IndexWriter writer, IndexableModel record) throws IOException {
		Document document = null;
		try {
			document = new Document();
			document.add(new TextField("name", record.getName(), Field.Store.YES));
			document.add(new StringField("assetHash", record.getAssetHash(), Field.Store.YES));
			if (record.getProjectId()!= null) {
				document.add(new TextField("projectId", record.getProjectId(), Field.Store.YES));
			}
			if (record.getObjType() != null) {
				document.add(new TextField("objType", record.getObjType(), Field.Store.YES));
			} else {
				document.add(new TextField("objType", "artwork", Field.Store.YES));
			}
			if (record.getTokenId() != null) {
				document.add(new StoredField("tokenId", record.getTokenId()));
			}
			if (record.getUpdated() != null) {
				document.add(new NumericDocValuesField("updated", record.getUpdated()));
			}
			if (record.getPrivacy() != null) {
				document.add(new TextField("privacy", record.getPrivacy(), Field.Store.YES));
			}
			if (record.getCreated() != null) {
				document.add(new NumericDocValuesField("created", record.getCreated()));
			}
			if (record.getDomain() != null) {
				document.add(new TextField("domain", record.getDomain(), Field.Store.YES));
			}
			if (record.getOwner() != null) {
				document.add(new TextField("owner", record.getOwner(), Field.Store.YES));
			}
			if (record.getArtist() != null) {
				document.add(new TextField("artist", record.getArtist(), Field.Store.YES));
			}
			if (record.getDescription() != null) {
				document.add(new TextField("description", record.getDescription(), Field.Store.YES));
			}
			if (record.getStatus() != null) {
				document.add(new TextField("status", record.getStatus(), Field.Store.YES));
			}
			if (record.getPrivacy() != null) {
				document.add(new TextField("privacy", record.getPrivacy(), Field.Store.YES));
			}
			if (record.getAssetProjectUrl() != null) {
				document.add(new TextField("assetProjectUrl", record.getAssetProjectUrl(), Field.Store.YES));
			}
			if (record.getMetaDataUrl() != null) {
				document.add(new TextField("metaDataUrl", record.getMetaDataUrl(), Field.Store.YES));
			}
			if (record.getAssetUrl() != null) {
				document.add(new TextField("assetUrl", record.getAssetUrl(), Field.Store.YES));
			}
			if (record.getCategory() != null) {
				document.add(new TextField("category", record.getCategory().getId(), Field.Store.YES));
			}
			
			if (record.getNftMedia() != null) {
				NftMedia nfm = record.getNftMedia();
				document.add(new StoredField("coverArtist", nfm.getCoverArtist()));
				if (nfm.getArtworkClip() != null) {
					document.add(new StoredField("awcSize", nfm.getArtworkClip().getSize()));
					document.add(new StoredField("awcFileUrl", nfm.getArtworkClip().getFileUrl()));
					document.add(new StoredField("awcStorage", nfm.getArtworkClip().getStorage()));
					document.add(new StoredField("awcType", nfm.getArtworkClip().getType()));
				}
				if (nfm.getArtworkFile() != null) {
					document.add(new StoredField("awfSize", nfm.getArtworkFile().getSize()));
					document.add(new StoredField("awfFileUrl", nfm.getArtworkFile().getFileUrl()));
					document.add(new StoredField("awfStorage", nfm.getArtworkFile().getStorage()));
					document.add(new StoredField("awfType", nfm.getArtworkFile().getType()));
				}
				if (nfm.getCoverImage() != null) {
					document.add(new StoredField("aciSize", nfm.getCoverImage().getSize()));
					document.add(new StoredField("aciFileUrl", nfm.getCoverImage().getFileUrl()));
					document.add(new StoredField("aciStorage", nfm.getCoverImage().getStorage()));
					document.add(new StoredField("aciType", nfm.getCoverImage().getType()));
				}
			}
			
			if (record.getKeywords() != null) {
				String csKeywords = "1 ";
				for (KeywordModel km : record.getKeywords()) {
					csKeywords += km.getId() + " ";
				}
				csKeywords.substring(0, csKeywords.length() -2);
				document.add(new TextField("keywords", csKeywords, Field.Store.YES));
			} else {
				document.add(new TextField("keywords", "no keywords", Field.Store.YES));
			}
			if (record.getMetaData() != null) {
				for (String key : record.getMetaData().keySet()) {
					try {
						Object value = record.getMetaData().get(key);
						if (value instanceof Integer) {
							document.add(new IntPoint(key, (Integer)value));
						} else if (value instanceof Float) {
							document.add(new FloatPoint(key, (Integer)value));
						} else if (value instanceof Double) {
							document.add(new DoublePoint(key, (Integer)value));
						} else {
							document.add(new TextField(key, (String)value, Field.Store.YES));
						}
					} catch (Exception e) {
						logger.error("Field error - only coping with strings just now: " + e.getMessage());
					}
				}
			}
			Term term = new Term("assetHash", String.valueOf(record.getAssetHash()));
			writer.updateDocument(term, document);
			logger.info("Indexed dapp search record: nftIndex: " + document.get("nftIndex") + " object: " +record.getOwner() + " object: " + record.getObjType() + " record title: " + record.getName() + " from domain: " + record.getDomain());
		} catch (Exception e) {
			logger.error("Error idexing document from record: " + e.getMessage());
		}
		return;
	}
}
