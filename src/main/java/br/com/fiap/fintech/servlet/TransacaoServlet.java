package br.com.fiap.fintech.servlet;

import br.com.fiap.fintech.dao.TransacaoDAO;
import br.com.fiap.fintech.model.Transacao;
import br.com.fiap.fintech.dao.ContaDAO;
import br.com.fiap.fintech.model.Conta;
import br.com.fiap.fintech.model.Usuario;

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
        HttpSession session = req.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            ContaDAO contaDAO = new ContaDAO();
            List<Conta> contas = contaDAO.listarPorUsuario(usuarioLogado.getCodigo());
            Conta conta = contas.isEmpty() ? null : contas.get(0);
            req.setAttribute("contaLogada", conta);

            String codigoStr = req.getParameter("codigo");

            if (codigoStr != null && !codigoStr.isEmpty()) {
                int codigo = Integer.parseInt(codigoStr);
                Transacao transacao = dao.buscarPorCodigo(codigo);
                req.setAttribute("transacao", transacao);
            } else {
                List<Transacao> transacoes = dao.listarTodos();
                req.setAttribute("transacoes", transacoes);
            }

            req.getRequestDispatcher("/jsp/Transacao.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao buscar dados: " + e.getMessage());
            e.printStackTrace();
            req.getRequestDispatcher("/jsp/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            String acao = req.getParameter("acao");

            if ("inserir".equalsIgnoreCase(acao)) {
                Transacao transacao = montarTransacao(req);
                dao.inserir(transacao);
                // Redireciona para o dashboard após inserir
                resp.sendRedirect(req.getContextPath() + "/dashboard?mensagem=Transacao inserida com sucesso&refresh=true");

            } else if ("atualizar".equalsIgnoreCase(acao)) {
                Transacao transacao = montarTransacao(req);
                transacao.setCodigo(Integer.parseInt(req.getParameter("codigo")));
                dao.atualizar(transacao);
                // Redireciona para o dashboard após atualizar
                resp.sendRedirect(req.getContextPath() + "/dashboard?mensagem=Transação atualizada com sucesso&refresh=true");

            } else if ("excluir".equalsIgnoreCase(acao)) {
                int codigo = Integer.parseInt(req.getParameter("codigo"));
                dao.excluir(codigo);
                // Redireciona para o dashboard após excluir
                resp.sendRedirect(req.getContextPath() + "/dashboard?mensagem=Transação excluída com sucesso&refresh=true");

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