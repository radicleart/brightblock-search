package org.brightblock.search.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexableContainerModel implements Serializable {

	private static final long serialVersionUID = -4406659402008572723L;
	private List<IndexableModel> records = new ArrayList<>();

	@JsonCreator
	public IndexableContainerModel(@JsonProperty("records") List<IndexableModel> records) {
		super();
		this.records = records;
	}

	public List<IndexableModel> getRecords() {
		return records;
	}

	public void setRecords(List<IndexableModel> records) {
		this.records = records;
	}

}
