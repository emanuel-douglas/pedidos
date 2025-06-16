package com.vrsoftware.pedidos.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String FILA_ENTRADA = "pedidos.entrada.douglas";
    private static final String FILA_DLQ = "pedidos.entrada.douglas.dlq";
    private static final String FILA_STATUS_SUCESSO = "pedidos.status.sucesso.douglas";
    private static final String FILA_STATUS_FALHA = "pedidos.status.falha.douglas";

    @Bean
    public Queue filaEntrada() {
        return QueueBuilder.durable(FILA_ENTRADA)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", FILA_DLQ)
                .build();
    }

    @Bean
    public Queue filaDLQ() {
        return QueueBuilder.durable(FILA_DLQ).build();
    }

    @Bean
    public Queue filaStatusSucesso() {
        return new Queue(FILA_STATUS_SUCESSO, true);
    }

    @Bean
    public Queue filaStatusFalha() {
        return new Queue(FILA_STATUS_FALHA, true);
    }
}
