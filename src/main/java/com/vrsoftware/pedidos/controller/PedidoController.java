package com.vrsoftware.pedidos.controller;

import com.vrsoftware.pedidos.model.Pedido;
import com.vrsoftware.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        return service.processarPedido(pedido);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> statusPedido(@PathVariable String id) {
        return service.statusPedido(id);
    }

}
