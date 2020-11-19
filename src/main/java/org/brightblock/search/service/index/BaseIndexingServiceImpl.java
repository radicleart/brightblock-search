package org.brightblock.search.service.index;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.brightblock.search.api.model.IndexableModel;
import org.brightblock.search.conf.ApplicationSettings;
import org.brightblock.search.service.blockstack.BlockstackApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseIndexingServiceImpl {
    protected static final Logger logger = LogManager.getLogger(BaseIndexingServiceImpl.class);
	@Autowired ApplicationSettings applicationSettings;
	@Autowired protected BlockstackApiService blockstackService;
	protected Directory namesIndex;
    protected StandardAnalyzer namesAnalyzer;
	protected Directory artIndex;
    protected StandardAnalyzer artAnalyzer;
	protected Directory auctionIndex;
    protected StandardAnalyzer auctionAnalyzer;

    protected void wrapUp(IndexWriter writer, int indexCount, long freeMemBefore, long timeStart) {
		try {
			if (writer != null) writer.close();
			long timeEnd = new Date().getTime();
			long freeMemAfter = Runtime.getRuntime().freeMemory();
			logger.info("-----------------------------------------------------------------------------------------");
			logger.info("(Re) Indexed " + indexCount + " documents");
			logger.info("Time to build index = " + (timeEnd - timeStart) / 1000);
			logger.info("Memory use (free memory) - before: " + freeMemBefore + " after: " + freeMemAfter);
			logger.info("-----------------------------------------------------------------------------------------");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

    protected void initNames() throws IOException {
		if (namesIndex == null) {
			logger.info("Initialising names index from directory at: " + applicationSettings.getBlockstackNamesIndex());
			namesIndex = FSDirectory.open(Paths.get(applicationSettings.getBlockstackNamesIndex()));
		}
		if (namesAnalyzer == null) {
			logger.info("Initialising names analyser.");
			namesAnalyzer = new StandardAnalyzer();
		}
	}

    protected void initArtMarket() throws IOException {
		if (artIndex == null) {
			logger.info("Initialising art index from directory at: " + applicationSettings.getArtMarketIndex());
			artIndex = FSDirectory.open(Paths.get(applicationSettings.getArtMarketIndex()));
		}
		if (artAnalyzer == null) {
			logger.info("Initialising art analyser.");
			artAnalyzer = new StandardAnalyzer();
		}
	}

    protected void initAuctionMarket() throws IOException {
		if (auctionIndex == null) {
			logger.info("Initialising auction index from directory at: " + applicationSettings.getAuctionIndex());
			auctionIndex = FSDirectory.open(Paths.get(applicationSettings.getAuctionIndex()));
		}
		if (auctionAnalyzer == null) {
			logger.info("Initialising auction analyser.");
			auctionAnalyzer = new StandardAnalyzer();
		}
	}

}
