package org.brightblock.search.api.v2;

import java.util.List;

import org.brightblock.search.api.model.IndexableModel;
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
public class RootFile {

	private Long created;
	private Long updated;
	private ArtistProfile artistProfile;
	private List<IndexableModel> records;
}
