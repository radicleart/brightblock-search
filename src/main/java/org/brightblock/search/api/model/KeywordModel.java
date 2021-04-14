package org.brightblock.search.api.model;

import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias(value = "KeywordModel")
//@JsonDeserialize(using = KeywordModel.Deserializer.class)
public class KeywordModel implements Comparable<KeywordModel> {

	private String id;
	private String name;
	private Integer level;
	private String parent;

//	public static KeywordModel defKeywordModel() {
//		return new KeywordModel("1", "artwork", 0, null);
//	}
//
//	public KeywordModel(String id) {
//		super();
//		this.id = id;
//	}
//
//	public static class Deserializer extends StdDeserializer<KeywordModel> {
//		public Deserializer() {
//			this(null);
//		}
//
//		Deserializer(Class<?> vc) {
//			super(vc);
//		}
//
//		@Override
//		public KeywordModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//			JsonNode node = jp.getCodec().readTree(jp);
//			KeywordModel im = new KeywordModel();
//			if (node.has("id")) {
//				im.setId(node.get("id").asText());
//			}
//			im.setName(node.get("name").asText());
//			im.setParent(node.get("parent").asText());
//			if (node.has("level")) {
//				im.setLevel(node.get("artist").asInt(3));
//			}
//			return im;
//		}
//	}

	@Override
	public int compareTo(KeywordModel model) {
		return this.name.compareTo(model.getName());
	}

	@Override
	public boolean equals(Object model) {
		KeywordModel record = (KeywordModel) model;
		return this.name.equals(record.getName());
	}
}
