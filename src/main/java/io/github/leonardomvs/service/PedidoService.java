package io.github.leonardomvs.service;

import io.github.leonardomvs.domain.entity.Pedido;
import io.github.leonardomvs.domain.enums.StatusPedido;
import io.github.leonardomvs.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}