package com.vrsoftware.pedidos.service;

import com.vrsoftware.pedidos.model.Pedido;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PedidoService {

    public ResponseEntity<?> processarPedido(Pedido pedido);
    ResponseEntity<?> statusPedido(String idStr);

    public void atualizarStatus(UUID id, String status);

}
