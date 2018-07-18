package org.brightblock.mam.services.index.names;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.brightblock.mam.conf.ApplicationSettings;
import org.brightblock.mam.services.blockstack.BlockstackApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseIndexingServiceImpl {
    protected static final Logger logger = LogManager.getLogger(BaseIndexingServiceImpl.class);
	@Autowired ApplicationSettings applicationSettings;
	@Autowired protected BlockstackApiService blockstackService;
	protected Directory namesIndex;
    protected StandardAnalyzer analyzer;

    protected void init() throws IOException {
		if (namesIndex == null) {
			logger.info("Initialising index from directory at: " + applicationSettings.getBlockstackNamesIndex());
			namesIndex = FSDirectory.open(Paths.get(applicationSettings.getBlockstackNamesIndex()));
		}
		if (analyzer == null) {
			logger.info("Initialising analyser.");
			analyzer = new StandardAnalyzer();
		}
	}
}
