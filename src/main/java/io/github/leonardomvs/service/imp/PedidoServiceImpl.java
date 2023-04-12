package io.github.leonardomvs.service.imp;

import io.github.leonardomvs.domain.entity.Cliente;
import io.github.leonardomvs.domain.entity.ItemPedido;
import io.github.leonardomvs.domain.entity.Pedido;
import io.github.leonardomvs.domain.entity.Produto;
import io.github.leonardomvs.domain.enums.StatusPedido;
import io.github.leonardomvs.domain.repository.Clientes;
import io.github.leonardomvs.domain.repository.ItensPedido;
import io.github.leonardomvs.domain.repository.Pedidos;
import io.github.leonardomvs.domain.repository.Produtos;
import io.github.leonardomvs.exception.PedidoNaoEncontradoException;
import io.github.leonardomvs.exception.RegraNegocioException;
import io.github.leonardomvs.rest.dto.ItemPedidoDTO;
import io.github.leonardomvs.rest.dto.PedidoDTO;
import io.github.leonardomvs.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos pedidosRepository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedido = converterItens(pedido, dto.getItems());
        pedidosRepository.save(pedido);
        itensPedidoRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidosRepository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidosRepository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens){
        if(itens.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
        }

        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;

                }).collect(Collectors.toList());

    }

}
