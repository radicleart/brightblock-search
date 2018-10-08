package org.brightblock.mam.conf.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ethereum")
public class EthereumSettings {
	private String contractAddress;
	private String walletPassword;
	private String walletPath;
	private String httpBase;
	private String wssBase;
	private boolean ganache;
	private String ganachePassword;
	private String ganacheSeed;

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getWalletPassword() {
		return walletPassword;
	}

	public void setWalletPassword(String walletPassword) {
		this.walletPassword = walletPassword;
	}

	public String getWalletPath() {
		return walletPath;
	}

	public void setWalletPath(String walletPath) {
		this.walletPath = walletPath;
	}

	public String getHttpBase() {
		return httpBase;
	}

	public void setHttpBase(String httpBase) {
		this.httpBase = httpBase;
	}

	public String getWssBase() {
		return wssBase;
	}

	public void setWssBase(String wssBase) {
		this.wssBase = wssBase;
	}

	public boolean isGanache() {
		return ganache;
	}

	public void setGanache(boolean ganache) {
		this.ganache = ganache;
	}

	public String getGanacheSeed() {
		return ganacheSeed;
	}

	public void setGanacheSeed(String ganacheSeed) {
		this.ganacheSeed = ganacheSeed;
	}

	public String getGanachePassword() {
		return ganachePassword;
	}

	public void setGanachePassword(String ganachePassword) {
		this.ganachePassword = ganachePassword;
	}
}
