package org.brightblock.mam.lightning.web;

import java.io.Serializable;

public class UserInvoiceModel implements Serializable {

	private static final long serialVersionUID = -6076158540399552737L;

	private Long amount;
	private String descriptionHash;
	private String memo;

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getDescriptionHash() {
		return descriptionHash;
	}

	public void setDescriptionHash(String descriptionHash) {
		this.descriptionHash = descriptionHash;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
