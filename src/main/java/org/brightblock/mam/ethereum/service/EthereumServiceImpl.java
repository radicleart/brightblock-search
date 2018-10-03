package org.brightblock.mam.ethereum.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.conf.settings.EthereumSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tuples.generated.Tuple6;

import rx.Subscription;

@Service
public class EthereumServiceImpl implements EthereumService {

	private static final Logger logger = LogManager.getLogger(EthereumServiceImpl.class);
	@Autowired private EthereumSettings ethereumSettings;
	@Autowired private Web3j web3;
	@Autowired private Credentials credentials;
	public static String contractAddress;
	private ArtMarket contract;
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	private Subscription blockSubscription;

	@Override
	public void subscribeBlocks() {
		blockSubscription = web3.blockObservable(false).subscribe(block -> {
		    simpMessagingTemplate.convertAndSend("/topic/exchanges", block.getJsonrpc());
		});
	}
	
	@Override
	public void unSubscribeBlocks() {
		blockSubscription.unsubscribe();
	}
	
	@Override
	public ArtMarketJson getContractInfo() throws IOException {
		ArtMarketJson amj = new ArtMarketJson();
		Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
		amj.setClient(web3ClientVersion.getWeb3ClientVersion());
		if (contract == null || !contract.isValid()) {
			loadContract(EthereumService.remixGasLimit, EthereumService.remixGas);
		}
		logger.info("Loading client version data from contract: " + contractAddress);
		logger.info("Loading client version data from contract: " + contract.getContractAddress());
		amj.setContractAddress(contract.getContractAddress());
		amj.setValid(contract.isValid());
		amj.setNetwork("rinkeby - need to run geth with admin module!");
		amj.setNumbItems(numbItems().longValue());
		return amj;
	}

	@Override
	public ArtMarket loadContract(Long gasLimit, Long gas) {
		try {
			BigInteger bgGas = BigInteger.valueOf(gas);
			BigInteger bgGasLimit = BigInteger.valueOf(gasLimit);
			if (contractAddress == null) {
				contractAddress = ethereumSettings.getContractAddress();
			}
			if (!contractAddress.startsWith(_0X)) {
				contractAddress = _0X + contractAddress;
			}
			logger.info("Loading contract from: " + contractAddress);
			contract = ArtMarket.load(contractAddress, web3, credentials, bgGas, bgGasLimit);
			logger.info("Loaded Contract: gas=" + gas + " gasLimit=" + gasLimit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contract;
	}

	@Override
	public ArtMarket loadContract(String contractAddress, Long gasLimit, Long gas) {
		try {
			BigInteger bgGas = BigInteger.valueOf(gas);
			BigInteger bgGasLimit = BigInteger.valueOf(gasLimit);
			if (contractAddress == null) {
				contractAddress = ethereumSettings.getContractAddress();
			} else {
				if (!contractAddress.startsWith(_0X)) {
					contractAddress = _0X + contractAddress;
				}
				EthereumServiceImpl.contractAddress = contractAddress;
				ethereumSettings.setContractAddress(contractAddress);
			}
			logger.info("Loading contract from: " + contractAddress);
			contract = ArtMarket.load(contractAddress, web3, credentials, bgGas, bgGasLimit);
			logger.info("Loaded Contract: gas=" + gas + " gasLimit=" + gasLimit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contract;
	}

	@Override
	public String deployContract(Long gasLimit, Long gas) throws Exception {
		BigInteger bgGas = BigInteger.valueOf(gas);
		BigInteger bgGasLimit = BigInteger.valueOf(gasLimit);
		contract = ArtMarket.deploy(web3, credentials, bgGas, bgGasLimit).send();
		logger.info("Deployed Contract: contract=" + contract);
//		future.thenAccept(s -> logger.info("Deployed Contract: s=" + s));
		// future.get();
		contractAddress = contract.getContractAddress();
		logger.info("Deployed Contract: contract=" + contract.getContractAddress());
		return contractAddress;
	}

	@Override
	public BigInteger numbItems() {
		try {
			if (contract == null) {
				loadContract(EthereumService.remixGasLimit, EthereumService.remixGas);
			}
			BigInteger result;
			RemoteCall<BigInteger> numbItems = contract.itemIndex();
			result = numbItems.send();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Item lookupItemByHash(String hash) {
		try {
			if (contract == null) {
				loadContract(EthereumService.remixGasLimit, EthereumService.remixGas);
			}
			logger.info("Looking up by hash from contract: " + contractAddress);
			logger.info("Looking up by hash from contract: " + contract.getContractAddress());
			Item item = null;
			Long numbItems = numbItems().longValue();
			Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean> tuple = null;
			for (Long itemIndex = numbItems; itemIndex > -1; itemIndex--) {
				tuple = contract.items(BigInteger.valueOf(itemIndex)).send();
				String timestamp = _0X + new String(Hex.encode(tuple.getValue3()));
				if (timestamp.equals(hash)) {
					item = new Item(itemIndex, tuple);
					logger.info("Item for hash: in contract: " + contract.getContractAddress() + " item: " + item.toString());
					break;
				}
			}
			return item;
		} catch (Exception e) {
			logger.error("Item for hash: " + hash + " was not found for the current contrcat: " + contract.getContractAddress());
			return null;
		}
	}

	@Override
	public List<Item> fetchItems() {
		List<Item> items = new ArrayList<Item>();
		try {
			if (contract == null) {
				loadContract(EthereumService.remixGasLimit, EthereumService.remixGas);
			}
			logger.info("Fetching items from contract: " + contractAddress);
			logger.info("Fetching items from contract: " + contract.getContractAddress());
			Long numbItems = numbItems().longValue();
			if (numbItems > 100) {
				numbItems = 100L;
			}
			Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean> tuple = null;
			Item item = null;
			for (Long itemIndex = numbItems; itemIndex > -1; itemIndex--) {
				try {
					tuple = contract.items(BigInteger.valueOf(itemIndex)).send();
					item = new Item(itemIndex, tuple);
					addOwners(item);
					items.add(item);
				} catch (Exception e) {
					logger.error("Item at " + itemIndex +  " threw error in contract: " + contract.getContractAddress(), e);
				}
			}
			return items;
		} catch (Exception e) {
			logger.error("Fetch Item threw error in contract: " + contract.getContractAddress(), e);
			return items;
		}
	}
	
	private void addOwners(Item item) throws Exception {
		Long owners = item.getOwnerIndex().longValue() + 1;
		for (int i = 0; i < owners; i++) {
			item.addOwner(contract.getItemOwner(BigInteger.valueOf(item.getItemIndex()), BigInteger.valueOf(i)).send());
		}
	}

}
