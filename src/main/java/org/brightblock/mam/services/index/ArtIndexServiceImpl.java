package org.brightblock.mam.services.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.brightblock.mam.services.blockstack.models.ZonefileModel;
import org.brightblock.mam.services.index.posts.OwnershipRecordModel;
import org.brightblock.mam.services.index.posts.OwnershipRecordsModel;
import org.brightblock.mam.services.index.posts.SaleDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ArtIndexServiceImpl extends BaseIndexingServiceImpl implements ArtIndexService {

	private static final String RECORDS_V01_JSON = "/records_v01.json";
	private static final Logger logger = LogManager.getLogger(ArtIndexServiceImpl.class);
	@Autowired
	private NamesSearchService namesSearchService;
	@Autowired
	private RestOperations restTemplate1;
	@Autowired
	private ObjectMapper mapper;

	@Override
	public int clear() {
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
	public void indexSingleRecord(OwnershipRecordModel indexData) {
		clearUserDocuments("uploader", indexData.getUploader());
		List<ZonefileModel> zonefiles = namesSearchService.searchIndex("name", indexData.getUploader());
		buildIndex(zonefiles);
		// no gaia url on below path which leads to a bug fetching the provenance record in the ui.
//		IndexWriter writer = null;
//		long freeMemBefore = Runtime.getRuntime().freeMemory();
//		long timeStart = new Date().getTime();
//		try {
//			logger.info("-----------------------------------------------------------------------------------------");
//			logger.info("Current index size: " + getNumbDocs() + " documents");
//			initArtMarket();
//			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
//			writer = new IndexWriter(artIndex, indexWriterConfig);
//			addToIndex(writer, indexData);
//			logger.info("Indexed " + indexData.toString());
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} finally {
//			try {
//				writer.close();
//				long timeEnd = new Date().getTime();
//				long freeMemAfter = Runtime.getRuntime().freeMemory();
//				logger.info("New indexed size " + getNumbDocs() + " documents");
//				logger.info("Time to build index = " + (timeEnd - timeStart) / 1000);
//				logger.info("Memory use (free memory) - before: " + freeMemBefore + " after: " + freeMemAfter);
//				logger.info(
//						"-----------------------------------------------------------------------------------------");
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//		}
	}

	@Override
	public void addToIndex(OwnershipRecordsModel userRecords) {
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
			for (OwnershipRecordModel record : userRecords.getRecords()) { 
				record.setAppUrl(userRecords.getAppUrl());
				record.setGaiaUrl(userRecords.getGaiaUrl());
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
	public void indexUser(String username) {
		clearUserDocuments("uploader", username);
		List<ZonefileModel> zonefiles = namesSearchService.searchIndex("name", username);
		buildIndex(zonefiles);
	}

	@Async
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

	@Async
	@Override
	public void buildIndex() {
		String domainString = applicationSettings.getDomainString();
		logger.info("Building index for domains: " + domainString);
		List<ZonefileModel> zonefiles = null;
		String[] domains = domainString.split(",");
		for (String domain : domains) {
			logger.info("Building index for domains: " + domainString);
			zonefiles = namesSearchService.searchIndex("apps", "*" + domain + "*");
			zonefiles.addAll(zonefiles);
		}
		buildIndex(zonefiles);
	}

	private void buildIndex(List<ZonefileModel> zonefiles) {
		String domainString = applicationSettings.getDomainString();
		String[] appHttpLines = null;
		String[] appParts = null;
		List<OwnershipRecordsModel> userRecords = new ArrayList<>();
		String url = null;
		for (ZonefileModel zonefile : zonefiles) {
			String apps = zonefile.getApps();
			appHttpLines = apps.split("/\\shttp");
			for (String appLine : appHttpLines) {
				appParts = appLine.split("=");
				try {
					String appUrl = appParts[0];
					logger.info("Building index for domains: " + domainString + " and appUrl: " + appUrl);
					String[] domains = domainString.split(",");
					for (String domain : domains) {
						if (appUrl.indexOf(domain) > -1 || domain.indexOf(appUrl) > -1) {
							url = appParts[1].trim() + RECORDS_V01_JSON;
							OwnershipRecordsModel ownershipRecordsModel = fetchArtworkInfo(url);
							if (ownershipRecordsModel != null && ownershipRecordsModel.getRecords().size() > 0) {
								ownershipRecordsModel.setAppUrl(appParts[0].trim());
								ownershipRecordsModel.setGaiaUrl(url);
								userRecords.add(ownershipRecordsModel);
							}
						}
					}
				} catch (HttpClientErrorException e) {
					logger.error("Error reading from: " + url + " Error thrown: " + e.getMessage());
				} catch (Exception e1) {
					logger.error("Error deserialising from: " + url + " Error thrown: " + e1.getMessage());
				}
			}
		}
		reindex(userRecords);
	}

	private OwnershipRecordsModel fetchArtworkInfo(String url)
			throws JsonParseException, JsonMappingException, IOException {
		OwnershipRecordsModel model = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("text/plain")));
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
		String jsonFile = null;
		try {
			jsonFile = restTemplate1.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
			model = mapper.readValue(jsonFile, OwnershipRecordsModel.class);
		} catch (Exception e) {
			logger.error("Error reading from: " + url + " Error thrown: " + e.getMessage());
			logger.error(jsonFile);
		}
		return model;
	}

	/**
	 * 1. Find the users who have visited the application. 2. Fetch their gaia url
	 * 3. Read the art market app specific data.
	 */
	private void reindex(List<OwnershipRecordsModel> userRecords) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		try {
			initArtMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(artAnalyzer);
			writer = new IndexWriter(artIndex, indexWriterConfig);
			int userIndexCount = 0;
			for (OwnershipRecordsModel userControlRecord : userRecords) { // index a portion of the namespace for test
																			// purposes..
				int indexCount = 0;
				for (OwnershipRecordModel record : userControlRecord.getRecords()) { // index a portion of the namespace
																						// for test purposes..
					record.setAppUrl(userControlRecord.getAppUrl());
					record.setGaiaUrl(userControlRecord.getGaiaUrl());
					addToIndex(writer, record);
					indexCount++;
				}
				userIndexCount++;
				logger.info("Indexed " + userIndexCount + " users and " + indexCount + " user records.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
				long timeEnd = new Date().getTime();
				long freeMemAfter = Runtime.getRuntime().freeMemory();
				logger.info(
						"-----------------------------------------------------------------------------------------");
				logger.info("(Re) Indexed " + getNumbDocs() + " documents");
				logger.info("Time to build index = " + (timeEnd - timeStart) / 1000);
				logger.info("Memory use (free memory) - before: " + freeMemBefore + " after: " + freeMemAfter);
				logger.info(
						"-----------------------------------------------------------------------------------------");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void addToIndex(IndexWriter writer, OwnershipRecordModel record) throws IOException {
		Document document = null;
		try {
			document = new Document();
			document.add(new TextField("title", record.getTitle(), Field.Store.YES));
			document.add(new StringField("id", String.valueOf(record.getId()), Field.Store.YES));
			if (record.getEditions() != null) {
				document.add(new TextField("editions", String.valueOf(record.getEditions()), Field.Store.YES));
			} else {
				document.add(new TextField("editions", "1", Field.Store.YES));
			}
			if (record.getItemType() != null) {
				document.add(new TextField("itemType", record.getItemType(), Field.Store.YES));
			} else {
				document.add(new TextField("itemType", "digiart", Field.Store.YES));
			}
			if (record.getArtist() != null) {
				document.add(new TextField("artist", record.getArtist(), Field.Store.YES));
			} else {
				document.add(new TextField("artist", record.getUploader(), Field.Store.YES));
			}
			if (record.getOwner() != null) {
				document.add(new TextField("owner", record.getOwner(), Field.Store.YES));
			} else {
				document.add(new TextField("owner", record.getUploader(), Field.Store.YES));
			}
			if (record.getUploader() != null) {
				document.add(new TextField("uploader", record.getUploader(), Field.Store.YES));
			} else {
				logger.error("Skipping " + record.getId() + "; No uploader field.");
				return;
			}
			if (record.getAppUrl() != null) {
				document.add(new TextField("appUrl", record.getAppUrl(), Field.Store.YES));
			} else {
				document.add(new TextField("appUrl", "appUrl not given", Field.Store.YES));
			}
			if (record.getGaiaUrl() != null) {
				document.add(new TextField("gaiaUrl", record.getGaiaUrl(), Field.Store.YES));
			} else {
				document.add(new TextField("gaiaUrl", "gaiaUrl not given", Field.Store.YES));
			}
			if (record.getDescription() != null) {
				document.add(new TextField("description", record.getDescription(), Field.Store.YES));
			} else {
				logger.error("Skipping " + record.getId() + "; No description field.");
				return;
			}
			if (record.getKeywords() != null) {
				document.add(new TextField("keywords", record.getKeywords(), Field.Store.YES));
			} else {
				document.add(new TextField("keywords", "keywords not given", Field.Store.YES));
			}
			if (record.getSaleData() == null) {
				record.setSaleData(new SaleDataModel());
			}
			document.add(new TextField("soid", String.valueOf(record.getSaleData().getSoid()), Field.Store.YES));
			document.add(new TextField("amount", String.valueOf(record.getSaleData().getAmount()), Field.Store.YES));
			document.add(new TextField("reserve", String.valueOf(record.getSaleData().getReserve()), Field.Store.YES));
			document.add(
					new TextField("increment", String.valueOf(record.getSaleData().getIncrement()), Field.Store.YES));
			document.add(new TextField("fiatCurrency", String.valueOf(record.getSaleData().getFiatCurrency()),
					Field.Store.YES));
			document.add(new TextField("initialRateBtc", String.valueOf(record.getSaleData().getInitialRateBtc()),
					Field.Store.YES));
			document.add(new TextField("initialRateEth", String.valueOf(record.getSaleData().getInitialRateEth()),
					Field.Store.YES));
			document.add(new TextField("amountInEther", String.valueOf(record.getSaleData().getAmountInEther()),
					Field.Store.YES));
			Term term = new Term("id", String.valueOf(record.getId()));
			writer.updateDocument(term, document);
		} catch (Exception e) {
			logger.error("Error message: " + e.getMessage());
		}
		return;
	}
}
