package br.com.fiap.fintech.model;

import java.math.BigDecimal;

public class Conta {
    private Integer codigo;
    private String nome;
    private String tipo;
    private BigDecimal saldo;
    private Integer codigoUsuario;

    public Conta() {
        this.saldo = BigDecimal.ZERO;
    }

    public Conta(Integer codigo, String nome, String tipo, BigDecimal saldo, Integer codigoUsuario) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.saldo = saldo;
        this.codigoUsuario = codigoUsuario;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Integer getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    @Override
    public String toString() {
        return "Conta [codigo=" + codigo + ", nome=" + nome + ", tipo=" + tipo + ", saldo=" + saldo + ", codigoUsuario="
                + codigoUsuario + "]";
    }
}