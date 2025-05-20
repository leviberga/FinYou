<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastro - FinYou</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg" type="image/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css"> <%-- mesmo CSS da home/login --%>
    <style>
        body {
            background-color: #ffffff;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #000;
        }
        .cadastro-card {
            background-color: #fff;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 500px;
        }
        .cadastro-card h2 {
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
        .btn-cadastrar {
            width: 100%;
            background-color: #000;
            color: #fff;
            transition: background-color 0.3s;
        }
        .btn-cadastrar:hover {
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
<div class="cadastro-card">
    <h2>Crie sua conta</h2>

    <c:if test="${not empty mensagemErro}">
        <p class="mensagem erro">${mensagemErro}</p>
    </c:if>
    <c:if test="${not empty mensagemSucesso}">
        <p class="mensagem sucesso">${mensagemSucesso}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/cadastro" method="post">
        <div class="mb-3">
            <label for="nomeCompleto" class="form-label">Nome completo</label>
            <input type="text" class="form-control" id="nomeCompleto" name="nomeCompleto" value="${param.nomeCompleto}" required>
            <div class="input-hint">Ex: João da Silva</div>
        </div>
        <div class="mb-3">
            <label for="cpf" class="form-label">CPF</label>
            <input type="text" class="form-control" id="cpf" name="cpf" value="${param.cpf}" placeholder="000.000.000-00" required>
            <div class="input-hint">Digite apenas números ou com pontuação</div>
        </div>
        <div class="mb-3">
            <label for="dataNascimento" class="form-label">Data de nascimento</label>
            <input type="date" class="form-control" id="dataNascimento" name="dataNascimento" value="${param.dataNascimento}" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">E-mail</label>
            <input type="email" class="form-control" id="email" name="email" value="${param.email}" required>
            <div class="input-hint">Você usará este e-mail para fazer login</div>
        </div>
        <div class="mb-3">
            <label for="senha" class="form-label">Senha</label>
            <input type="password" class="form-control" id="senha" name="senha" required>
            <div class="input-hint">Use uma senha segura</div>
        </div>
        <div class="mb-3">
            <label for="confirmarSenha" class="form-label">Confirmar senha</label>
            <input type="password" class="form-control" id="confirmarSenha" name="confirmarSenha" required>
        </div>
        <button type="submit" class="btn btn-cadastrar">Cadastrar</button>
    </form>

    <a class="small-link" href="${pageContext.request.contextPath}/jsp/Login.jsp">Já tem uma conta? Faça login</a>
</div>

<script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js" async></script>
</body>
</html>
