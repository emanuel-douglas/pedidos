package com.vrsoftware.pedidos.listener;

import com.vrsoftware.pedidos.exception.ExcecaoDeProcessamento;
import com.vrsoftware.pedidos.model.Pedido;
import com.vrsoftware.pedidos.model.Status;
import com.vrsoftware.pedidos.model.StatusPedido;
import com.vrsoftware.pedidos.service.PedidoService;
import com.vrsoftware.pedidos.service.PedidoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@Slf4j
public class PedidoListener {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoService service;
    private final Random random = new Random();

    public PedidoListener(PedidoService service, RabbitTemplate rabbitTemplate) {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "pedidos.entrada.douglas")
    public void consumirPedido(Pedido pedido) {
        log.info("Início do processamento do pedido [{}]", pedido.getId());
        service.atualizarStatus(pedido.getId(), "PROCESSANDO");

        try {
            Thread.sleep((1 + random.nextInt(3)) * 1000);

            if (Math.random() < 0.2) {
                throw new ExcecaoDeProcessamento("Erro ao processar pedido: " + pedido.getId());
            }

            service.atualizarStatus(pedido.getId(), "SUCESSO");

            StatusPedido status = new StatusPedido(pedido.getId(), Status.SUCESSO, LocalDateTime.now(), null);
            rabbitTemplate.convertAndSend("pedidos.status.sucesso.douglas", status);

            log.info("Processamento concluído com SUCESSO para o pedido [{}]", pedido.getId());

        } catch (ExcecaoDeProcessamento e) {
            service.atualizarStatus(pedido.getId(), "FALHA");

            StatusPedido status = new StatusPedido(pedido.getId(), Status.FALHA, LocalDateTime.now(), e.getMessage());
            rabbitTemplate.convertAndSend("pedidos.status.falha.douglas", status);

            log.error("Processamento FALHOU para o pedido [{}]: {}", pedido.getId(), e.getMessage());

            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            service.atualizarStatus(pedido.getId(), "FALHA_INTERRUPÇÃO");
            log.error("Processamento do pedido [{}] foi interrompido: {}", pedido.getId(), e.getMessage());
        }
    }
}
