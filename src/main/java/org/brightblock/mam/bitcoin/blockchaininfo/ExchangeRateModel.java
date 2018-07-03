package org.brightblock.mam.bitcoin.blockchaininfo;

import org.brightblock.mam.rest.models.ApiModel;

public class ExchangeRateModel extends ApiModel {

	private static final long serialVersionUID = 1L;

	private float fifteenMinDelay;
	private float last;
	private float buy;
	private float sell;
	private float symbol;

	public ExchangeRateModel() {
		super();
	}

	public float getFifteenMinDelay() {
		return fifteenMinDelay;
	}

	public void setFifteenMinDelay(float fifteenMinDelay) {
		this.fifteenMinDelay = fifteenMinDelay;
	}

	public float getLast() {
		return last;
	}

	public void setLast(float last) {
		this.last = last;
	}

	public float getBuy() {
		return buy;
	}

	public void setBuy(float buy) {
		this.buy = buy;
	}

	public float getSell() {
		return sell;
	}

	public void setSell(float sell) {
		this.sell = sell;
	}

	public float getSymbol() {
		return symbol;
	}

	public void setSymbol(float symbol) {
		this.symbol = symbol;
	}

}
