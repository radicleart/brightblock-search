package org.brightblock.mam.lightning.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.lightning.proto.LightningGrpc;
import org.brightblock.mam.lightning.proto.LightningService;
import org.brightblock.mam.lightning.proto.LightningService.ChannelPoint;
import org.brightblock.mam.lightning.proto.LightningService.CloseStatusUpdate;
import org.brightblock.mam.lightning.proto.LightningService.OpenStatusUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.grpc.ManagedChannel;
import io.grpc.stub.ClientCallStreamObserver;

@RestController
public class LightningChannelsController {
	
    private static final Logger logger = LogManager.getLogger(LightningChannelsController.class);
	@Autowired ManagedChannel aliceChannel;
	@Autowired ManagedChannel bobChannel;
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	private Gson gson = new Gson();

	@RequestMapping("/lnd/{channel}/pendingChannels")
	String pendingChannels(@PathVariable String channel) throws InvalidProtocolBufferException {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.PendingChannelsRequest request = LightningService.PendingChannelsRequest.newBuilder()
				.build();
		LightningService.PendingChannelsResponse response = stub.pendingChannels(request);
		return gson.toJson(response.toBuilder());
    }
	
	@RequestMapping("/lnd/{channel}/listChannels")
	String listChannels(@PathVariable String channel) throws InvalidProtocolBufferException {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.ListChannelsRequest request = LightningService.ListChannelsRequest.newBuilder()
				.setActiveOnly(false).setInactiveOnly(false).setPrivateOnly(false).setPublicOnly(false).build();
		LightningService.ListChannelsResponse response = stub.listChannels(request);
		return gson.toJson(response.toBuilder());
    }
	
	@RequestMapping("/lnd/{channel}/closedChannels")
	String closedChannels(@PathVariable String channel) throws InvalidProtocolBufferException {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.ClosedChannelsRequest request = LightningService.ClosedChannelsRequest.newBuilder()
				.build();
		LightningService.ClosedChannelsResponse response = stub.closedChannels(request);
		return gson.toJson(response.toBuilder());
    }
	
	@RequestMapping("/lnd/{channel}/closeChannel/{pubkey}/{amt}")
	String closeChannel(@PathVariable String channel, @PathVariable String pubkey, @PathVariable Long amt) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		ByteString bs = ByteString.copyFromUtf8(pubkey);
		LightningService.CloseChannelRequest request = LightningService.CloseChannelRequest.newBuilder()
				.setChannelPoint(ChannelPoint.newBuilder().build())
				.build();
		//stub.getChannel();
        @SuppressWarnings("unchecked")
		Consumer<CloseStatusUpdate> action = new Consumer() {
			@Override
			public void accept(Object arg0) {
				logger.info("GRPC: OpenStatusUpdate: " + arg0);
			}
		};
		Iterator<CloseStatusUpdate> it = stub.closeChannel(request); // .forEachRemaining(action);
//		OpenStatusUpdate osu = null;
//		while (updates.hasNext()) {
//			osu = updates.next();
//			logger.info("GRPC: OpenStatusUpdate: " + osu);
//		}
		return "";
		//gson.toJson(osu.toBuilder());
    }


	@RequestMapping("/lnd/{channel}/openChannelSync/{pubkey}/{amt}")
	String openChannelSync(@PathVariable String channel, @PathVariable String pubkey, @PathVariable Long amt) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		ByteString bs = ByteString.copyFromUtf8(pubkey);
		LightningService.OpenChannelRequest request = LightningService.OpenChannelRequest.newBuilder()
				.setNodePubkeyString(pubkey)
				.setLocalFundingAmount(amt)
				.build();
		//stub.getChannel();
        @SuppressWarnings("unchecked")
		Consumer<OpenStatusUpdate> action = new Consumer() {
			@Override
			public void accept(Object arg0) {
				logger.info("GRPC: OpenStatusUpdate: " + arg0);
			}
		};
		stub.openChannel(request).forEachRemaining(action);
//		OpenStatusUpdate osu = null;
//		while (updates.hasNext()) {
//			osu = updates.next();
//			logger.info("GRPC: OpenStatusUpdate: " + osu);
//		}
		return "";
		//gson.toJson(osu.toBuilder());
    }


	@RequestMapping("/lnd/{channel}/openChannel/{pubkey}/{amt}")
	void openChannel(@PathVariable String channel, @PathVariable String pubkey, @PathVariable Long amt) throws IOException {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningStub stub = LightningGrpc.newStub(myChannel);
		ByteString bs = ByteString.copyFrom(pubkey.getBytes());
		InputStream is = new ByteArrayInputStream(String.valueOf(Hex.encode(pubkey.getBytes())).getBytes());
		
		LightningService.OpenChannelRequest request = LightningService.OpenChannelRequest.newBuilder()
				.setSatPerByte(1000).setNodePubkey(ByteString.copyFrom(pubkey.getBytes()))
				//.setNodePubkeyString(pubkey)
				.setLocalFundingAmount(amt)
				.build();

		//logger.info("Hex encoded pubkey: \n" + ByteString.copyFrom(pubkey.getBytes()));
		//logger.info("Hex encoded pubkey: \n" + String.valueOf(Hex.encode(pubkey.getBytes())));
        ClientCallStreamObserver<OpenStatusUpdate> streamObserver = new ClientCallStreamObserver<OpenStatusUpdate>() {
			
			@Override
			public void onNext(OpenStatusUpdate value) {
                logger.info("GRPC: <-- " + value);
        			String jsonString = gson.toJson(value.toBuilder().build());
        	    		simpMessagingTemplate.convertAndSend("/topic/exchanges", jsonString);
			}
			
			@Override
			public void onError(Throwable e) {
                logger.info("GRPC: <-- ", e);
    				String jsonString = gson.toJson(e.getMessage());
    				simpMessagingTemplate.convertAndSend("/topic/exchanges", jsonString);
			}
			
			@Override
			public void onCompleted() {
                logger.info("GRPC: <-- completed");
				String jsonString = gson.toJson("completed");
				simpMessagingTemplate.convertAndSend("/topic/exchanges", jsonString);
			}
			
			@Override
			public void setOnReadyHandler(Runnable arg0) {
                logger.info("GRPC: <-- setOnReadyHandler");
			}
			
			@Override
			public void setMessageCompression(boolean arg0) {
                logger.info("GRPC: <-- setMessageCompression: " + arg0);
			}
			
			@Override
			public void request(int arg0) {
                logger.info("GRPC: <-- request: " + arg0);
				String jsonString = gson.toJson(arg0);
				simpMessagingTemplate.convertAndSend("/topic/exchanges", jsonString);
			}
			
			@Override
			public boolean isReady() {
                logger.info("GRPC: <-- isReady: ");
				return false;
			}
			
			@Override
			public void disableAutoInboundFlowControl() {
                logger.info("GRPC: <-- disableAutoInboundFlowControl: ");
			}
			
			@Override
			public void cancel(String message, Throwable e) {
                logger.info("GRPC: <-- cancel: " + message);
				String jsonString = gson.toJson("Canceled: " + message);
				simpMessagingTemplate.convertAndSend("/topic/exchanges", jsonString);
			}
		};
		stub.openChannel(request, streamObserver);
		//return gson.toJson("");
    }

	private ManagedChannel getMyChannel(String channel) {
		if (channel.equals("bob")) {
			return bobChannel;
		} else {
			return aliceChannel;
		}
	}
}
