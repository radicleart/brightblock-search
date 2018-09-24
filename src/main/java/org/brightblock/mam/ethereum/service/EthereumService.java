package org.brightblock.mam.ethereum.service;

import java.io.IOException;
import java.math.BigInteger;

import org.brightblock.mam.ethereum.rest.ArtMarket;

public interface EthereumService
{
	public String getWeb3ClientVersion() throws IOException;
	public ArtMarket loadContract();
	public ArtMarket deployContract() throws Exception;
	public BigInteger numbItems();
}
