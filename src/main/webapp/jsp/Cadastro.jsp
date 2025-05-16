<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Novo Usuário</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>Cadastro de Novo Usuário</h1>

<c:if test="${not empty mensagemErro}">
    <p style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 15px;">
            ${mensagemErro}
    </p>
</c:if>
<c:if test="${not empty mensagemSucesso}">
    <p style="color: green; border: 1px solid green; padding: 10px; margin-bottom: 15px;">
            ${mensagemSucesso}
    </p>
</c:if>

<form action="${pageContext.request.contextPath}/cadastro" method="post">
    <div>
        <label for="nomeCompleto">Nome Completo:</label>
        <input type="text" id="nomeCompleto" name="nomeCompleto" value="${param.nomeCompleto}" required>
    </div>
    <div>
        <label for="cpf">CPF:</label>
        <input type="text" id="cpf" name="cpf" value="${param.cpf}" required placeholder="000.000.000-00">
    </div>
    <div>
        <label for="dataNascimento">Data de Nascimento:</label>
        <input type="date" id="dataNascimento" name="dataNascimento" value="${param.dataNascimento}" required>
    </div>
    <div>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${param.email}" required>
    </div>
    <div>
        <label for="senha">Senha:</label>
        <input type="password" id="senha" name="senha" required>
    </div>
    <div>
        <label for="confirmarSenha">Confirmar Senha:</label>
        <input type="password" id="confirmarSenha" name="confirmarSenha" required>
    </div>
    <div>
        <button type="submit">Cadastrar</button>
    </div>
</form>
<p><a href="${pageContext.request.contextPath}/login.jsp">Já tem uma conta? Faça login</a></p>
</body>
</html>