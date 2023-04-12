package io.github.leonardomvs.domain.repository;

import io.github.leonardomvs.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}
