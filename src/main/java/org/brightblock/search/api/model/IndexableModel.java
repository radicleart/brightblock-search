package org.brightblock.search.api.model;

import java.util.List;
import java.util.Map;

import org.brightblock.search.api.v2.NftMedia;
import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.annotation.JsonAlias;

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
	@JsonAlias({ "title" }) private String name;
	private String description;
	private Long created;
	private Long updated;
	private String owner;
	private String uploader;
	private String assetHash;
	private String imageUrl;
	private String assetUrl;
	private String assetProjectUrl;
	private String privacy;
	private String artist;
	private String objType;
	private String domain;
	private KeywordModel category;
	private List<KeywordModel> keywords;
	private String status;
	private Long tokenId;
	private NftMedia nftMedia;
	private Map<String, String> metaData;
	
	@Override
	public int compareTo(IndexableModel o) {
		if (o == null) return 0;
		return this.assetHash.compareTo(o.assetHash);
	}

	// @JsonCreator
	// public IndexableModel(@JsonAlias({"id", "auctionId"}) String id,
	// @JsonProperty("title") String title,
	// @JsonProperty("description") String description,
	// @JsonProperty("objType") String objType,
	// @JsonProperty("owner") String owner,
	// @JsonProperty("domain") String domain,
	// @JsonProperty("keywords") String keywords) {
	// super();
	// this.id = id;
	// this.title = title;
	// this.description = description;
	// this.owner = owner;
	// this.keywords = keywords;
	// this.objType = objType;
	// this.domain = domain;
	// }

//	public static class Deserializer extends StdDeserializer<IndexableModel> {
//		public Deserializer() {
//			this(null);
//		}
//
//		Deserializer(Class<?> vc) {
//			super(vc);
//		}
//
//		@Override
//		public IndexableModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//			JsonNode node = jp.getCodec().readTree(jp);
//			IndexableModel im = new IndexableModel();
//			ObjectMapper mapper = new ObjectMapper();
//			
//			// don't allow things without an asset hash - primary id of items.
//			im.created = node.get("created").asLong();
//			im.updated = node.get("updated").asLong();
//			
//			im.setAssetHash(node.get("assetHash").asText());
//			
//			if (node.hasNonNull("tokenId")) {
//				im.setTokenId(node.get("tokenId").asLong());
//			}
//			if (node.hasNonNull("nftIndex")) {
//				im.setNftIndex(node.get("nftIndex").asLong());
//			}
//			if (node.hasNonNull("privacy")) {
//				im.setPrivacy(node.get("privacy").asText());
//			}
//			if (node.hasNonNull("projectId")) {
//				im.setProjectId(node.get("projectId").asText());
//			}
//			if (node.hasNonNull("imageUrl")) {
//				im.setAssetUrl(node.get("imageUrl").asText());
//			} else if (node.hasNonNull("assetUrl")) {
//				im.setAssetUrl(node.get("assetUrl").asText());
//			}
//			
//			if (node.hasNonNull("assetProjectUrl")) {
//				im.setAssetProjectUrl(node.get("assetProjectUrl").asText());
//			} else if (node.hasNonNull("externalUrl")) {
//				im.setAssetProjectUrl(node.get("externalUrl").asText());
//			}
//			
//			if (node.hasNonNull("title")) {
//				im.setTitle(node.get("title").asText());
//			} else {
//				im.setTitle(node.get("name").asText());
//			}
//			if (node.hasNonNull("owner")) {
//				im.setOwner(node.get("owner").asText());
//			} else if (node.hasNonNull("auctioneer")) {
//				im.setOwner(node.get("auctioneer").asText());
//			} else if (node.has("administrator")) {
//				im.setOwner(node.get("administrator").asText());
//			} else {
//				throw new RuntimeException("unable to parse");
//			}
//			if (node.hasNonNull("description")) {
//				im.setDescription(node.get("description").asText());
//			} else {
//				im.setDescription(node.get("title").asText());
//			}
//			if (node.hasNonNull("mintedOn")) {
//				im.setArtist(node.get("mintedOn").asText());
//			}
//			if (node.hasNonNull("artist")) {
//				im.setArtist(node.get("artist").asText());
//			}
//			if (node.hasNonNull("objType")) {
//				im.setObjType(node.get("objType").asText());
//			}
//			if (node.hasNonNull("domain")) {
//				im.setDomain(node.get("domain").asText());
//			}
//			if (node.hasNonNull("buyer")) {
//				im.setBuyer(node.get("buyer").asText());
//			}
//			if (node.hasNonNull("status")) {
//				im.setStatus(node.get("status").asText());
//			}
//			if (node.hasNonNull("category")) {
//				try {
//					mapper = new ObjectMapper();
//					@SuppressWarnings("unchecked") LinkedHashMap<String, Object> cat = mapper.convertValue(node.get("category"), LinkedHashMap.class);
//					Object lev = cat.get("level");
//					Integer level = 0;
//					if (lev != null) level = (Integer)lev;
//					KeywordModel km = new KeywordModel((String)cat.get("id"), (String)cat.get("name"), level, (String)cat.get("parent"));
//					im.setCategory(km);
//				} catch (Exception e) {
//					im.setCategory(KeywordModel.defKeywordModel());
//				}
//			} else {
//				im.setCategory(KeywordModel.defKeywordModel());
//			}
//			if (node.hasNonNull("keywords")) {
//				mapper = new ObjectMapper();
//				try {
//					@SuppressWarnings("unchecked") List<LinkedHashMap<String, Object>> keywords = mapper.convertValue(node.get("keywords"), List.class);
//					List<KeywordModel> kms = new ArrayList<>();
//					KeywordModel km = null;
//					for (LinkedHashMap<String, Object> keyword : keywords) {
//						Object lev = keyword.get("level");
//						Integer level = (Integer)lev;
//						km = new KeywordModel((String)keyword.get("id"), (String)keyword.get("name"), level, (String)keyword.get("parent"));
//						kms.add(km);
//					}
//					im.setKeywords(kms);
//				} catch (Exception e) {
//					@SuppressWarnings("unchecked") List<String> keywords = mapper.convertValue(node.get("keywords"), List.class);
//					List<KeywordModel> kms = new ArrayList<>();
//					KeywordModel km = null;
//					for (String keyword : keywords) {
//						km = new KeywordModel("1", keyword, 1, null);
//						kms.add(km);
//					}
//					im.setKeywords(kms);
//				}
//			}
//			if (node.hasNonNull("metaData")) {
//				mapper = new ObjectMapper();
//				@SuppressWarnings("unchecked") Map<String, String> result = mapper.convertValue(node.get("metaData"), Map.class);
//				im.setMetaData(result);
//			}
//			return im;
//		}
//	}
}
