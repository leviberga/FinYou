<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Login - FinYou</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg" type="image/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css"> <%-- mesmo CSS da home --%>
    <style>
        body {
            background-color: #ffffff;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #000;
        }
        .login-card {
            background-color: #fff;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
        }
        .login-card h2 {
            text-align: center;
            margin-bottom: 1.5rem;
            font-weight: bold;
        }
        .form-label {
            font-weight: 500;
        }
        .mensagem {
            padding: 0.75rem;
            border-radius: 8px;
            margin-bottom: 1rem;
            font-size: 0.9rem;
        }
        .mensagem.erro {
            background-color: #f8d7da;
            color: #721c24;
        }
        .mensagem.sucesso {
            background-color: #d4edda;
            color: #155724;
        }
        .btn-login {
            width: 100%;
            background-color: #000;
            color: #fff;
            transition: background-color 0.3s;
        }
        .btn-login:hover {
            background-color: #333;
        }
        .small-link {
            display: block;
            text-align: center;
            margin-top: 1rem;
            font-size: 0.9rem;
        }
        .input-hint {
            font-size: 0.8rem;
            color: #555;
            margin-top: 0.25rem;
        }
    </style>
</head>
<body>
    <div class="login-card">
        <h2>Entrar na FinYou</h2>

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
            <div class="mb-3">
                <label for="email" class="form-label">E-mail</label>
                <input type="email" class="form-control" id="email" name="email" value="${param.email}" required>
                <div class="input-hint">Digite o endereço de e-mail cadastrado na FinYou.</div>
            </div>
            <div class="mb-3">
                <label for="senha" class="form-label">Senha</label>
                <input type="password" class="form-control" id="senha" name="senha" required>
                <div class="input-hint">Sua senha é secreta e protege seu acesso.</div>
            </div>
            <button type="submit" class="btn btn-login">Entrar</button>
        </form>

        <a class="small-link" href="${pageContext.request.contextPath}/jsp/Cadastro.jsp">Não tem uma conta? Cadastre-se aqui</a>
    </div>

    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js" async></script>
</body>
</html>
