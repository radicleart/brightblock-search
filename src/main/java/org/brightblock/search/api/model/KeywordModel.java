package org.brightblock.search.api.model;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@JsonDeserialize(using = KeywordModel.Deserializer.class)
public class KeywordModel implements Serializable, Comparable<KeywordModel> {

	private static final long serialVersionUID = 4923555414784492143L;
	private String id;
	private String name;
	private Integer level;
	private String parent;

	public KeywordModel() {
		super();
	}

	public KeywordModel(String id, String name, Integer level, String parent) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.parent = parent;
	}

	public static KeywordModel defKeywordModel() {
		return new KeywordModel("1", "artwork", 0, null);
	}

	public KeywordModel(String id) {
		super();
		this.id = id;
	}

	public static class Deserializer extends StdDeserializer<KeywordModel> {
		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public KeywordModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			KeywordModel im = new KeywordModel();
			if (node.has("id")) {
				im.setId(node.get("id").asText());
			}
			im.setName(node.get("name").asText());
			im.setParent(node.get("parent").asText());
			if (node.has("level")) {
				im.setLevel(node.get("artist").asInt(3));
			}
			return im;
		}
	}

	@Override
	public int compareTo(KeywordModel model) {
		return this.name.compareTo(model.getName());
	}

	@Override
	public boolean equals(Object model) {
		KeywordModel record = (KeywordModel) model;
		return this.name.equals(record.getName());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

}
