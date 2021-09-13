package org.brightblock.search.api.model;

import java.util.List;
import java.util.Map;

import org.brightblock.search.api.v2.Attributes;
import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TypeAlias(value = "RootFile")
//@JsonDeserialize(using = IndexableModel.Deserializer.class)
public class IndexableModel implements SearchResultModel, Comparable<IndexableModel> {

	private static final long serialVersionUID = 7471051478505916999L;
	private String projectId;
	private String name;
	private String description;
	private Long created;
	private Long updated;
	private String owner;
	private String uploader;
	private String assetHash;
	private String image;
	private String externalUrl;
	private String metaDataUrl;
	private String currentRunKey;
	private String privacy;
	private String artist;
	private String objType;
	private String domain;
	private String contractAssetJson;
	private KeywordModel category;
	private List<KeywordModel> keywords;
	private String status;
	private Attributes attributes;
	private Map<String, String> metaData;
	
	@Override
	public int compareTo(IndexableModel o) {
		if (o == null) return 0;
		return this.assetHash.compareTo(o.assetHash);
	}
}
