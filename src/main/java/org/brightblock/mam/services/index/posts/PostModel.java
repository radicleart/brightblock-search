package org.brightblock.mam.services.index.posts;

import org.brightblock.mam.rest.models.ApiModel;

public class PostModel extends ApiModel {

	private static final long serialVersionUID = 1L;

	private int docIndex;
	private float score;
	private String handle;
	private String created;
	private String title;
	private String body;

	public PostModel() {
		super();
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getDocIndex() {
		return docIndex;
	}

	public void setDocIndex(int docIndex) {
		this.docIndex = docIndex;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}


}
