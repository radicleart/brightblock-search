package org.brightblock.mam.lightning.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.lightning.proto.LightningGrpc;
import org.brightblock.mam.lightning.proto.LightningService;
import org.brightblock.mam.lightning.proto.LightningService.GetInfoRequest;
import org.brightblock.mam.lightning.proto.LightningService.LightningAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import io.grpc.ManagedChannel;

@RestController
public class LightningPeersController {
	
	@Autowired ManagedChannel aliceChannel;
	@Autowired ManagedChannel bobChannel;
	private Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(LightningPeersController.class);

	@RequestMapping("/lnd/{channel}/getInfo")
	String getInfo(@PathVariable String channel) throws InvalidProtocolBufferException {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.GetInfoRequest request = LightningService.GetInfoRequest.newBuilder().build();
		LightningService.GetInfoResponse response = stub.getInfo(GetInfoRequest.getDefaultInstance());
		logger.info("GRPC: GetInfoResponse: " + response.getIdentityPubkey(), response);
		logger.info("GRPC: " + gson.toJson(response));
		return gson.toJson(stub.getInfo(request).toBuilder());
    }
	
	@RequestMapping("/lnd/{channel}/getNodeInfo/{pubkey}")
	String getNodeInfo(@PathVariable String channel, @PathVariable String pubkey) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.NodeInfoRequest request = LightningService.NodeInfoRequest.newBuilder().setPubKey(pubkey).build();
		return gson.toJson(stub.getNodeInfo(request).toBuilder());
    }

	@RequestMapping("/lnd/{channel}/listPeers")
	String listPeers(@PathVariable String channel) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.ListPeersRequest request = LightningService.ListPeersRequest.newBuilder().build();
		return gson.toJson(stub.listPeers(request).toBuilder());
    }

	@RequestMapping("/lnd/{channel}/describeGraph")
	String describeGraph(@PathVariable String channel) throws InvalidProtocolBufferException {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.ChannelGraphRequest request = LightningService.ChannelGraphRequest.newBuilder().build();
		return gson.toJson(stub.describeGraph(request).toBuilder());
    }

	@RequestMapping("/lnd/{channel}/connect/{addr}/{pubkey}")
	String connect(@PathVariable String channel, @PathVariable String addr, @PathVariable String pubkey) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningAddress la = LightningAddress.newBuilder().setHost(addr).setPubkey(pubkey).build();
		LightningService.ConnectPeerRequest request = LightningService.ConnectPeerRequest.newBuilder().setPerm(false).setAddr(la).build();
		return gson.toJson(stub.connectPeer(request).toBuilder());
    }

	@RequestMapping("/lnd/{channel}/disconnect/{pubkey}")
	String disconnect(@PathVariable String channel, @PathVariable String pubkey) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.DisconnectPeerRequest request = LightningService.DisconnectPeerRequest.newBuilder().setPubKey(pubkey).build();
		return gson.toJson(stub.disconnectPeer(request).toBuilder());
    }

	private ManagedChannel getMyChannel(String channel) {
		if (channel.equals("bob")) {
			return bobChannel;
		} else {
			return aliceChannel;
		}
	}
}
