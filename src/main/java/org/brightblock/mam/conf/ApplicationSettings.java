package org.brightblock.mam.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class ApplicationSettings {
	private String confluenceBase;
	private String blockstackNamesIndex;
	private String artMarketIndex;
	private String blockstackBase;
	private String bitcoinBase;
	private String lightningCertFileName;
	private String lightningBase;
	private int lightningPortAlice;
	private int lightningPortBob;
	private String bitcoinRpcPasssword;
	private String bitcoinRpcUser;
	private String bitcoinTestRpcPasssword;
	private String bitcoinTestRpcUser;
	private String domainString;

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

	public String getBitcoinTestRpcUser() {
		return bitcoinTestRpcUser;
	}

	public void setBitcoinTestRpcUser(String bitcoinTestRpcUser) {
		this.bitcoinTestRpcUser = bitcoinTestRpcUser;
	}

	public String getBitcoinTestRpcPasssword() {
		return bitcoinTestRpcPasssword;
	}

	public void setBitcoinTestRpcPasssword(String bitcoinTestRpcPasssword) {
		this.bitcoinTestRpcPasssword = bitcoinTestRpcPasssword;
	}

	public String getLightningBase() {
		return lightningBase;
	}

	public void setLightningBase(String lightningBase) {
		this.lightningBase = lightningBase;
	}

	public String getLightningCertFileName() {
		return lightningCertFileName;
	}

	public void setLightningCertFileName(String lightningCertFileName) {
		this.lightningCertFileName = lightningCertFileName;
	}

	public int getLightningPortAlice() {
		return lightningPortAlice;
	}

	public void setLightningPortAlice(int lightningPortAlice) {
		this.lightningPortAlice = lightningPortAlice;
	}

	public int getLightningPortBob() {
		return lightningPortBob;
	}

	public void setLightningPortBob(int lightningPortBob) {
		this.lightningPortBob = lightningPortBob;
	}

	public String getBlockstackNamesIndex() {
		return blockstackNamesIndex;
	}

	public void setBlockstackNamesIndex(String blockstackNamesIndex) {
		this.blockstackNamesIndex = blockstackNamesIndex;
	}

	public String getArtMarketIndex() {
		return artMarketIndex;
	}

	public void setArtMarketIndex(String artMarketIndex) {
		this.artMarketIndex = artMarketIndex;
	}

	public String getDomainString() {
		return domainString;
	}

	public void setDomainString(String domainString) {
		this.domainString = domainString;
	}
}
