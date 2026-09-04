package com.pablo.notification.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Map;

/**
 * Configuração do RabbitMQ para o serviço de notificações.
 * Define exchanges, queues, bindings e estratégia de retry com DLQ.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Exchange principal para envio de notificações.
     */
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    /**
     * Fila principal de notificações.
     */
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    /**
     * Routing key para envio de notificações.
     */
    public static final String NOTIFICATION_ROUTING_KEY = "notification.send";

    /**
     * Exchange para mensagens em retry.
     */
    public static final String NOTIFICATION_RETRY_EXCHANGE = "notification.retry.exchange";

    /**
     * Fila de retry com TTL e dead-letter routing para reenvio automático.
     */
    public static final String NOTIFICATION_RETRY_QUEUE = "notification.retry.queue";

    /**
     * Routing key para envio à fila de retry.
     */
    public static final String NOTIFICATION_RETRY_ROUTING_KEY = "notification.retry";

    /**
     * Dead Letter Queue para mensagens que falharam após máximo de tentativas.
     */
    public static final String NOTIFICATION_DLQ = "notification.dlq";

    /**
     * Exchange da DLQ.
     */
    public static final String NOTIFICATION_DLQ_EXCHANGE =
            "notification.dlq.exchange";

    /**
     * Routing key da DLQ.
     */
    public static final String NOTIFICATION_DLQ_ROUTING_KEY =
            "notification.dlq";

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding notificationBinding(
            Queue notificationQueue,
            DirectExchange notificationExchange
    ) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public DirectExchange notificationRetryExchange() {
        return new DirectExchange(
                NOTIFICATION_RETRY_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue notificationRetryQueue() {

        Map<String, Object> arguments = Map.of(
                "x-message-ttl", 5000,
                "x-dead-letter-exchange", NOTIFICATION_EXCHANGE,
                "x-dead-letter-routing-key", NOTIFICATION_ROUTING_KEY
        );

        return new Queue(
                NOTIFICATION_RETRY_QUEUE,
                true,
                false,
                false,
                arguments
        );
    }

    @Bean
    public Binding notificationRetryBinding(
            Queue notificationRetryQueue,
            DirectExchange notificationRetryExchange
    ) {
        return BindingBuilder
                .bind(notificationRetryQueue)
                .to(notificationRetryExchange)
                .with(NOTIFICATION_RETRY_ROUTING_KEY);
    }


    @Bean
    public Queue notificationDlq() {
        return new Queue(NOTIFICATION_DLQ, true);
    }

    @Bean
    public DirectExchange notificationDlqExchange() {
        return new DirectExchange(
                NOTIFICATION_DLQ_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Binding notificationDlqBinding(
            Queue notificationDlq,
            DirectExchange notificationDlqExchange
    ) {
        return BindingBuilder
                .bind(notificationDlq)
                .to(notificationDlqExchange)
                .with(NOTIFICATION_DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}