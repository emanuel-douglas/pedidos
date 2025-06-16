package com.vrsoftware.pedidos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vrsoftware.pedidos.model.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final RabbitTemplate rabbitTemplate;
    private final Map<UUID, String> statusMap = new HashMap<>();

    public PedidoServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public ResponseEntity<?> processarPedido(Pedido pedido) {
        try {
            if (pedido.getProduto() == null || pedido.getProduto().isBlank() || pedido.getQuantidade() <= 0) {
                return ResponseEntity.badRequest().body("Produto inválido ou quantidade <= 0");
            }

            pedido.setId(UUID.randomUUID());
            pedido.setDataCriacao(LocalDateTime.now());
            statusMap.put(pedido.getId(), "RECEBIDO");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(pedido);

            rabbitTemplate.convertAndSend("pedidos.entrada.douglas", json);
            return ResponseEntity.accepted().body(Map.of("id", pedido.getId()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> statusPedido(String idStr) {
        try {
            UUID id = UUID.fromString(idStr);
            String status = statusMap.getOrDefault(id, "DESCONHECIDO");
            return ResponseEntity.ok(Map.of("id", id, "status", status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("UUID inválido");
        }
    }

    public void atualizarStatus(UUID id, String status) {
        statusMap.put(id, status);
    }
}
