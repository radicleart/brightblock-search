package org.brightblock.mam.lightning.conf;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.brightblock.mam.conf.ApplicationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;

@Configuration
public class LndServerApplication {

    private static final Logger logger = LogManager.getLogger(LndServerApplication.class);
    private ManagedChannel aliceChannel;
    private ManagedChannel bobChannel;
	//private final static String host = "localhost";
	//private final static int port = 10001;
	@Autowired ApplicationSettings applicationSettings;

	@PreDestroy
    public void destroy() {
		logger.info("GRPC: Channel shutting down: " + aliceChannel.authority());
		aliceChannel.shutdown();
		bobChannel.shutdown();
    }
	
	@Bean(name = "aliceChannel")
	ManagedChannel createLndRpcAlice() throws IOException {
		String certFileName = applicationSettings.getLightningCertFileName();
		String baseUrl = applicationSettings.getLightningBase();
		int portAlice = applicationSettings.getLightningPortAlice();
		aliceChannel = createChannel(certFileName, "alice.admin.macaroon", baseUrl, portAlice);
		return aliceChannel;
	}
	
	@Bean(name = "bobChannel")
	ManagedChannel createLndRpcBob() throws IOException {
		String certFileName = applicationSettings.getLightningCertFileName();
		String baseUrl = applicationSettings.getLightningBase();
		int portBob = applicationSettings.getLightningPortBob();
		bobChannel = createChannel(certFileName, "bob.admin.macaroon", baseUrl, portBob);
		return bobChannel;
	}
	
	private ManagedChannel createChannel(String certFileName, String macFileName, String baseUrl, int port) throws IOException {
		logger.info("GRPC: Channel creation using settings: " + baseUrl + ":" + port);
		logger.info("GRPC: Channel creation using certificat: " + certFileName);
		InputStream stream  = LndServerApplication.class.getResourceAsStream("/static/" + certFileName);
		SslContext sslContext = GrpcSslContexts
				.configure(SslContextBuilder.forClient(), SslProvider.OPENSSL)
				.trustManager(stream)
				.build();
		InputStream aliceAdminMacaroon = getMacaroonStream(macFileName);
		ManagedChannel channel = NettyChannelBuilder.forAddress(baseUrl, port)
                .sslContext(sslContext)
                .intercept(new MacaroonClientInterceptor(aliceAdminMacaroon))
                .build();
		logger.info("GRPC: Channel successfully created: " + channel.authority());
		this.aliceChannel = channel;
		return channel;

	}

	private InputStream getMacaroonStream(String name) throws IOException {
		InputStream initialStream = null;
	    try {
			initialStream = LndServerApplication.class.getResourceAsStream("/static/" + name);
			return initialStream;
		} catch(Exception e) {
			logger.info("GRPC: Error getting macaroon: /static/" + name, e);
			throw new RuntimeException(e);
		}
	    finally {
	    		// initialStream.close(); stream gets passed down to grpc so not closed here.
		}
	}

}
