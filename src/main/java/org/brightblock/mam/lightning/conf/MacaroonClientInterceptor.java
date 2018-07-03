package org.brightblock.mam.lightning.conf;


import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

import java.io.InputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.Context;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

/**
 * Macaroon Client Call Interceptor used to inject current macaroon into each GRPC call.
 *
 * @see MacaroonContext
 * Created by Philip Vendil on 2018-02-03.
 */
public class MacaroonClientInterceptor implements io.grpc.ClientInterceptor {

    private static final Logger logger = LogManager.getLogger(MacaroonClientInterceptor.class);
    private static final Metadata.Key<String> MACAROON_METADATA_KEY = Metadata.Key.of("macaroon", ASCII_STRING_MARSHALLER);

    private String currentMacaroonData;

    public MacaroonClientInterceptor(InputStream fis) {
        try {
            //FileInputStream fis = new FileInputStream(macaroonPath);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            currentMacaroonData = DatatypeConverter.printHexBinary(data);
        } catch(Exception e) {
			logger.info("MacaroonClientInterceptor: getMacaroon from: /static/", e);
            throw new RuntimeException("Error reading macaroon from path '" + fis + "', message: " + e.getMessage(), e);
        }
    }

    /**
     * Intercept {@link ClientCall} creation by the {@code next} {@link Channel}.
     * <p>
     * <p>Many variations of interception are possible. Complex implementations may return a wrapper
     * around the result of {@code next.newCall()}, whereas a simpler implementation may just modify
     * the header metadata prior to returning the result of {@code next.newCall()}.
     * <p>
     * <p>{@code next.newCall()} <strong>must not</strong> be called under a different {@link Context}
     * other than the current {@code Context}. The outcome of such usage is undefined and may cause
     * memory leak due to unbounded chain of {@code Context}s.
     *
     * @param method      the remote method to be called.
     * @param callOptions the runtime options to be applied to this call.
     * @param next        the channel which is being intercepted.
     * @return the call object for the remote operation, never {@code null}.
     */
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(MACAROON_METADATA_KEY, currentMacaroonData);
                super.start(responseListener, headers);
            }
        };
    }

}
