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
import org.brightblock.search.service.index.posts.AuctionModel;
import org.springframework.stereotype.Service;

@Service
public class AuctionSearchServiceImpl extends BaseIndexingServiceImpl implements AuctionSearchService {

	@Override
	public List<AuctionModel> fetchAll() {
		try {
			initAuctionMarket();
			List<AuctionModel> models = new ArrayList<AuctionModel>();
			IndexReader reader = DirectoryReader.open(auctionIndex);
			Document document = null;
			for (int i = 0; i < reader.maxDoc(); i++) {
				try {
					document = reader.document(i);
					AuctionModel model = convertToRecord(document);
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
	public List<AuctionModel> searchIndex(int limit, String inField, String query) {
		try {
			initAuctionMarket();
			QueryParser qp = new QueryParser(inField, auctionAnalyzer);
			if (inField.equals("description") || inField.equals("title")) {
				qp.setAllowLeadingWildcard(true);
			}
			Query q = qp.parse(query);
			IndexReader indexReader = DirectoryReader.open(auctionIndex);
			IndexSearcher searcher = new IndexSearcher(indexReader);
			TopDocs topDocs = searcher.search(q, limit);
			List<AuctionModel> models = new ArrayList<AuctionModel>();
			Document document = null;
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				document = searcher.doc(scoreDoc.doc);
				AuctionModel model = convertToRecord(document);
				models.add(model);
			}
			return models;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private AuctionModel convertToRecord(Document document) {
		AuctionModel model = new AuctionModel();
		model.setAppUrl(document.get("appUrl"));
		model.setDescription(document.get("description"));
		model.setGaiaUrl(document.get("gaiaUrl"));
		model.setAuctionId(Long.valueOf(document.get("auctionId")));
		model.setAuctionType(document.get("auctionType"));
		model.setAdministrator(document.get("administrator"));
		model.setStartDate(Long.valueOf(document.get("startDate")));
		model.setEndDate(Long.valueOf(document.get("endDate")));
		model.setAuctioneer(document.get("auctioneer"));
		model.setKeywords(document.get("keywords"));
		model.setTitle(document.get("title"));
		model.setPrivacy(document.get("privacy"));
		return model;
	}
}
