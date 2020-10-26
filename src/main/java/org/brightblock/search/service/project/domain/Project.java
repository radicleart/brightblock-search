package org.brightblock.search.service.project.domain;

import java.util.List;
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
@TypeAlias(value = "Project")
public class Project {

	@Id private String uuid;
	private String projectId;
	private String owner;
	private String domain;
	private String storeageModel;
	private List<IndexFileModel> indexFiles;

	public Project() {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.storeageModel = "gaia";
	}
}
