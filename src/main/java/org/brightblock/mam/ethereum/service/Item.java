package org.brightblock.mam.ethereum.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;
import org.web3j.tuples.generated.Tuple6;

public class Item implements Serializable, Comparable<Item> {
	private static final long serialVersionUID = -907471625497750800L;
	private Long itemIndex;
	private String title;
	private String blockstackId;
	private String timestamp;
	private BigInteger ownerIndex; // index into array of owners
	private BigInteger price;
	private Boolean inAuction;

	public Item() {
		super();
	}

	public Item(Long itemIndex, Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean> tuple) throws NoSuchAlgorithmException {
		super();
		this.itemIndex = itemIndex;
		this.title = tuple.getValue1();
		this.blockstackId = tuple.getValue2();
		timestamp = new String(Hex.encode(tuple.getValue3()));
		this.timestamp = EthereumService._0X + timestamp;
		this.ownerIndex = tuple.getValue4();
		this.price = tuple.getValue5();
		this.inAuction = tuple.getValue6();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBlockstackId() {
		return blockstackId;
	}

	public void setBlockstackId(String blockstackId) {
		this.blockstackId = blockstackId;
	}

	public BigInteger getOwnerIndex() {
		return ownerIndex;
	}

	public void setOwnerIndex(BigInteger ownerIndex) {
		this.ownerIndex = ownerIndex;
	}

	public BigInteger getPrice() {
		return price;
	}

	public void setPrice(BigInteger price) {
		this.price = price;
	}

	public Boolean getInAuction() {
		return inAuction;
	}

	public void setInAuction(Boolean inAuction) {
		this.inAuction = inAuction;
	}

	public Long getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(Long itemIndex) {
		this.itemIndex = itemIndex;
	}
	
	public String toString() {
		return "itemIndex: " + itemIndex + "title: " + title +
				"blockstackId: " + blockstackId +
				"timestamp: " + timestamp +
				"ownerIndex: " + ownerIndex.toString() +
				"price: " + price.toString() +
				"inAuction: " + inAuction;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(Item item) {
		return this.itemIndex.compareTo(item.itemIndex);
	}
}
