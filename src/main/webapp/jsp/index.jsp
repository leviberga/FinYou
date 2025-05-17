<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinYou - Financie Você</title>
    <%-- Caminhos para CSS, JS e Imagens ajustados --%>
    <link rel="shortcut icon" type="imagex/png" href="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark"> <%-- Adicionado navbar-dark bg-dark para melhor contraste --%>
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index">
            <img src="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg" alt="Logo FinYou"
                 width="40" height="40" class="d-inline-block align-top rounded-circle me-2"> <%-- Ajustes de tamanho e margem --%>
            FinYou
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/jsp/Login.jsp">Entrar</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/jsp/Cadastro.jsp">Cadastre-se</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- Carousel -->
<div id="carouselExample" class="carousel slide" data-bs-ride="carousel">
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="${pageContext.request.contextPath}/resources/images/pessoamexendonocelular.jpg" class="d-block w-100" alt="Finanças Simplificadas" style="max-height: 500px; object-fit: cover;"> <%-- Estilo para altura máxima e ajuste --%>
            <div class="carousel-caption d-none d-md-block">
                <h3>Controle suas Finanças</h3>
                <p>De forma simples e eficiente.</p>
            </div>
        </div>
        <div class="carousel-item">
            <img src="${pageContext.request.contextPath}/resources/images/organizacao.jpg" class="d-block w-100" alt="Organização" style="max-height: 500px; object-fit: cover;">
            <div class="carousel-caption d-none d-md-block">
                <h3>Organize seus Gastos</h3>
                <p>Planeje seu futuro financeiro.</p>
            </div>
        </div>
        <div class="carousel-item">
            <img src="${pageContext.request.contextPath}/resources/images/pessoasreunidas.jpg" class="d-block w-100" alt="Metas" style="max-height: 500px; object-fit: cover;">
            <div class="carousel-caption d-none d-md-block">
                <h3>Defina Metas</h3>
                <p>Alcance seus objetivos mais rápido.</p>
            </div>
        </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
    </button>
</div>

<!-- Vantagens -->
<section id="vantagens" class="py-5">
    <div class="container text-center">
        <h2 class="mb-4">Por que usar a FinYou?</h2>
        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">Controle Total</h5>
                        <p class="card-text">Gerencie todas as suas contas em um só lugar, de forma rápida e prática.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">Segurança</h5>
                        <p class="card-text">Seus dados financeiros estão seguros com nossa tecnologia de ponta.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">Metas Financeiras</h5>
                        <p class="card-text">Defina objetivos e acompanhe seu progresso diariamente.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Contato -->
<section id="contato" class="py-5 bg-light">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8 text-center">
                <h2 class="mb-3">Fale Conosco</h2>
                <p class="lead mb-4">Envie-nos uma mensagem e tire suas dúvidas!</p>
                <%-- Para o formulário de contato funcionar, precisaria de um action e method, e um servlet para processar --%>
                <form>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <input type="text" class="form-control form-control-lg" name="nomeContato" placeholder="Seu Nome" required>
                        </div>
                        <div class="col-md-6">
                            <input type="email" class="form-control form-control-lg" name="emailContato" placeholder="Seu E-mail" required>
                        </div>
                    </div>
                    <div class="mt-3">
                        <textarea class="form-control form-control-lg" name="mensagemContato" rows="5" placeholder="Sua Mensagem" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary btn-lg mt-4">Enviar Mensagem</button>
                </form>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="text-center py-4 bg-dark text-white">
    <div class="container">
        <p class="mb-0">© 2024 FinYou. Todos os direitos reservados.</p>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js" async></script>
</body>
</html>