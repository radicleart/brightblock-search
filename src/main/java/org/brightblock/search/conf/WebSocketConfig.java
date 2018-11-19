package org.brightblock.search.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    private static final Logger logger = LogManager.getLogger(WebSocketConfig.class);
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/exchanges").setAllowedOrigins("*").withSockJS();
    }

    public void configureMessageBroker(MessageBrokerRegistry registry) {
		logger.info("====================================================================================");
		logger.info("starting spring brightblock application " + this.getClass().getName());
		logger.info("====================================================================================");

        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");   // Enables a simple in-memory broker

//        registry.enableStompBrokerRelay("/topic", "/queue").setRelayHost("rabbit1").setSystemLogin("mijobc").setSystemPasscode("b1dl0g1x");        
//        registry.setApplicationDestinationPrefixes("/app");
    }

//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/generate").setAllowedOrigins("*").withSockJS();
//        registry.addEndpoint("/exchanges").setAllowedOrigins("*").withSockJS();
//    }
}