package br.com.fiap.fintech.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import br.com.fiap.fintech.model.Usuario;
import br.com.fiap.fintech.service.LoginService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginService loginService;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            loginService = new LoginService(); // Instancia o serviço
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Se o usuário acessar /login via GET, apenas mostramos a página de login
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        // Validação simples de entrada
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            request.setAttribute("mensagemErro", "Email e senha são obrigatórios.");
            request.setAttribute("email", email); // Para preencher o campo de email novamente
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            Usuario usuarioAutenticado = loginService.autenticar(email, senha);

            if (usuarioAutenticado != null) {
                // Usuário autenticado com sucesso!
                // 1. Criar/Obter a sessão HTTP
                HttpSession session = request.getSession(true); // true = cria uma nova sessão se não existir

                // 2. Armazenar o objeto Usuario na sessão
                session.setAttribute("usuarioLogado", usuarioAutenticado);

                // 3. Definir um tempo de inatividade máximo para a sessão (opcional, em segundos)
                // session.setMaxInactiveInterval(30 * 60); // 30 minutos

                // 4. Redirecionar para a página principal do usuário (dashboard)
                // Usar sendRedirect para seguir o padrão Post-Redirect-Get
                response.sendRedirect(request.getContextPath() + "/dashboard"); // Ou qualquer página que seja seu dashboard
                System.out.println("Login bem-sucedido para: " + email + ". Redirecionando para dashboard.");

            } else {
                // Falha na autenticação
                request.setAttribute("mensagemErro", "Email ou senha inválidos. Tente novamente.");
                request.setAttribute("email", email); // Para preencher o campo de email novamente
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Login.jsp");
                dispatcher.forward(request, response);
                System.out.println("Falha no login para: " + email);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Logar o erro no servidor
            request.setAttribute("mensagemErro", "Erro no servidor ao tentar fazer login. Tente mais tarde. (SQL)");
            request.setAttribute("email", email);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Login.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // Logar o erro no servidor
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado. Tente novamente.");
            request.setAttribute("email", email);
            RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/Login.jsp");
            dispatcher.forward(request, response);
        }
    }
}