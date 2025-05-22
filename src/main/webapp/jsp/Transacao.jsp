<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title><c:choose>
        <c:when test="${not empty transacao}">Editar Transação</c:when>
        <c:otherwise>Nova Transação</c:otherwise>
    </c:choose> - FinYou</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg" type="image/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    <style>
        body {
            background-color: #ffffff;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #000;
        }
        .form-card {
            background-color: #fff;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 500px;
        }
        .form-card h2 {
            text-align: center;
            margin-bottom: 1.5rem;
            font-weight: bold;
        }
        .form-label {
            font-weight: 500;
        }
        .btn-primary-custom {
            background-color: #000;
            border: none;
            width: 100%;
            color: #fff
        }
        .btn-primary-custom:hover {
            background-color: #333;
        }
        .back-arrow {
            position: fixed;
            top: 20px;
            left: 20px;
            color: #000;
            font-size: 1.2rem;
            font-weight: bold;
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 0.3rem;
            z-index: 1000;
        }
        .back-arrow:hover {
            color: #333;
        }
    </style>
</head>
<body>

<a href="javascript:history.back()" class="back-arrow">&#8592; Voltar</a>

<div class="form-card">
    <h2>
        <c:choose>
            <c:when test="${not empty transacao}">Editar Transação</c:when>
            <c:otherwise>Nova Transação</c:otherwise>
        </c:choose>
    </h2>

    <form action="${pageContext.request.contextPath}/transacao" method="post">
        <input type="hidden" name="acao" value="<c:choose><c:when test='${not empty transacao}'>atualizar</c:when><c:otherwise>inserir</c:otherwise></c:choose>" />
        <c:if test="${not empty transacao}">
            <input type="hidden" name="codigo" value="${transacao.codigo}" />
        </c:if>

        <div class="mb-3">
            <label for="data" class="form-label">Data</label>
            <input type="date" class="form-control" id="data" name="data" value="${transacao.data}" required>
        </div>

        <div class="mb-3">
            <label for="valor" class="form-label">Valor</label>
            <input type="number" class="form-control" id="valor" name="valor" step="0.01" value="${transacao.valor}" required>
        </div>

        <div class="mb-3">
            <label for="tipo" class="form-label">Tipo</label>
            <select class="form-select" id="tipo" name="tipo" required>
                <option value="RECEITA" ${transacao.tipo == 'RECEITA' ? 'selected' : ''}>Receita</option>
                <option value="DESPESA" ${transacao.tipo == 'DESPESA' ? 'selected' : ''}>Despesa</option>
                <option value="TRANSFERENCIA" ${transacao.tipo == 'TRANSFERENCIA' ? 'selected' : ''}>Transferência</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="descricao" class="form-label">Descrição</label>
            <input type="text" class="form-control" id="descricao" name="descricao" value="${transacao.descricao}">
        </div>

        <div class="mb-3">
            <label for="contaOrigem" class="form-label">Conta de Origem</label>
            <input type="number" class="form-control" id="contaOrigem" name="contaOrigem" value="${transacao.codigoContaOrigem}" required>
        </div>

        <div class="mb-3">
            <label for="contaDestino" class="form-label">Conta de Destino</label>
            <input type="number" class="form-control" id="contaDestino" name="contaDestino" value="${transacao.codigoContaDestino}">
        </div>

        <button type="submit" class="btn btn-primary-custom">
            <c:choose>
                <c:when test="${not empty transacao}">Atualizar</c:when>
                <c:otherwise>Cadastrar</c:otherwise>
            </c:choose>
        </button>
    </form>
</div>

<script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js" async></script>
</body>
</html>
