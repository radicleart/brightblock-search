package org.brightblock.search.service.index.posts;

import java.util.List;

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
@TypeAlias(value = "CacheQuery")
public class CacheQuery {

	private String queryType;
	private String contractId;
	private List<String> hashes;

}
