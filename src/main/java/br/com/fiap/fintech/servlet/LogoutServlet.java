package br.com.fiap.fintech.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        response.sendRedirect(request.getContextPath() + "/login.jsp?logout=sucesso");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Para logout via GET (menos comum, mas pode ser útil para links diretos se não houver preocupação com CSRF)
        doPost(request, response);
    }
}