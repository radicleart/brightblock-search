package org.brightblock.mam.lightning.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.lightning.proto.LightningGrpc;
import org.brightblock.mam.lightning.proto.LightningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.grpc.ManagedChannel;

@RestController
public class LightningInvoiceController {
	
    private static final Logger logger = LogManager.getLogger(LightningInvoiceController.class);
	@Autowired ManagedChannel aliceChannel;
	@Autowired ManagedChannel bobChannel;
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	private Gson gson = new Gson();

	@RequestMapping("/lnd/{channel}/listInvoices")
	String listInvoices(@PathVariable String channel) throws InvalidProtocolBufferException {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		LightningService.ListInvoiceRequest request = LightningService.ListInvoiceRequest.newBuilder()
				.setPendingOnly(false).build();
		LightningService.ListInvoiceResponse response = stub.listInvoices(request);
		return gson.toJson(response.toBuilder());
    }
	
	/**
	 * memo	string	An optional memo to attach along with the invoice. Used for record keeping purposes for the invoice’s creator, and will also be set in the description field of the encoded payment request if the description_hash field is not being used.
	 * receipt	bytes	An optional cryptographic receipt of payment
	 * r_preimage	bytes	The hex-encoded preimage (32 byte) which will allow settling an incoming HTLC payable to this preimage
	 * r_hash	bytes	The hash of the preimage
	 * value	int64	The value of this invoice in satoshis
	 * settled	bool	Whether this invoice has been fulfilled
	 * creation_date	int64	When this invoice was created
	 * settle_date	int64	When this invoice was settled
	 * payment_request	string	A bare-bones invoice for a payment within the Lightning Network. With the details of the invoice, the sender has all the data necessary to send a payment to the recipient.
	 * description_hash	bytes	Hash (SHA-256) of a description of the payment. Used if the description of payment (memo) is too long to naturally fit within the description field of an encoded payment request.
	 * expiry	int64	Payment request expiry time in seconds. Default is 3600 (1 hour).
	 * fallback_addr	string	Fallback on-chain address.
	 * cltv_expiry	uint64	Delta to use for the time-lock of the CLTV extended to the final hop.
	 * route_hints	array RouteHint	Route hints that can each be individually used to assist in reaching the invoice’s destination.
	 * private	bool	Whether this invoice should include routing hints for private channels.
	 * @param channel
	 * @param pubkey
	 * @param amt
	 * @return
	 */
	@RequestMapping("/lnd/{channel}/addInvoice/{value}/{descriptionHash}/{memo}")
	String addInvoice(@PathVariable String channel, @PathVariable Long value, @PathVariable String descriptionHash, @PathVariable String memo) {
		ManagedChannel myChannel = getMyChannel(channel);
		LightningGrpc.LightningBlockingStub stub = LightningGrpc.newBlockingStub(myChannel);
		ByteString descriptionHashBytes = ByteString.copyFromUtf8(descriptionHash);
		LightningService.Invoice request = LightningService.Invoice.newBuilder()
				.setMemo(memo)
				.setDescriptionHash(descriptionHashBytes)
				.setValue(value).build();
		LightningService.AddInvoiceResponse response = stub.addInvoice(request);
		return gson.toJson(response.toBuilder());
    }

	private ManagedChannel getMyChannel(String channel) {
		if (channel.equals("bob")) {
			return bobChannel;
		} else {
			return aliceChannel;
		}
	}
}
