package org.brightblock.search.service.project.domain;

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
@TypeAlias(value = "IndexFileModel")
public class IndexFileModel {

	private String indexFileName;
	private String indexObjType;

}
