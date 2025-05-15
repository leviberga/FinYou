package br.com.fiap.fintech.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transacao {
    private Integer codigo;
    private LocalDate data;
    private BigDecimal valor;
    private String tipo;
    private String descricao;
    private Integer codigoContaOrigem;
    private Integer codigoContaDestino;

    public Transacao() {
        this.data = LocalDate.now();
    }

    public Transacao(Integer codigo, LocalDate data, BigDecimal valor, String tipo, String descricao,
                     Integer codigoContaOrigem, Integer codigoContaDestino) {
        this.codigo = codigo;
        this.data = data;
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.codigoContaOrigem = codigoContaOrigem;
        this.codigoContaDestino = codigoContaDestino;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigoContaOrigem() {
        return codigoContaOrigem;
    }

    public void setCodigoContaOrigem(Integer codigoContaOrigem) {
        this.codigoContaOrigem = codigoContaOrigem;
    }

    public Integer getCodigoContaDestino() {
        return codigoContaDestino;
    }

    public void setCodigoContaDestino(Integer codigoContaDestino) {
        this.codigoContaDestino = codigoContaDestino;
    }

    @Override
    public String toString() {
        return "Transacao [codigo=" + codigo + ", data=" + data + ", valor=" + valor + ", tipo=" + tipo + ", descricao="
                + descricao + ", codigoContaOrigem=" + codigoContaOrigem + ", codigoContaDestino=" + codigoContaDestino
                + "]";
    }
}
