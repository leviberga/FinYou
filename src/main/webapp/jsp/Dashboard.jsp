<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Para formatação de números e datas --%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinYou - Minha dashboard</title>
    <%-- Ajuste os caminhos para seus assets CSS e JS usando pageContext.request.contextPath --%>
    <link rel="shortcut icon" type="imagex/png" href="${pageContext.request.contextPath}/images/LogoFinYou.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/home.css">
    <style>
        .money-value.hidden-value::before {
            content: "****";
        }
        .money-value.hidden-value span {
            display: none;
        }
    </style>
</head>
<body class="bg-light">
<nav class="navbar bg-black">
    <div class="container-fluid d-flex justify-content-between align-items-center">
        <a class="navbar-brand d-flex align-items-center p-2" href="#">
            <img src="${pageContext.request.contextPath}/resources/images/perfil.png" alt="Foto de perfil"
                 class="rounded-circle"
                 style="width: 50px; height: 50px; object-fit: cover;">
            <%-- Exibe o nome do usuário logado --%>
            <span class="ms-3 fs-4 text-white"><c:out value="${nomeUsuario}" default="NOME_NAO_ENCONTRADO"/></span>
        </a>
        <%-- Link de Logout - Idealmente deveria ser um POST para um LogoutServlet --%>
        <a class="navbar-brand logout" href="${pageContext.request.contextPath}/logout"> <%-- Ajuste para seu servlet de logout --%>
            <span class="text-white fs-4">Logout</span>
        </a>
    </div>
</nav>

<div class="container mt-4">
    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger" role="alert">
            <c:out value="${mensagemErro}"/>
        </div>
    </c:if>

    <div class="card mt-3 text-center">
        <div class="card-body">
            <h5 class="card-title fs-3">Saldo Atual</h5>
            <p class="card-text fs-3 fw-bold text-success money-value" data-value="<fmt:formatNumber value="${saldoTotal}" type="currency" currencySymbol="R$ "/>">
                <span><fmt:formatNumber value="${saldoTotal}" type="currency" currencySymbol="R$ "/></span>
            </p>
            <button id="toggleButton" class="btn btn-outline-dark">Esconder Valores</button>
        </div>
    </div>
    <div class="row g-3">
        <div class="col-md-6 mt-4">
            <div class="card text-center">
                <div class="card-body">
                    <h5 class="card-title">Ganhos</h5>
                    <p class="card-text fs-4 text-success money-value" data-value="<fmt:formatNumber value="${totalGanhos}" type="currency" currencySymbol="R$ "/>">
                        <span><fmt:formatNumber value="${totalGanhos}" type="currency" currencySymbol="R$ "/></span>
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-6 mt-4">
            <div class="card text-center">
                <div class="card-body">
                    <h5 class="card-title">Gastos</h5>
                    <p class="card-text fs-4 text-danger money-value" data-value="<fmt:formatNumber value="${totalGastos}" type="currency" currencySymbol="R$ "/>">
                        <span><fmt:formatNumber value="${totalGastos}" type="currency" currencySymbol="R$ "/></span>
                    </p>
                </div>
            </div>
        </div>
    </div>
    <div class="mt-4">
        <h5 class="text-center fs-4">Transações Recentes</h5>
    </div>
    <div class="card mt-3">
        <div class="card-body">
            <h5 class="card-title">Últimas Transações</h5>
            <ul class="list-group list-group-flush">
                <c:choose>
                    <c:when test="${not empty transacoesRecentes}">
                        <c:forEach var="transacao" items="${transacoesRecentes}">
                            <li class="list-group-item d-flex justify-content-between">
                                <span><c:out value="${transacao.descricao}"/></span>
                                <c:choose>
                                    <c:when test="${transacao.tipo == 'RECEITA'}">
                                        <span class="text-success">+ <fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/></span>
                                    </c:when>
                                    <c:when test="${transacao.tipo == 'DESPESA' || transacao.tipo == 'TRANSFERENCIA'}"> <%-- Assumindo que transferencia é uma saída para a conta de origem --%>
                                        <span class="text-danger">- <fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span><fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/></span>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="list-group-item">Nenhuma transação recente encontrada.</li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js" async></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const toggleButton = document.getElementById("toggleButton");
        if (toggleButton) {
            toggleButton.addEventListener("click", function () {
                const values = document.querySelectorAll(".money-value");
                const button = this;
                // Verifica se o primeiro span visível tem conteúdo ou se o data-value foi aplicado
                const firstValueSpan = values[0] ? values[0].querySelector('span') : null;
                const areValuesCurrentlyVisible = firstValueSpan ? (firstValueSpan.textContent.trim() !== '****' && firstValueSpan.style.display !== 'none') : (values[0] && values[0].textContent.trim() !== '****');


                values.forEach(valueElement => {
                    const span = valueElement.querySelector('span');
                    if (areValuesCurrentlyVisible) {
                        if(span) span.style.display = 'none';
                        valueElement.classList.add('hidden-value'); // Adiciona classe para mostrar '****' via CSS
                    } else {
                        if(span) span.style.display = ''; // ou 'inline' ou o display original
                        valueElement.classList.remove('hidden-value');
                    }
                });
                button.textContent = areValuesCurrentlyVisible ? "Mostrar Valores" : "Esconder Valores";
            });
        }
    });
</script>
</body>
</html>