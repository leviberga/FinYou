<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinYou - Minha dashboard</title>
    <link rel="shortcut icon" type="imagex/png" href="${pageContext.request.contextPath}/images/LogoFinYou.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/transacoes-recentes.css">
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
        <div class="d-flex align-items-center">
            <a class="navbar-brand d-flex align-items-center p-2" href="#">
                <span class="ms-3 fs-4 text-white">
                    <c:out value="${nomeUsuario}" default="Nome não encontrado"/>
                </span>
            </a>
            <a class="nav-link logout text-white ms-4 fs-5" href="${pageContext.request.contextPath}/transacao">Transações</a>
        </div>
        <a class="navbar-brand logout" href="${pageContext.request.contextPath}/logout">
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

    <!-- Saldo Atual -->
    <div class="card mt-3 text-center">
        <div class="card-body">
            <h5 class="card-title fs-3">Saldo Atual</h5>
            <p class="card-text fs-3 fw-bold text-success money-value" data-value="<fmt:formatNumber value="${saldoTotal}" type="currency" currencySymbol="R$ "/>">
                <span><fmt:formatNumber value="${saldoTotal}" type="currency" currencySymbol="R$ "/></span>
            </p>
            <button id="toggleButton" class="btn btn-outline-dark">Esconder Valores</button>
        </div>
    </div>

    <!-- Ganhos e Gastos -->
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

    <!-- Transações Recentes -->
    <div class="mt-4">
        <h5 class="text-center fs-4">Transações Recentes</h5>
    </div>

    <div class="card mt-3 transacoes-card">
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5 class="card-title mb-0">Últimas Transações</h5>
                <div class="transacoes-navigation">
                    <button id="btnAnterior" class="btn btn-outline-secondary btn-sm" disabled>
                        <i class="fas fa-chevron-left"></i> Anterior
                    </button>
                    <span id="paginaInfo" class="mx-2 text-muted">1 de 1</span>
                    <button id="btnProximo" class="btn btn-outline-secondary btn-sm" disabled>
                        Próximo <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
            </div>

            <div id="transacoes-container">
                <div class="transacoes-list">
                    <c:choose>
                        <c:when test="${not empty transacoesRecentes}">
                            <c:forEach var="transacao" items="${transacoesRecentes}" varStatus="status">
                                <div class="transacao-item ${status.index >= 5 ? 'hidden' : ''}" data-page="${status.index div 5 + 1}">
                                    <div class="transacao-info">
                                        <div class="transacao-descricao">
                                            <strong><c:out value="${transacao.descricao}"/></strong>
                                        </div>
                                        <div class="transacao-data text-muted">
                                            <fmt:formatDate value="${transacao.dataComoDate}" pattern="dd/MM/yyyy"/>

                                        </div>
                                    </div>
                                    <div class="transacao-valor">
                                        <c:choose>
                                            <c:when test="${transacao.tipo == 'RECEITA'}">
                                                <span class="valor-positivo money-value" data-value="+ <fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/>">
                                                    <span>+ <fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/></span>
                                                </span>
                                            </c:when>
                                            <c:when test="${transacao.tipo == 'DESPESA' || transacao.tipo == 'TRANSFERENCIA'}">
                                                <span class="valor-negativo money-value" data-value="- <fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/>">
                                                    <span>- <fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/></span>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="valor-neutro money-value" data-value="<fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/>">
                                                    <span><fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ "/></span>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="transacao-tipo text-muted">
                                            <small><c:out value="${transacao.tipo}"/></small>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="transacao-vazia text-center py-4">
                                <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                <p class="text-muted">Nenhuma transação recente encontrada.</p>
                                <a href="${pageContext.request.contextPath}/transacao" class="btn btn-primary">
                                    Adicionar Transação
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <c:if test="${not empty transacoesRecentes && transacoesRecentes.size() > 5}">
                <div class="transacoes-pagination mt-3">
                    <div class="d-flex justify-content-center">
                        <nav aria-label="Paginação das transações">
                            <ul class="pagination pagination-sm" id="paginacao">
                                <!-- Páginas serão geradas via JavaScript -->
                            </ul>
                        </nav>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js" async></script>
<script src="${pageContext.request.contextPath}/resources/js/transacoes-recentes.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Toggle para esconder/mostrar valores
        const toggleButton = document.getElementById("toggleButton");
        if (toggleButton) {
            toggleButton.addEventListener("click", function () {
                const values = document.querySelectorAll(".money-value");
                const button = this;
                const firstValueSpan = values[0] ? values[0].querySelector('span') : null;
                const areValuesCurrentlyVisible = firstValueSpan ? (firstValueSpan.textContent.trim() !== '****' && firstValueSpan.style.display !== 'none') : (values[0] && values[0].textContent.trim() !== '****');

                values.forEach(valueElement => {
                    const span = valueElement.querySelector('span');
                    if (areValuesCurrentlyVisible) {
                        if(span) span.style.display = 'none';
                        valueElement.classList.add('hidden-value');
                    } else {
                        if(span) span.style.display = '';
                        valueElement.classList.remove('hidden-value');
                    }
                });
                button.textContent = areValuesCurrentlyVisible ? "Mostrar Valores" : "Esconder Valores";
            });
        }

        // Inicializar paginação das transações
        if (typeof initTransacoesPagination === 'function') {
            initTransacoesPagination();
        }
    });
</script>
</body>
</html>