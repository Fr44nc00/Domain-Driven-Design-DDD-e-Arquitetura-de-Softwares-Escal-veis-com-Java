package br.edu.infnet.ecommerce.shared.exception;

public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}
