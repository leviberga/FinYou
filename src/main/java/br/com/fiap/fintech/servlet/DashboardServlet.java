package br.com.fiap.fintech.servlet; // Certifique-se que este é seu pacote correto

// Escolha o namespace correto (javax ou jakarta) e remova o outro
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
// OU
// import jakarta.servlet.RequestDispatcher;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;

import br.com.fiap.fintech.model.Usuario;
import br.com.fiap.fintech.service.DashboardService;

import java.io.IOException;
import java.util.Map;
// Importe um logger se for usar (ex: SLF4J)
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // private static final Logger logger = LoggerFactory.getLogger(DashboardServlet.class); // Exemplo com SLF4J

    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            dashboardService = new DashboardService(); // O construtor do Service já trata seus erros
            // logger.info("DashboardServlet inicializado com DashboardService.");
        } catch (RuntimeException e) {
            // logger.error("Falha CRÍTICA ao instanciar DashboardService no init() do DashboardServlet.", e);
            e.printStackTrace(); // Mantenha para desenvolvimento
            throw new ServletException("Não foi possível inicializar o serviço de dashboard. A aplicação pode não funcionar corretamente.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // logger.debug("DashboardServlet.doGet() chamado para URI: {}", request.getRequestURI());
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogado") == null) {
            // logger.warn("Sessão não encontrada ou usuário não logado. Redirecionando para login.");
            // Ajuste o caminho para o seu servlet de login ou página JSP de login
            response.sendRedirect(request.getContextPath() + "/login?erro=naoLogado"); // Preferencialmente para o servlet de login
            return;
        }

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        // logger.debug("Usuário recuperado da sessão: {}", (usuarioLogado != null ? usuarioLogado.getEmail() : "null"));

        try {
            if (dashboardService == null) {
                // logger.error("DashboardService é NULO em doGet! Falha crítica na inicialização.");
                request.setAttribute("mensagemErroGeral", "Erro interno crítico do servidor (DS_INIT_FAIL). Por favor, tente novamente mais tarde.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/erro.jsp");
                dispatcher.forward(request, response);
                return;
            }

            Map<String, Object> dadosDashboard = dashboardService.carregarDadosDashboard(usuarioLogado);
            // logger.debug("Dados do dashboard carregados do service. Quantidade de itens: {}", (dadosDashboard != null ? dadosDashboard.size() : "mapa null"));

            if (dadosDashboard != null) {
                for (Map.Entry<String, Object> entry : dadosDashboard.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            } else {
                // logger.error("Mapa dadosDashboard retornado pelo service é NULO!");
                // Isso indicaria um problema sério no DashboardService que não lançou exceção
                request.setAttribute("mensagemErroGeral", "Não foi possível carregar os dados do dashboard (SERVICE_NULL_DATA).");
                // Considerar preencher atributos com valores default seguros aqui se for continuar para o JSP
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Dashboard.jsp");
            dispatcher.forward(request, response);

        } catch (IllegalArgumentException e) {
            // logger.warn("Argumento ilegal ao carregar dados do dashboard (ex: usuário inválido).", e);
            e.printStackTrace(); // Mantenha para desenvolvimento
            response.sendRedirect(request.getContextPath() + "/login?erro=argumentoInvalido");
        } catch (RuntimeException e) {
            // logger.error("Erro de Runtime (inesperado ou vindo do service) ao processar dashboard.", e);
            e.printStackTrace(); // Mantenha para desenvolvimento
            request.setAttribute("mensagemErroGeral", "Ocorreu um erro inesperado ao carregar seus dados. Tente novamente mais tarde.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/erro.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // logger.debug("DashboardServlet.doPost() chamado, redirecionando para doGet.");
        doGet(request, response);
    }

    @Override
    public void destroy() {
        // logger.info("DashboardServlet.destroy(): Sendo destruído.");
        super.destroy();
        // Qualquer limpeza de recursos do service, se necessário (improvável neste caso)
    }
}