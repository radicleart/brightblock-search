package org.brightblock.mam.ethereum.service;

import java.io.IOException;
import java.math.BigInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.conf.settings.EthereumSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

@Service
public class EthereumServiceImpl implements EthereumService {

	private static final Logger logger = LogManager.getLogger(EthereumServiceImpl.class);
	@Autowired private EthereumSettings ethereumSettings;
	@Autowired private Web3j web3;
	@Autowired private Credentials credentials;
	private String contractAddress;
	private ArtMarket contract;

	@Override
	public String getWeb3ClientVersion() throws IOException {
		Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
		return web3ClientVersion.getWeb3ClientVersion();
	}

    @Async
	@Override
	public ArtMarket loadContract(Long gasLimit, Long gas) {
		BigInteger bgGas = BigInteger.valueOf(gas);
		BigInteger bgGasLimit = BigInteger.valueOf(gasLimit);
		contract = ArtMarket.load(ethereumSettings.getContractAddress(), web3, credentials, bgGas, bgGasLimit);
		logger.info("Deployed Contract: contract=" + contract.getContractAddress());
		logger.info("Loaded Contract: gas=" + gas + " gasLimit=" + gasLimit);
		return contract;
	}

    @Async
	@Override
	public void deployContract(Long gasLimit, Long gas) throws Exception {
		BigInteger bgGas = BigInteger.valueOf(gas);
		BigInteger bgGasLimit = BigInteger.valueOf(gasLimit);
		ArtMarket contract = ArtMarket.deploy(web3, credentials, bgGas, bgGasLimit).sendAsync().get();
//		CompletableFuture<Void> future = completableFuture.thenAccept(s -> logger.info("Deployed Contract: s=" + s));
//		future.get();
		contractAddress = contract.getContractAddress();
		logger.info("Deployed Contract: contract=" + contract.getContractAddress());
		logger.info("Deployed Contract: contract=" + contract.getDeployedAddress("4"));
		return;
	}

	@Override
	public BigInteger numbItems() {
		try {
			BigInteger result;
			RemoteCall<BigInteger> numbItems = contract.itemIndex();
			result = numbItems.send();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
