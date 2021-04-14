package org.brightblock.search.api.model;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@TypeAlias(value = "OwnersModel")
public class OwnersModel {

	@Id private String uuid;
	private Set<String> owners;

	public OwnersModel() {
		super();
		this.uuid = UUID.randomUUID().toString();
	}
	
//	public static class Deserializer extends StdDeserializer<OwnersModel> {
//
//		public Deserializer() {
//			this(null);
//		}
//
//		Deserializer(Class<?> vc) {
//			super(vc);
//		}
//
//		@Override
//		public OwnersModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//			JsonNode node = jp.getCodec().readTree(jp);
//			OwnersModel im = new OwnersModel();
//			return im;
//		}
//	}
}
