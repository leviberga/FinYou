package br.com.fiap.fintech.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Pega a sessão existente, não cria uma nova

        if (session != null) {
            session.removeAttribute("usuarioLogado"); // Remove o atributo do usuário
            session.invalidate(); // Invalida a sessão completamente
            System.out.println("Sessão invalidada, logout realizado.");
        }

        // Redireciona para a página de login com uma mensagem de sucesso de logout
        response.sendRedirect(request.getContextPath() + "/jsp/Login.jsp?logout=sucesso");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Para logout via GET (menos comum, mas pode ser útil para links diretos se não houver preocupação com CSRF)
        doPost(request, response);
    }
}