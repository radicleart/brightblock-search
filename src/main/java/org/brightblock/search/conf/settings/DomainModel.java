package org.brightblock.search.conf.settings;

import java.io.Serializable;
import java.util.List;

import org.brightblock.search.api.model.DomainIndexFileModel;

public class DomainModel implements Serializable {

	private static final long serialVersionUID = 3112854578667781497L;
	private String domain;
	private List<DomainIndexFileModel> indexFiles;
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

	public List<DomainIndexFileModel> getIndexFiles() {
		return indexFiles;
	}

	public void setIndexFiles(List<DomainIndexFileModel> indexFiles) {
		this.indexFiles = indexFiles;
	}

}
