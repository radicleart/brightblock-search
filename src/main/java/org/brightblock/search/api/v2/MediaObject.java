package org.brightblock.search.api.v2;

import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TypeAlias(value = "MediaObject")
public class MediaObject {

	private String dataHash;
	private String dataUrl;
	private String fileUrl;
	private String id;
	private String size;
	private String storage;
	private String type;
}
