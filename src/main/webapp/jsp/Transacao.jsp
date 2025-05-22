<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><c:choose>
        <c:when test="${not empty transacao}">Editar Transação</c:when>
        <c:otherwise>Nova Transação</c:otherwise>
    </c:choose></title>
</head>
<body>

<h2>
    <c:choose>
        <c:when test="${not empty transacao}">Editar Transação</c:when>
        <c:otherwise>Nova Transação</c:otherwise>
    </c:choose>
</h2>

<form action="transacao" method="post">
    <input type="hidden" name="acao" value="<c:choose><c:when test='${not empty transacao}'>atualizar</c:when><c:otherwise>inserir</c:otherwise></c:choose>"/>

    <c:if test="${not empty transacao}">
        <input type="hidden" name="codigo" value="${transacao.codigo}"/>
    </c:if>

    <label for="data">Data:</label>
    <input type="date" id="data" name="data" value="${transacao.data}" required><br><br>

    <label for="valor">Valor:</label>
    <input type="number" id="valor" name="valor" step="0.01" value="${transacao.valor}" required><br><br>

    <label for="tipo">Tipo:</label>
    <select id="tipo" name="tipo" required>
        <option value="ENTRADA" ${transacao.tipo == 'ENTRADA' ? 'selected' : ''}>Entrada</option>
        <option value="SAIDA" ${transacao.tipo == 'SAIDA' ? 'selected' : ''}>Saída</option>
        <option value="TRANSFERENCIA" ${transacao.tipo == 'TRANSFERENCIA' ? 'selected' : ''}>Transferência</option>
    </select><br><br>

    <label for="descricao">Descrição:</label>
    <input type="text" id="descricao" name="descricao" value="${transacao.descricao}"><br><br>

    <label for="contaOrigem">Conta de Origem:</label>
    <input type="number" id="contaOrigem" name="contaOrigem" value="${transacao.codigoContaOrigem}" required><br><br>

    <label for="contaDestino">Conta de Destino:</label>
    <input type="number" id="contaDestino" name="contaDestino" value="${transacao.codigoContaDestino}"><br><br>

    <button type="submit">
        <c:choose>
            <c:when test="${not empty transacao}">Atualizar</c:when>
            <c:otherwise>Cadastrar</c:otherwise>
        </c:choose>
    </button>
</form>

<br>
<a href="javascript:history.back()">Voltar para lista</a>

</body>
</html>
