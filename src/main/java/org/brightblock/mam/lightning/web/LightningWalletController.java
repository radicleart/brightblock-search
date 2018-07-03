package org.brightblock.mam.lightning.web;

import org.brightblock.mam.lightning.proto.LightningGrpc;
import org.brightblock.mam.lightning.proto.LightningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import io.grpc.ManagedChannel;

@RestController
public class LightningWalletController {
	
	@Autowired ManagedChannel aliceChannel;
	@Autowired ManagedChannel bobChannel;
	private Gson gson = new Gson();

	@RequestMapping("/lnd/{channel}/walletbalance")
    String balance(@PathVariable String channel) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.WalletBalanceRequest request = LightningService.WalletBalanceRequest.newBuilder().build();
		return gson.toJson(stub.walletBalance(request).toBuilder());
    }
	
	private ManagedChannel getMyChannel(String channel) {
		if (channel.equals("bob")) {
			return bobChannel;
		} else {
			return aliceChannel;
		}
	}
}
