package org.brightblock.mam.ethereum.service;

import java.io.IOException;
import java.math.BigInteger;


public interface EthereumService
{
	public String getWeb3ClientVersion() throws IOException;
	public ArtMarket loadContract(Long gasLimit, Long gas);
	public void deployContract(Long gasLimit, Long gas) throws Exception;
	public BigInteger numbItems();
}
