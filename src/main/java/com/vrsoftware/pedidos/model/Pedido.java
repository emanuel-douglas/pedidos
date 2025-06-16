package com.vrsoftware.pedidos.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Pedido implements Serializable {

    private UUID id;
    private String produto;
    private int quantidade;
    private LocalDateTime dataCriacao;

}
