package org.brightblock.search.service.project.domain;

import java.io.Serializable;

public class IndexFileModel implements Serializable {

	private static final long serialVersionUID = 7644522607123444433L;
	private String indexFileName;
	private String indexObjType;

	public String getIndexFileName() {
		return indexFileName;
	}

	public void setIndexFileName(String indexFileName) {
		this.indexFileName = indexFileName;
	}

	public String getIndexObjType() {
		return indexObjType;
	}

	public void setIndexObjType(String indexObjType) {
		this.indexObjType = indexObjType;
	}

}
