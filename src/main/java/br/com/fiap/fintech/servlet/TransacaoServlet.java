package br.com.fiap.fintech.servlet;

import br.com.fiap.fintech.dao.TransacaoDAO;
import br.com.fiap.fintech.model.Transacao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/transacao")
public class TransacaoServlet extends HttpServlet {

    private TransacaoDAO dao;

    @Override
    public void init() throws ServletException {
        try {
            dao = new TransacaoDAO();
        } catch (SQLException e) {
            throw new ServletException("Erro ao iniciar TransacaoDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String codigoStr = req.getParameter("codigo");

            if (codigoStr != null && !codigoStr.isEmpty()) {
                int codigo = Integer.parseInt(codigoStr);
                Transacao transacao = dao.buscarPorCodigo(codigo);
                req.setAttribute("transacao", transacao);
                req.getRequestDispatcher("/jsp/Transacao.jsp").forward(req, resp);
            } else {
                List<Transacao> transacoes = dao.listarTodos();
                req.setAttribute("transacoes", transacoes);
                req.getRequestDispatcher("/jsp/Transacao.jsp").forward(req, resp);
            }

        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("erro", "Erro ao buscar transações: " + e.getMessage());
            e.printStackTrace();
            req.getRequestDispatcher("/jsp/erro.jsp").forward(req, resp);
            System.out.println();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        System.out.println("Recebido POST em /transacao");

        try {
            String acao = req.getParameter("acao");
            System.out.println("Ação recebida: " + acao);

            if ("inserir".equalsIgnoreCase(acao)) {
                Transacao transacao = montarTransacao(req);
                dao.inserir(transacao);
                resp.sendRedirect("transacao?mensagem=Transação inserida com sucesso");

            } else if ("atualizar".equalsIgnoreCase(acao)) {
                Transacao transacao = montarTransacao(req);
                transacao.setCodigo(Integer.parseInt(req.getParameter("codigo")));
                dao.atualizar(transacao);
                resp.sendRedirect("transacao?mensagem=Transação atualizada com sucesso");

            } else if ("excluir".equalsIgnoreCase(acao)) {
                int codigo = Integer.parseInt(req.getParameter("codigo"));
                dao.excluir(codigo);
                resp.sendRedirect("transacao?mensagem=Transação excluída com sucesso");

            } else {
                req.setAttribute("erro", "Ação inválida.");
                req.getRequestDispatcher("/jsp/erro.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao processar transação: " + e.getMessage());
            req.getRequestDispatcher("/jsp/erro.jsp").forward(req, resp);
        }
    }

    private Transacao montarTransacao(HttpServletRequest req) throws Exception {
        Transacao transacao = new Transacao();

        String dataStr = req.getParameter("data");
        String valorStr = req.getParameter("valor");
        String tipo = req.getParameter("tipo");
        String descricao = req.getParameter("descricao");
        String contaOrigemStr = req.getParameter("contaOrigem");
        String contaDestinoStr = req.getParameter("contaDestino");

        System.out.println("Dados recebidos:");
        System.out.println("Data: " + dataStr);
        System.out.println("Valor: " + valorStr);
        System.out.println("Tipo: " + tipo);
        System.out.println("Descrição: " + descricao);
        System.out.println("Conta Origem: " + contaOrigemStr);
        System.out.println("Conta Destino: " + contaDestinoStr);

        if (dataStr == null || valorStr == null || tipo == null || contaOrigemStr == null)
            throw new Exception("Campos obrigatórios não foram preenchidos");

        transacao.setData(LocalDate.parse(dataStr));
        transacao.setValor(new BigDecimal(valorStr));
        transacao.setTipo(tipo);
        transacao.setDescricao(descricao != null ? descricao : "");
        transacao.setCodigoContaOrigem(Integer.parseInt(contaOrigemStr));

        if (contaDestinoStr != null && !contaDestinoStr.isEmpty()) {
            transacao.setCodigoContaDestino(Integer.parseInt(contaDestinoStr));
        } else {
            transacao.setCodigoContaDestino(null);
        }

        return transacao;
    }
}
