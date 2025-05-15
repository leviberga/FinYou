package br.com.fiap.fintech.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Investimento {
    private Integer codigo;
    private String nome;
    private String tipo;
    private BigDecimal valorAplicado;
    private LocalDate dataAplicacao;
    private BigDecimal valorAtual;
    private Integer codigoUsuario;
    private Integer codigoConta;

    public Investimento() {
        this.dataAplicacao = LocalDate.now();
    }

    public Investimento(Integer codigo, String nome, String tipo, BigDecimal valorAplicado, LocalDate dataAplicacao,
                        BigDecimal valorAtual, Integer codigoUsuario, Integer codigoConta) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = dataAplicacao;
        this.valorAtual = valorAtual;
        this.codigoUsuario = codigoUsuario;
        this.codigoConta = codigoConta;
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

    public BigDecimal getValorAplicado() {
        return valorAplicado;
    }

    public void setValorAplicado(BigDecimal valorAplicado) {
        this.valorAplicado = valorAplicado;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }

    public Integer getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Integer getCodigoConta() {
        return codigoConta;
    }

    public void setCodigoConta(Integer codigoConta) {
        this.codigoConta = codigoConta;
    }

    public BigDecimal calcularRendimento() {
        if (valorAplicado == null || valorAtual == null || valorAplicado.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return valorAtual.subtract(valorAplicado);
    }

    public BigDecimal calcularPercentualRendimento() {
        if (valorAplicado == null || valorAtual == null || valorAplicado.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal rendimento = calcularRendimento();
        return rendimento.multiply(new BigDecimal("100")).divide(valorAplicado, 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return "Investimento [codigo=" + codigo + ", nome=" + nome + ", tipo=" + tipo + ", valorAplicado="
                + valorAplicado + ", dataAplicacao=" + dataAplicacao + ", valorAtual=" + valorAtual
                + ", codigoUsuario=" + codigoUsuario + ", codigoConta=" + codigoConta + "]";
    }
}