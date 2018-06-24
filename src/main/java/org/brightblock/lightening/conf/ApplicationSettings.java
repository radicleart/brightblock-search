package org.brightblock.lightening.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class ApplicationSettings {
	private String confluenceBase;
	private String blockstackBase;
	private String bitcoinBase;
	private String bitcoinRpcPasssword;
	private String bitcoinRpcUser;

	public String getBitcoinRpcUser() {
		return bitcoinRpcUser;
	}

	public void setBitcoinRpcUser(String bitcoinRpcUser) {
		this.bitcoinRpcUser = bitcoinRpcUser;
	}

	public String getBitcoinRpcPasssword() {
		return bitcoinRpcPasssword;
	}

	public void setBitcoinRpcPasssword(String bitcoinRpcPasssword) {
		this.bitcoinRpcPasssword = bitcoinRpcPasssword;
	}

	public String getConfluenceBase() {
		return confluenceBase;
	}

	public void setConfluenceBase(String confluenceBase) {
		this.confluenceBase = confluenceBase;
	}

	public String getBitcoinBase() {
		return bitcoinBase;
	}

	public void setBitcoinBase(String bitcoinBase) {
		this.bitcoinBase = bitcoinBase;
	}

	public String getBlockstackBase() {
		return blockstackBase;
	}

	public void setBlockstackBase(String blockstackBase) {
		this.blockstackBase = blockstackBase;
	}

}
