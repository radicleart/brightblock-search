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
	private String fiatCurrency = "USD";
	private Float initialRateBtc = 0F;
	private Float initialRateEth = 0F;
	private Float amountInEther = 0F;
	private Long auctionId;

	public SaleDataModel() {
		super();
	}

	@JsonCreator
	public SaleDataModel(@JsonProperty("soid") Integer soid, 
			@JsonProperty("amount") Float amount, 
			@JsonProperty("fiatCurrency") String fiatCurrency, 
			@JsonProperty("initialRateBtc") Float initialRateBtc, 
			@JsonProperty("initialRateEth") Float initialRateEth, 
			@JsonProperty("amountInEther") Float amountInEther, 
			@JsonProperty("reserve") Float reserve, 
			@JsonProperty("auctionId") Long auctionId, 
			@JsonProperty("increment") Float increment) {
		super();
		this.soid = soid;
		this.amount = amount;
		this.reserve = reserve;
		this.fiatCurrency = fiatCurrency;
		this.initialRateBtc = initialRateBtc;
		this.initialRateEth = initialRateEth;
		this.amountInEther = amountInEther;
		this.increment = increment;
		this.auctionId = auctionId;
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

	public String getFiatCurrency() {
		return fiatCurrency;
	}

	public void setFiatCurrency(String fiatCurrency) {
		this.fiatCurrency = fiatCurrency;
	}

	public Float getInitialRateBtc() {
		return initialRateBtc;
	}

	public void setInitialRateBtc(Float initialRateBtc) {
		this.initialRateBtc = initialRateBtc;
	}

	public Float getInitialRateEth() {
		return initialRateEth;
	}

	public void setInitialRateEth(Float initialRateEth) {
		this.initialRateEth = initialRateEth;
	}

	public Float getAmountInEther() {
		return amountInEther;
	}

	public void setAmountInEther(Float amountInEther) {
		this.amountInEther = amountInEther;
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}


}
