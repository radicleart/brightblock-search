package org.brightblock.search.service;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexableModel implements Serializable, Comparable<IndexableModel> {

	private static final long serialVersionUID = 7471051478505916999L;
	private Long id;
	private String title;
	private String description;
	private String owner;
	private String objType;
	private String domain;
	private String keywords;

	public IndexableModel() {
		super();
	}
	
	@JsonCreator
	public IndexableModel(@JsonProperty("id") String id, 
			@JsonProperty("title") String title, 
			@JsonProperty("description") String description, 
			@JsonProperty("objType") String objType, 
			@JsonProperty("owner") String owner, 
			@JsonProperty("domain") String domain, 
			@JsonProperty("keywords") String keywords) {
		super();
		this.id = Long.valueOf(id);
		this.title = title;
		this.description = description;
		this.owner = owner;
		this.keywords = keywords;
		this.objType = objType;
		this.domain = domain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public int compareTo(IndexableModel model) {
		return this.id.compareTo(model.getId());
	}

	@Override
	public boolean equals(Object model) {
		IndexableModel record = (IndexableModel) model;
		return this.id.equals(record.getId());
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
