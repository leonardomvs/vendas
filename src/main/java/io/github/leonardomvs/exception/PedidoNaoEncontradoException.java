package io.github.leonardomvs.exception;

public class PedidoNaoEncontradoException extends  RuntimeException {
    public PedidoNaoEncontradoException(){
        super("Pedido não encontrado");
    }
}
