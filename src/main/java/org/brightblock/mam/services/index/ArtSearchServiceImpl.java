package org.brightblock.mam.services.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.brightblock.mam.services.index.posts.OwnershipRecordModel;
import org.brightblock.mam.services.index.posts.SaleDataModel;
import org.springframework.stereotype.Service;

@Service
public class ArtSearchServiceImpl extends BaseIndexingServiceImpl implements ArtSearchService {

	@Override
	public Set<OwnershipRecordModel> fetchAll() {
		try {
			initArtMarket();
			Set<OwnershipRecordModel> models = new HashSet<>();
			IndexReader reader = DirectoryReader.open(artIndex);
			Document document = null;
			for (int i=0; i < reader.maxDoc(); i++) {
				try {
					document = reader.document(i);
					OwnershipRecordModel model = convertToRecord(document);
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
	public List<OwnershipRecordModel> searchIndex(String inField, String query) {
		try {
			initArtMarket();
			QueryParser qp = new QueryParser(inField, artAnalyzer);
 			if (inField.equals("description") || inField.equals("title")) {
 	 			qp.setAllowLeadingWildcard(true);
 			}
 			Query q = qp.parse(query);
			IndexReader indexReader = DirectoryReader.open(artIndex);
			IndexSearcher searcher = new IndexSearcher(indexReader);
			TopDocs topDocs = searcher.search(q, 20);
			List<OwnershipRecordModel> models = new ArrayList<OwnershipRecordModel>();
			Document document = null;
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				document = searcher.doc(scoreDoc.doc);
				OwnershipRecordModel model = convertToRecord(document);
				models.add(model);
			}
			return models;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private OwnershipRecordModel convertToRecord(Document document) {
		OwnershipRecordModel model = new OwnershipRecordModel();
		model.setAppUrl(document.get("appUrl"));
		model.setDescription(document.get("description"));
		model.setGaiaUrl(document.get("gaiaUrl"));
		model.setId(Long.valueOf(document.get("id")));
		model.setItemType(document.get("itemType"));
		model.setOwner(document.get("owner"));
		model.setKeywords(document.get("keywords"));
		model.setRegistered(Boolean.valueOf(document.get("registered")));
		model.setSold(Boolean.valueOf(document.get("sold")));
		model.setTitle(document.get("title"));
		model.setUploader(document.get("uploader"));
		SaleDataModel saleData = new SaleDataModel();
		try {
			saleData.setSoid(Integer.valueOf(document.get("soid")));
			saleData.setAmount(Float.valueOf(document.get("amount")));
			saleData.setReserve(Float.valueOf(document.get("reserve")));
			saleData.setIncrement(Float.valueOf(document.get("increment")));
			saleData.setInitialRateBtc(Float.valueOf(document.get("initialRateBtc")));
			saleData.setInitialRateEth(Float.valueOf(document.get("initialRateEth")));
			saleData.setAmountInEther(Float.valueOf(document.get("amountInEther")));
			saleData.setIncrement(Float.valueOf(document.get("increment")));
			saleData.setFiatCurrency(String.valueOf(document.get("fiatCurrency")));
		} catch (Exception e) {
			// no sale data;
		}
		model.setSaleData(saleData);
		return model;
	}
}