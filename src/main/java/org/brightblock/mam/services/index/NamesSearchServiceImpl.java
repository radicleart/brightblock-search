package org.brightblock.mam.services.index;

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
import org.brightblock.mam.services.blockstack.models.ZonefileModel;
import org.springframework.stereotype.Service;

@Service
public class NamesSearchServiceImpl extends BaseIndexingServiceImpl implements NamesSearchService {

	public List<ZonefileModel> searchIndex(String inField, String query) {
		try {
			initNames();
			QueryParser qp = new QueryParser(inField, namesAnalyzer);
 			if (inField.equals("description") || inField.equals("apps")) {
 	 			qp.setAllowLeadingWildcard(true);
 			}
 			Query q = qp.parse(query);
			IndexReader indexReader = DirectoryReader.open(namesIndex);
			IndexSearcher searcher = new IndexSearcher(indexReader);
			TopDocs topDocs = searcher.search(q, 10);
			List<ZonefileModel> models = new ArrayList<ZonefileModel>();
			Document document = null;
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				document = searcher.doc(scoreDoc.doc);
				ZonefileModel model = new ZonefileModel();
				model.setDocIndex(scoreDoc.doc);
				model.setScore(scoreDoc.score);
				model.setAddress(document.get("address"));
				model.setApps(document.get("apps"));
				model.setBlockchain(document.get("blockchain"));
				model.setExpireBlock(document.get("expireBlock"));
				model.setLastTxid(document.get("lastTxid"));
				model.setName(document.get("name"));
				model.setDescription(document.get("description"));
				model.setStatus(document.get("status"));
				model.setZonefile(document.get("zonefile"));
				model.setZonefileHash(document.get("zonefileHash"));
				models.add(model);
			}
			return models;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
