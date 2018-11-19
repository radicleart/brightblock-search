package org.brightblock.search.rest.models;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class ResponseCodesTypeEnumSerializer extends JsonSerializer<ResponseCodes> {

	@Override
	public void serialize(ResponseCodes value, JsonGenerator generator, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		generator.writeStartObject();
		generator.writeFieldName("status");
		generator.writeNumber(value.getStatus());
		generator.writeFieldName("description");
		generator.writeString(value.getDescription());
		generator.writeFieldName("name");
		generator.writeString(value.name());
		generator.writeEndObject();
	}
}
