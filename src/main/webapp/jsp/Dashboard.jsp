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

        /* Estilos para melhor exibição de todas as transações */
        .transacoes-list {
            max-height: none; /* Remove limite de altura */
        }

        .transacao-item {
            display: flex !important; /* Força exibição */
            margin-bottom: 10px;
            padding: 12px;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            background-color: #f8f9fa;
        }

        .transacao-item:hover {
            background-color: #e9ecef;
            transition: background-color 0.2s;
        }

        /* Estilo para mensagem de sucesso */
        .alert-success {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1050;
            min-width: 300px;
            animation: slideIn 0.5s ease-out;
        }

        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
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
    <!-- Mensagem de sucesso -->
    <c:if test="${not empty param.mensagem}">
        <div id="mensagemSucesso" class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>Sucesso!</strong> <c:out value="${param.mensagem}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

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
        <h5 class="text-center fs-4">Todas as Transações</h5>
    </div>

    <div class="card mt-3 transacoes-card">
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5 class="card-title mb-0">
                    Transações
                    <c:if test="${not empty transacoesRecentes}">
                        <small class="text-muted">(${transacoesRecentes.size()} total)</small>
                    </c:if>
                </h5>
                <!-- Removido elemento de navegação pois não precisamos mais -->
            </div>

            <div id="transacoes-container">
                <div class="transacoes-list">
                    <c:choose>
                        <c:when test="${not empty transacoesRecentes}">
                            <c:forEach var="transacao" items="${transacoesRecentes}" varStatus="status">
                                <div class="transacao-item" data-index="${status.index}">
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
                                <p class="text-muted">Nenhuma transação encontrada.</p>
                                <a href="${pageContext.request.contextPath}/transacao" class="btn btn-primary">
                                    Adicionar Transação
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Removido elemento de paginação -->
        </div>
    </div>
</div>

<!-- Scripts com caminhos corrigidos -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/transacoes-recentes.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        console.log('DOM carregado - inicializando dashboard');

        // Verifica se precisa fazer refresh (quando vem de uma transação)
        const urlParams = new URLSearchParams(window.location.search);
        const shouldRefresh = urlParams.get('refresh');

        if (shouldRefresh === 'true') {
            // Remove o parâmetro refresh da URL sem recarregar a página
            const newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
            window.history.replaceState({path: newUrl}, '', newUrl);

            // Força atualização dos dados se necessário
            setTimeout(() => {
                // Você pode adicionar aqui uma chamada AJAX para recarregar os dados
                // ou simplesmente confiar que o servlet já forneceu os dados atualizados
                console.log('Dashboard atualizado após transação');
            }, 100);
        }

        // Auto-hide da mensagem de sucesso após 5 segundos
        const mensagemSucesso = document.getElementById('mensagemSucesso');
        if (mensagemSucesso) {
            setTimeout(() => {
                mensagemSucesso.style.animation = 'slideOut 0.5s ease-in forwards';
                setTimeout(() => {
                    mensagemSucesso.style.display = 'none';
                }, 500);
            }, 5000);
        }

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

        // Inicializar exibição das transações (sem paginação)
        setTimeout(() => {
            console.log('Tentando inicializar transações...');
            if (typeof initTransacoesPagination === 'function') {
                initTransacoesPagination();
                console.log('Transações inicializadas com sucesso');
            } else {
                console.error('Função initTransacoesPagination não encontrada');
                // Fallback: mostrar todas as transações diretamente
                const transacoes = document.querySelectorAll('.transacao-item');
                transacoes.forEach(transacao => {
                    transacao.style.display = 'flex';
                });
                console.log(`Fallback aplicado - ${transacoes.length} transações exibidas`);
            }
        }, 100);
    });

    // CSS adicional para animação de saída
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideOut {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(100%);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);
</script>
</body>
</html>