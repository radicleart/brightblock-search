package org.brightblock.mam.conf;

import java.io.IOException;

import org.brightblock.mam.conf.settings.EthereumSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthereumConfiguration {
	
	@Autowired private EthereumSettings ethereumSettings;

	@Bean
    public Web3j getWeb3() {
		Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
        return web3;
    }
	
	@Bean
    public Credentials getCredentials() throws IOException, CipherException {
		Credentials credentials = WalletUtils.loadCredentials(
				ethereumSettings.getWalletPassword(),
				ethereumSettings.getWalletPath());
        return credentials;
    }
}
