package org.brightblock.search.service.index;

import java.io.IOException;
import java.util.ArrayList;
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
import org.brightblock.search.service.blockstack.models.ZonefileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NamesIndexServiceImpl extends BaseIndexingServiceImpl implements NamesIndexService {

	@Autowired private DappsIndexService dappIndexService;

	@Override
    public int clearAll() {
		IndexWriter writer = null;
		try {
			logger.info("-----------------------------------------------------------------------------------------");
			logger.info("Clear index - current size: " + getNumbDocs() + " documents");
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(namesAnalyzer);
			indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			writer = new IndexWriter(namesIndex, indexWriterConfig);
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
			initNames();
			IndexReader indexReader = DirectoryReader.open(namesIndex);
			return indexReader.numDocs();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
		
    @Async
	@Override
	public void indexPages(int from, int to) {
		List<ZonefileModel> zonefiles = new ArrayList<>();
		List<String> names = null;
		for  (int page = from; page <= to; page++) { // index a portion of the namespace for test purposes..
			names = blockstackService.names(page);
			if (names == null || names.size() == 0) {
				break;
			}
			zonefiles.addAll(indexUsersInternal(names));
			indexDappData(zonefiles);
		}
	}

    @Async
	@Override
	public void indexUsers(List<String> names) {
    	List<ZonefileModel> zonefiles = indexUsersInternal(names);
    		indexDappData(zonefiles);
	}

    private void indexDappData(List<ZonefileModel> zonefiles) {
		for  (ZonefileModel zonefile : zonefiles) { // index a portion of the namespace for test purposes..
			dappIndexService.indexUser(zonefile);
		}
    }
    	
    
	private List<ZonefileModel> indexUsersInternal(List<String> names) {
		IndexWriter writer = null;
		List<ZonefileModel> zonefiles = new ArrayList<>();
		long freeMemBefore = Runtime.getRuntime().freeMemory();
		long timeStart = new Date().getTime();
		int indexCount = 0;
		try {
			initNames();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(namesAnalyzer);
			writer = new IndexWriter(namesIndex, indexWriterConfig);
			for (String name : names) {
				zonefiles.add(addToIndex(writer, name));
				indexCount++;
			}
			return zonefiles; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			wrapUp(writer, indexCount, freeMemBefore, timeStart);
		}
	}

	private ZonefileModel addToIndex(IndexWriter writer, String name) throws IOException {
		ZonefileModel zonefile = null;
		try {
			zonefile = blockstackService.getZonefile(name, true);
			zonefile.setName(name);
			Term term = new Term("name", name);
			writer.updateDocument(term, getDocument(name, zonefile));
			logger.info("--------------------------------------------\nIndexed zonefile data for user: " + name + " \nprofile url " + zonefile.getProfileUrl() + "\n--------------------------------------------");
			return zonefile;
		} catch (Exception e) {
			if (zonefile != null) {
				logger.debug("Error indexing blockstack name: " + name + " records contains null values. Record returned: " + zonefile.toString());
			} else {
				logger.debug("Error indexing blockstack name: " + name + ". No record returned");
			}
			logger.error("Error message: " + e.getMessage());
			return null;
		}
	}
	
	private Document getDocument(String name, ZonefileModel zonefile) {
		Document document = new Document();
		document.add(new StringField("name", name, Field.Store.YES));
		if (zonefile.getAddress() != null)  {
			document.add(new StringField("address", zonefile.getAddress(), Field.Store.YES));
		}
		if (zonefile.getBlockchain() != null) {
			document.add(new StringField("blockchain", zonefile.getBlockchain(), Field.Store.YES));
		}
		if (zonefile.getExpireBlock() != null) {
			document.add(new StringField("expireBlock", zonefile.getExpireBlock(), Field.Store.YES));
		}
		if (zonefile.getLastTxid() != null) {
			document.add(new StringField("lastTxid", zonefile.getLastTxid(), Field.Store.YES));
		}
		if (zonefile.getStatus() != null) {
			document.add(new StringField("status", zonefile.getStatus(), Field.Store.YES));
		}
		if (zonefile.getZonefileHash() != null) {
			document.add(new StringField("zonefileHash", zonefile.getZonefileHash(), Field.Store.YES));
		}
		if (zonefile.getZonefile() != null) {
			document.add(new TextField("zonefile", zonefile.getZonefile(), Field.Store.YES));
		}
		if (zonefile.getProfileUrl() != null) {
			document.add(new TextField("profileUrl", zonefile.getProfileUrl(), Field.Store.YES));
		}
		if (zonefile.getDescription() != null) {
			document.add(new TextField("description", zonefile.getDescription(), Field.Store.YES));
		}
		if (zonefile.getApps() != null) {
			document.add(new TextField("apps", zonefile.getApps(), Field.Store.YES));
		}
		return document;
	}
}
