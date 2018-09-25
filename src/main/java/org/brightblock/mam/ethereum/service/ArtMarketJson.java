package org.brightblock.mam.ethereum.service;

import java.io.Serializable;

public class ArtMarketJson implements Serializable {
	
	private static final long serialVersionUID = 712753852202648607L;
	private String client;
	private String network;
	private String contractAddress;
	private Boolean valid;
	private String abi;
	private Long numbItems;

	public ArtMarketJson() {
		super();
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getAbi() {
		return abi;
	}

	public void setAbi(String abi) {
		this.abi = abi;
	}

	public Long getNumbItems() {
		return numbItems;
	}

	public void setNumbItems(Long numbItems) {
		this.numbItems = numbItems;
	}
}
