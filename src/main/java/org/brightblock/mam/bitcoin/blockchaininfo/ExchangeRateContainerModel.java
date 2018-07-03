package org.brightblock.mam.bitcoin.blockchaininfo;

import java.util.Map;

import org.brightblock.mam.rest.models.ApiModel;


public class ExchangeRateContainerModel extends ApiModel {

	private static final long serialVersionUID = 1L;
	private String code;
	private Map<String, ExchangeRateModel> rates;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ExchangeRateModel getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(ExchangeRateModel exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	private ExchangeRateModel exchangeRate;

	public ExchangeRateContainerModel() {
		super();
	}

	public Map<String, ExchangeRateModel> getRates() {
		return rates;
	}

	public void setRates(Map<String, ExchangeRateModel> rates) {
		this.rates = rates;
	}

}
