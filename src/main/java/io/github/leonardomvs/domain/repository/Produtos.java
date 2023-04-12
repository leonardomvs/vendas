package io.github.leonardomvs.domain.repository;

import io.github.leonardomvs.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
