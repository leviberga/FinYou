package br.com.fiap.fintech.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.fiap.fintech.model.Usuario;
import br.com.fiap.fintech.service.CadastroService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CadastroService cadastroService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Instancia o service uma vez na inicialização do servlet

        try {
            cadastroService = new CadastroService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Se alguém acessar /cadastro via GET, apenas mostramos a página de cadastro
        RequestDispatcher dispatcher = request.getRequestDispatcher("/cadastro.html"); // ou cadastro.jsp
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Garante o encoding correto para caracteres especiais

        // 1. Obter os parâmetros do formulário
        String nome = request.getParameter("nomeCompleto");
        String cpf = request.getParameter("cpf");
        String dataNascimentoStr = request.getParameter("dataNascimento");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String confirmarSenha = request.getParameter("confirmarSenha"); // Para validação

        // 2. Validações básicas (no servidor)
        if (nome == null || nome.trim().isEmpty() ||
                cpf == null || cpf.trim().isEmpty() ||
                dataNascimentoStr == null || dataNascimentoStr.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty()) {

            request.setAttribute("mensagemErro", "Todos os campos obrigatórios devem ser preenchidos.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cadastro.html"); // ou cadastro.jsp
            dispatcher.forward(request, response);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            request.setAttribute("mensagemErro", "As senhas não coincidem.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cadastro.html"); // ou cadastro.jsp
            dispatcher.forward(request, response);
            return;
        }

        LocalDate dataNascimento;
        try {
            // O input type="date" do HTML5 envia no formato yyyy-MM-dd
            dataNascimento = LocalDate.parse(dataNascimentoStr);
        } catch (DateTimeParseException e) {
            request.setAttribute("mensagemErro", "Formato de data de nascimento inválido. Use DD/MM/AAAA ou selecione no calendário.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cadastro.html"); // ou cadastro.jsp
            dispatcher.forward(request, response);
            return;
        }

        // 3. Chamar o CadastroService
        try {
            Usuario novoUsuario = cadastroService.cadastrarNovoUsuario(nome, cpf, dataNascimento, email, senha);

            if (novoUsuario != null) {
                System.out.println("Cadastro de " + email + " bem-sucedido. Redirecionando para página de login.");

                response.sendRedirect(request.getContextPath() + "/jsp/Login.jsp?mensagem=cadastroSucesso");

            } else {
                // Este 'else' raramente será atingido se o service lançar exceções
                request.setAttribute("mensagemErro", "Ocorreu um erro desconhecido durante o cadastro. Tente novamente.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Cadastro.jsp");
                dispatcher.forward(request, response);
            }

        } catch (SQLException e) {
            // Erro de banco de dados
            e.printStackTrace(); // Logar o erro no servidor
            request.setAttribute("mensagemErro", "Erro no servidor ao tentar realizar o cadastro. Tente mais tarde. (SQL)");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Cadastro.jsp"); // ou cadastro.jsp
            dispatcher.forward(request, response);
        } catch (IllegalArgumentException e) {
            // Erro de validação vindo do Service (ex: CPF/Email já existe)
            request.setAttribute("mensagemErro", e.getMessage()); // Exibe a mensagem específica da exceção
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Cadastro.jsp"); // ou cadastro.jsp
            dispatcher.forward(request, response);
        } catch (Exception e) {
            // Outros erros inesperados
            e.printStackTrace(); // Logar o erro no servidor
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado. Tente novamente.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cadastro.html"); // ou cadastro.jsp
            dispatcher.forward(request, response);
        }
    }
}