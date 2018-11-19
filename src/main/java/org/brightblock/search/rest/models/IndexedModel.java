package org.brightblock.search.rest.models;

public class IndexedModel implements IApiModel {

	private static final long serialVersionUID = -3720637995364883296L;
	private int docIndex;
	private float score;
	
	
	public IndexedModel() {
		super();
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
