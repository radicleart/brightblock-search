package org.brightblock.mam.services.index.posts;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.brightblock.mam.conf.ApplicationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsIndexServiceImpl implements PostsIndexService {
	Directory namesIndex;
	StandardAnalyzer analyzer;
	@Autowired ApplicationSettings applicationSettings;

	@Override
	public void buildIndex() throws IOException {
		namesIndex = FSDirectory.open(Paths.get(applicationSettings.getBlockstackNamesIndex()));
		analyzer = new StandardAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter writter = new IndexWriter(namesIndex, indexWriterConfig);
		
		Document document = new Document();
		document.add(new StringField("handle", String.valueOf(new Date().getTime()), Field.Store.YES));
		document.add(new TextField("title", "once upon a time", Field.Store.YES));
		document.add(new TextField("body", "and the continuing community story weaves it's way closer to the boundary..", Field.Store.YES));
		writter.addDocument(document);
		
		document = new Document();
		document.add(new StringField("handle", String.valueOf(new Date().getTime()), Field.Store.YES));
		document.add(new TextField("title", "in a land far away", Field.Store.YES));
		document.add(new TextField("body", "a hunter gatherer community makes camp for the night..", Field.Store.YES));
		writter.addDocument(document);
		writter.close();
	}

	public List<PostModel> searchIndex(String inField, String queryString) throws ParseException, IOException {
		Query query = new QueryParser(inField, analyzer).parse(queryString);
		IndexReader indexReader = DirectoryReader.open(namesIndex);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TopDocs topDocs = searcher.search(query, 10);
		List<PostModel> models = new ArrayList<PostModel>();
		Document document = null;
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			document = searcher.doc(scoreDoc.doc);
			PostModel model = new PostModel();
			model.setDocIndex(scoreDoc.doc);
			model.setScore(scoreDoc.score);
			model.setTitle(document.get("title"));
			model.setBody(document.get("body"));
			model.setHandle(document.get("handle"));
			models.add(model);
		}
		return models;
	}

}
