package org.brightblock.mam.ethereum.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


public interface EthereumService
{
	public static final Long remixGasLimit = 3000000L;
	public static final Long remixGas = 101000000000L;
	public static final String _0X = "0x";

	public void subscribeBlocks();
	public void unSubscribeBlocks();
	public ArtMarketJson getContractInfo() throws IOException; 
	public ArtMarket loadContract(Long gasLimit, Long gas);
	public void deployContract(Long gasLimit, Long gas) throws Exception;
	public BigInteger numbItems();
	public Item lookupItemByHash(String hash);
	public List<Item> fetchItems();
}
