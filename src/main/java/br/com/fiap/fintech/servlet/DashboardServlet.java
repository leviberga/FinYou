package br.com.fiap.fintech.servlet;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import br.com.fiap.fintech.model.Usuario;
import br.com.fiap.fintech.service.DashboardService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
   
    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            dashboardService = new DashboardService(); 
        } catch (RuntimeException e) {
           
            e.printStackTrace(); 
            throw new ServletException("Não foi possível inicializar o serviço de dashboard. A aplicação pode não funcionar corretamente.", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogado") == null) {
            
            response.sendRedirect(request.getContextPath() + "/login?erro=naoLogado"); 
            return;
        }

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
       
        try {
            if (dashboardService == null) {
               
                request.setAttribute("mensagemErroGeral", "Erro interno crítico do servidor (DS_INIT_FAIL). Por favor, tente novamente mais tarde.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/erro.jsp");
                dispatcher.forward(request, response);
                return;
            }

            Map<String, Object> dadosDashboard = dashboardService.carregarDadosDashboard(usuarioLogado);
           

            if (dadosDashboard != null) {
                for (Map.Entry<String, Object> entry : dadosDashboard.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            } else {
                
                request.setAttribute("mensagemErroGeral", "Não foi possível carregar os dados do dashboard (SERVICE_NULL_DATA).");
               
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/Dashboard.jsp");
            dispatcher.forward(request, response);

        } catch (IllegalArgumentException e) {
            
            e.printStackTrace(); 
            response.sendRedirect(request.getContextPath() + "/login?erro=argumentoInvalido");
        } catch (RuntimeException e) {
           
            e.printStackTrace();
            request.setAttribute("mensagemErroGeral", "Ocorreu um erro inesperado ao carregar seus dados. Tente novamente mais tarde.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/erro.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        doGet(request, response);
    }

    @Override
    public void destroy() {
       
        super.destroy();
      
    }



}
