package org.brightblock.search.conf.settings;

import java.io.Serializable;
import java.util.List;

public class DomainModel implements Serializable {

	private static final long serialVersionUID = 3112854578667781497L;
	private String gaiaIndexFileUrl;
	private String domain;
	private List<IndexFileModel> indexFiles;
	private String[] fields;

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGaiaIndexFileUrl() {
		return gaiaIndexFileUrl;
	}

	public void setGaiaIndexFileUrl(String gaiaIndexFileUrl) {
		this.gaiaIndexFileUrl = gaiaIndexFileUrl;
	}

	public List<IndexFileModel> getIndexFiles() {
		return indexFiles;
	}

	public void setIndexFiles(List<IndexFileModel> indexFiles) {
		this.indexFiles = indexFiles;
	}

}
