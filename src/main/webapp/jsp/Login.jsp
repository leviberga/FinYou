<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - FINYOU</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo_login.css"> <%-- ADICIONAR CSS --%>
</head>
<body>
<div class="container login-container">
    <h1>Login</h1>

    <c:if test="${not empty mensagemErro}">
        <p class="mensagem erro">${mensagemErro}</p>
    </c:if>
    <c:if test="${not empty param.mensagem && param.mensagem == 'cadastroSucesso'}">
        <p class="mensagem sucesso">Cadastro realizado com sucesso! Por favor, faça o login.</p>
    </c:if>
    <c:if test="${not empty param.erro && param.erro == 'naologado'}">
        <p class="mensagem erro">Você precisa estar logado para acessar esta página.</p>
    </c:if>
    <c:if test="${not empty param.logout && param.logout == 'sucesso'}">
        <p class="mensagem sucesso">Logout realizado com sucesso!</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${param.email}" required>
        </div>
        <div>
            <label for="senha">Senha:</label>
            <input type="password" id="senha" name="senha" required>
        </div>
        <div>
            <button type="submit">Entrar</button>
        </div>
    </form>
    <p>Não tem uma conta? <a href="${pageContext.request.contextPath}/jsp/Cadastro.jsp">Cadastre-se aqui</a></p>
</div>
</body>
</html>