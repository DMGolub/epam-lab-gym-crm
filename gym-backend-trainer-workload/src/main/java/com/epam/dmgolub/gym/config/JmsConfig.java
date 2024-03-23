package com.epam.dmgolub.gym.config;

import jakarta.jms.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;

@EnableJms
@Configuration
public class JmsConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JmsConfig.class);

	private final ConnectionFactory connectionFactory;

	public JmsConfig(final ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}


	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		final var converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	@Bean
	public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
		final var factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(jacksonJmsMessageConverter());
		factory.setConcurrency("1-10");
		factory.setErrorHandler(e -> LOGGER.error("[{}] Failed to process request in listener due to {} with message: {}",
			MDC.get(TRANSACTION_ID), e.getClass().getSimpleName(), e.getMessage()));
		return factory;
	}
}
