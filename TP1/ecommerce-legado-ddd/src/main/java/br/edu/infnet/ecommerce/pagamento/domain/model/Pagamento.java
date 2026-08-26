package br.edu.infnet.ecommerce.pagamento.domain.model;

import br.edu.infnet.ecommerce.pagamento.domain.valueobject.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.valueobject.PagamentoId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {

    private PagamentoId id;
    private Long pedidoId;
    private Long usuarioId;
    private Dinheiro valor;
    private FormaPagamento formaPagamento;
    private String numeroCartaoMascarado;
    private StatusPagamento status;
    private String motivo;
    private String codigoAutorizacao;
    private LocalDateTime processadoEm;

    public PagamentoId getId() {
        return id;
    }

    public void setId(PagamentoId id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Dinheiro getValor() {
        return valor;
    }

    public void setValor(Dinheiro valor) {
        this.valor = valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getNumeroCartaoMascarado() {
        return numeroCartaoMascarado;
    }

    public void setNumeroCartaoMascarado(String numeroCartaoMascarado) {
        this.numeroCartaoMascarado = numeroCartaoMascarado;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCodigoAutorizacao() {
        return codigoAutorizacao;
    }

    public void setCodigoAutorizacao(String codigoAutorizacao) {
        this.codigoAutorizacao = codigoAutorizacao;
    }

    public LocalDateTime getProcessadoEm() {
        return processadoEm;
    }

    public void setProcessadoEm(LocalDateTime processadoEm) {
        this.processadoEm = processadoEm;
    }
}