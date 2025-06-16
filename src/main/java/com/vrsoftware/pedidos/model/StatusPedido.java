package com.vrsoftware.pedidos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedido {

    private UUID idPedido;
    private Status status;
    private LocalDateTime dataProcessamento;
    private String mensagemErro;

}
