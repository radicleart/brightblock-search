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
@TypeAlias(value = "NftMedia")
public class NftMedia {

	private String coverArtist;
	private MediaObject artworkFile;
	private MediaObject artworkClip;
	private MediaObject coverImage;
}
