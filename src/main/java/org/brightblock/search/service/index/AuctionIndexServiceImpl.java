package org.brightblock.search.service.index;

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
import org.brightblock.search.service.blockstack.models.ZonefileModel;
import org.brightblock.search.service.index.posts.AuctionModel;
import org.brightblock.search.service.index.posts.AuctionsModel;
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
public class AuctionIndexServiceImpl extends BaseIndexingServiceImpl implements AuctionIndexService {

	private static final Logger logger = LogManager.getLogger(AuctionIndexServiceImpl.class);
	private static final String RECORDS_V01_JSON = "/auctions_v01.json";
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
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(auctionAnalyzer);
			indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			writer = new IndexWriter(auctionIndex, indexWriterConfig);
			writer.deleteAll();
			int numbDocs = getNumbDocs();
			logger.info("Cleared index");
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
			initAuctionMarket();
			IndexReader indexReader = DirectoryReader.open(auctionIndex);
			return indexReader.numDocs();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addToIndex(AuctionsModel userRecords) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		try {
			logger.info("-----------------------------------------------------------------------------------------");
			logger.info("Current index size: " + getNumbDocs() + " documents");
			initAuctionMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(auctionAnalyzer);
			writer = new IndexWriter(auctionIndex, indexWriterConfig);
			int userIndexCount = 0;
			int indexCount = 0;
			for (AuctionModel auction : userRecords.getAuctions()) {
				if (auction.getPrivacy() != null && !auction.getPrivacy().equals("private")) {
					auction.setAppUrl(userRecords.getAppUrl());
					auction.setGaiaUrl(userRecords.getGaiaUrl());
					addToIndex(writer, auction);
					indexCount++;
				} else {
					logger.info("Skipped Indexed " + auction.getTitle() + " because privacy set to: " + auction.getPrivacy());
				}
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

	@Override
	public void reindexOne(AuctionModel auction) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		try {
			logger.info("-----------------------------------------------------------------------------------------");
			logger.info("Current index size: " + getNumbDocs() + " documents");
			initAuctionMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(auctionAnalyzer);
			writer = new IndexWriter(auctionIndex, indexWriterConfig);
			int userIndexCount = 0;
			int indexCount = 0;
			if (auction.getPrivacy() != null && !auction.getPrivacy().equals("private")) {
				addToIndex(writer, auction);
				indexCount++;
			} else {
				logger.info("Skipped Indexed " + auction.getTitle() + " because privacy set to: " + auction.getPrivacy());
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
		buildIndex(zonefiles, null);
	}

	@Async
	@Override
	public void remove(String field, String value) {
		clearUserDocuments(field, value);
	}

	private void clearUserDocuments(String field, String value) {
		IndexWriter writer = null;
		try {
			initAuctionMarket();
			QueryParser qp = new QueryParser(field, auctionAnalyzer);
			Query q = qp.parse(value);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(auctionAnalyzer);
			writer = new IndexWriter(auctionIndex, indexWriterConfig);
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
		String domainString = applicationSettings.getDomains().toString();
		logger.info("Building index for domains: " + domainString);
		List<ZonefileModel> zonefiles = null;
		String[] domains = domainString.split(",");
		for (String domain : domains) {
			logger.info("Building index for domains: " + domainString);
			zonefiles = namesSearchService.searchIndex("apps", "*" + domain + "*");
			zonefiles.addAll(zonefiles);
		}
		buildIndex(zonefiles, null);
	}

	private void buildIndex(List<ZonefileModel> zonefiles, AuctionModel auctionModel) {
		String domainString = applicationSettings.getDomains().toString();
		String[] appHttpLines = null;
		String[] appParts = null;
		List<AuctionsModel> auctions = new ArrayList<>();
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
							AuctionsModel auctionsModel = fetchBlockstackInfo(url);
							if (auctionsModel != null && auctionsModel.getAuctions().size() > 0) {
								auctionsModel.setAppUrl(appParts[0].trim());
								auctionsModel.setGaiaUrl(url);
								auctions.add(auctionsModel);
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
		reindex(auctions, auctionModel);
	}

	private AuctionsModel fetchBlockstackInfo(String url)
			throws JsonParseException, JsonMappingException, IOException {
		AuctionsModel model = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType("text/plain")));
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
		String jsonFile = null;
		try {
			jsonFile = restTemplate1.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
			model = mapper.readValue(jsonFile, AuctionsModel.class);
		} catch (Exception e) {
			logger.error("Error reading from: " + url + " Error thrown: " + e.getMessage());
			logger.error(jsonFile);
		}
		return model;
	}

	/**
	 * 1. Find the users who have visited the application. 2. Fetch their gaia url
	 * 3. Read the art market app specific data.
	 * @param indexData TODO
	 */
	private void reindex(List<AuctionsModel> userRecords, AuctionModel indexData) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		try {
			initAuctionMarket();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(auctionAnalyzer);
			writer = new IndexWriter(auctionIndex, indexWriterConfig);
			int userIndexCount = 0;
			for (AuctionsModel userControlRecord : userRecords) { // index a portion of the namespace for test  purposes..
				int indexCount = 0;
				for (AuctionModel auction : userControlRecord.getAuctions()) {
					if (auction.getPrivacy() != null && !auction.getPrivacy().equals("private")) {
						auction.setAppUrl(userControlRecord.getAppUrl());
						auction.setGaiaUrl(userControlRecord.getGaiaUrl());
						if (indexData != null && auction.getAuctionId().longValue() == indexData.getAuctionId().longValue()) {
							addToIndex(writer, auction);
							indexCount++;
						} else {
							addToIndex(writer, auction);
							indexCount++;
						}
					}
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

	private void addToIndex(IndexWriter writer, AuctionModel record) throws IOException {
		Document document = null;
		try {
			document = new Document();
			document.add(new TextField("title", record.getTitle(), Field.Store.YES));
			document.add(new StringField("auctionId", String.valueOf(record.getAuctionId()), Field.Store.YES));
			if (record.getAuctionType() != null) {
				document.add(new TextField("auctionType", record.getAuctionType(), Field.Store.YES));
			} else {
				document.add(new TextField("auctionType", "webcast", Field.Store.YES));
			}
			if (record.getAdministrator() != null) {
				document.add(new TextField("administrator", record.getAdministrator(), Field.Store.YES));
			} else {
				logger.error("Skipping " + record.getAuctionId() + "; No administrator field.");
				return;
			}
			if (record.getStartDate() != null) {
				document.add(new TextField("startDate", String.valueOf(record.getStartDate()), Field.Store.YES));
			} else {
				document.add(new TextField("startDate", String.valueOf(new Date().getTime()), Field.Store.YES));
			}
			if (record.getEndDate() != null) {
				document.add(new TextField("endDate", String.valueOf(record.getEndDate()), Field.Store.YES));
			} else {
				document.add(new TextField("endDate", String.valueOf(new Date().getTime()), Field.Store.YES));
			}
			if (record.getAuctioneer() != null) {
				document.add(new TextField("auctioneer", record.getAuctioneer(), Field.Store.YES));
			} else {
				document.add(new TextField("auctioneer", record.getAdministrator(), Field.Store.YES));
			}
			if (record.getPrivacy() != null) {
				document.add(new TextField("privacy", record.getPrivacy(), Field.Store.YES));
			} else {
				document.add(new TextField("privacy", "private", Field.Store.YES));
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
				logger.error("Skipping " + record.getAuctionId() + "; No description field.");
				return;
			}
			if (record.getKeywords() != null) {
				document.add(new TextField("keywords", record.getKeywords(), Field.Store.YES));
			} else {
				document.add(new TextField("keywords", "keywords not given", Field.Store.YES));
			}
			Term term = new Term("auctionId", String.valueOf(record.getAuctionId()));
			writer.updateDocument(term, document);
		} catch (Exception e) {
			logger.error("Error message: " + e.getMessage());
		}
		return;
	}
}
