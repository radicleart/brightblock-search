package org.brightblock.mam.services.index;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.brightblock.mam.services.blockstack.models.ProfileModel;
import org.brightblock.mam.services.blockstack.models.ZonefileModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NamesIndexServiceImpl extends BaseIndexingServiceImpl implements NamesIndexService {

    @Override
    public int getNumbDocs() {
		try {
			initNames();
			IndexReader indexReader = DirectoryReader.open(namesIndex);
			return indexReader.numDocs();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
	

    @Async
	@Override
	public void buildIndex(int from, int to) {
		reindex(from, to);
	}

    @Async
	@Override
	public void buildIndex(List<String> names) {
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		try {
			initNames();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(namesAnalyzer);
			writer = new IndexWriter(namesIndex, indexWriterConfig);
			for (String name : names) {
				int numberAdded = addToIndex(writer, name);
				if (numberAdded > 0) logger.info("Re-Indexed name: " + name);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
				long timeEnd = new Date().getTime();
				long freeMemAfter = Runtime.getRuntime().freeMemory();
				logger.info("-----------------------------------------------------------------------------------------");
				logger.info("(Re) Indexed " + getNumbDocs() + " documents");
				logger.info("Time to build index = " + (timeEnd - timeStart) / 1000);
				logger.info("Memory use (free memory) - before: " + freeMemBefore + " after: " + freeMemAfter);
				logger.info("-----------------------------------------------------------------------------------------");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void reindex(int from, int to) {
		
		IndexWriter writer = null;
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		try {
			initNames();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(namesAnalyzer);
			writer = new IndexWriter(namesIndex, indexWriterConfig);
			//int page = 0;
			List<String> names = null;
			for  (int page=from; page<to; page++) { // index a portion of the namespace for test purposes..
				names = blockstackService.names(page);
				if (names == null || names.size() == 0) {
					break;
				}
				int indexCount = 0;
				for (String name : names) {
					int numberAdded = addToIndex(writer, name);
					indexCount += numberAdded;
				}
				logger.info("Indexed " + indexCount + " items on page: " + page);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
				long timeEnd = new Date().getTime();
				long freeMemAfter = Runtime.getRuntime().freeMemory();
				logger.info("-----------------------------------------------------------------------------------------");
				logger.info("(Re) Indexed " + getNumbDocs() + " documents");
				logger.info("Time to build index = " + (timeEnd - timeStart) / 1000);
				logger.info("Memory use (free memory) - before: " + freeMemBefore + " after: " + freeMemAfter);
				logger.info("-----------------------------------------------------------------------------------------");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private int addToIndex(IndexWriter writer, String name) throws IOException {
		Document document = null;
		ZonefileModel bsName = null;
		int indexCount = 0;
		try {
			bsName = blockstackService.name(name);
			parseZonefile(bsName);
			document = new Document();
			document.add(new StringField("name", name, Field.Store.YES));
			if (bsName.getAddress() != null)  	document.add(new StringField("address", bsName.getAddress(), Field.Store.YES));
			if (bsName.getBlockchain() != null) 	document.add(new StringField("blockchain", bsName.getBlockchain(), Field.Store.YES));
			if (bsName.getExpireBlock() != null) document.add(new StringField("expireBlock", bsName.getExpireBlock(), Field.Store.YES));
			if (bsName.getLastTxid() != null) 	document.add(new StringField("lastTxid", bsName.getLastTxid(), Field.Store.YES));
			if (bsName.getStatus() != null) 		document.add(new StringField("status", bsName.getStatus(), Field.Store.YES));
			if (bsName.getZonefileHash() != null)document.add(new StringField("zonefileHash", bsName.getZonefileHash(), Field.Store.YES));
			if (bsName.getZonefile() != null) 	document.add(new TextField("zonefile", bsName.getZonefile(), Field.Store.YES));
			if (bsName.getProfileUrl() != null) 	document.add(new TextField("profileUrl", bsName.getProfileUrl(), Field.Store.YES));
			ProfileModel profile = blockstackService.profile(bsName.getProfileUrl());
			if (profile != null) {
				if (profile.getDescription() != null) 	document.add(new TextField("description", profile.getDescription(), Field.Store.YES));
				if (profile.getApps() != null) 			document.add(new TextField("apps", profile.getApps(), Field.Store.YES));
			}
			Term term = new Term("name", name);
			writer.updateDocument(term, document);
			indexCount++;
		} catch (Exception e) {
			if (bsName != null) {
				logger.error("Error indexing blockstack name: " + name + " records contains null values. Record returned: " + bsName.toString());
			} else {
				logger.error("Error indexing blockstack name: " + name + ". No record returned");
			}
			logger.error("Error message: " + e.getMessage());
		}
		return indexCount;
	}
	
	private void parseZonefile(ZonefileModel bsName) {
		String zonefile = bsName.getZonefile();
		int index1 = zonefile.indexOf("https://");
		int index2 = zonefile.indexOf("\\", index1);
		if (index2 == -1) {
			index2 = zonefile.indexOf("\"", index1);
		}
		bsName.setProfileUrl(zonefile.substring(index1, index2));
	}
}
