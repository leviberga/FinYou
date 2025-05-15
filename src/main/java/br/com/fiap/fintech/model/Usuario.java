package br.com.fiap.fintech.model;

public class Usuario {
    private Integer codigo;
    private String email;
    private String senha;
    private boolean ativo;
    private Integer codigoPessoa;

    public Usuario() {
        this.ativo = true;
    }

    public Usuario(Integer codigo, String email, String senha, boolean ativo, Integer codigoPessoa) {
        this.codigo = codigo;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.codigoPessoa = codigoPessoa;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getCodigoPessoa() {
        return codigoPessoa;
    }

    public void setCodigoPessoa(Integer codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    @Override
    public String toString() {
        return "Usuario [codigo=" + codigo + ", email=" + email + ", ativo=" + ativo + ", codigoPessoa=" + codigoPessoa + "]";
    }
}