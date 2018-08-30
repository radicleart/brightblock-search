package org.brightblock.mam.services.index.posts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SaleDataModel implements Serializable {

	private static final long serialVersionUID = 4546515127490479631L;
	private Integer soid = 0;
	private Float amount = 0F;
	private Float reserve = 0F;
	private Float increment = 0F;

	public SaleDataModel() {
		super();
	}

	@JsonCreator
	public SaleDataModel(@JsonProperty("soid") Integer soid, 
			@JsonProperty("amount") Float amount, 
			@JsonProperty("reserve") Float reserve, 
			@JsonProperty("increment") Float increment) {
		super();
		this.soid = soid;
		this.amount = amount;
		this.reserve = reserve;
		this.increment = increment;
	}

	public Integer getSoid() {
		return soid;
	}

	public void setSoid(Integer soid) {
		this.soid = soid;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getReserve() {
		return reserve;
	}

	public void setReserve(Float reserve) {
		this.reserve = reserve;
	}

	public Float getIncrement() {
		return increment;
	}

	public void setIncrement(Float increment) {
		this.increment = increment;
	}


}
