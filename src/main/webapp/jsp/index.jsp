<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinYou - Financie Você</title>
    <link rel="shortcut icon" type="imagex/png" href="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    <style>
        .nav-item:hover {
            transition: all 0.5s ease-in-out;
            font-weight: 900;
            text-shadow: 0 0 8px var(--light-color);
        }

        /* Estilos específicos para o navbar responsivo */
        .navbar-toggler {
            border-color: rgba(255, 255, 255, 0.3);
            padding: 0.25rem 0.5rem;
        }

        .navbar-toggler:focus {
            box-shadow: 0 0 0 0.25rem rgba(255, 255, 255, 0.25);
        }

        .navbar-toggler-icon {
            background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%28255, 255, 255, 0.75%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
        }

        /* Melhorar a aparência do menu mobile */
        @media (max-width: 991.98px) {
            .navbar-collapse {
                background-color: rgba(0, 0, 0, 0.9);
                border-radius: 8px;
                margin-top: 10px;
                padding: 15px;
            }

            .navbar-nav {
                text-align: center;
            }

            .nav-item {
                margin: 5px 0;
            }

            .nav-link {
                padding: 10px 15px !important;
                border-radius: 5px;
                transition: background-color 0.3s ease;
            }

            .nav-link:hover {
                background-color: rgba(255, 255, 255, 0.1);
            }
        }

        /* Garantir que o logo não quebre em telas pequenas */
        .navbar-brand {
            display: flex;
            align-items: center;
            white-space: nowrap;
        }

        .navbar-brand span {
            margin-left: 10px;
        }

        @media (max-width: 576px) {
            .navbar-brand span {
                font-size: 1.1rem;
            }

            .navbar-brand img {
                width: 45px;
                height: 45px;
            }
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand text-white" href="#">
                <img src="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg" alt="Logo FinYou"
                     class="rounded-circle"
                     style="width: 50px; height: 50px; object-fit: cover;">
                <span class="text-white">FinYou</span>
            </a>

            <!-- Botão hamburger para mobile -->
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <!-- Menu collapse -->
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/jsp/Login.jsp">Entrar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/jsp/Cadastro.jsp">Cadastre-se</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Carousel -->
    <div id="carouselExample" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="${pageContext.request.contextPath}/resources/images/pessoamexendonocelular.jpg" class="d-block w-100" alt="Finanças Simplificadas">
                <div class="carousel-caption">
                    <h3>Controle suas Finanças</h3>
                    <p>De forma simples e eficiente.</p>
                </div>
            </div>
            <div class="carousel-item">
                <img src="${pageContext.request.contextPath}/resources/images/organizacao.jpg" class="d-block w-100" alt="Organização">
                <div class="carousel-caption">
                    <h3>Organize seus Gastos</h3>
                    <p>Planeje seu futuro financeiro.</p>
                </div>
            </div>
            <div class="carousel-item">
                <img src="${pageContext.request.contextPath}/resources/images/pessoasreunidas.jpg" class="d-block w-100" alt="Metas">
                <div class="carousel-caption">
                    <h3>Defina Metas</h3>
                    <p>Alcance seus objetivos mais rápido.</p>
                </div>
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
        </button>
    </div>

    <!-- Vantagens -->
    <section id="vantagens" class="mt-5">
        <div class="container text-center">
            <h2 class="mb-4">Por que usar a FinYou?</h2>
            <div class="row">
                <div class="col-md-4">
                    <div class="card p-3">
                        <h5 class="card-title">Controle Total</h5>
                        <p class="card-text">Gerencie todas as suas contas em um só lugar, de forma rápida e prática.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card p-3">
                        <h5 class="card-title">Segurança</h5>
                        <p class="card-text">Seus dados financeiros estão seguros com nossa tecnologia de ponta.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card p-3">
                        <h5 class="card-title">Metas Financeiras</h5>
                        <p class="card-text">Defina objetivos e acompanhe seu progresso diariamente.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Contato -->
    <section id="contato" class="mt-5">
        <div class="container text-center">
            <h2>Fale Conosco</h2>
            <p>Envie-nos uma mensagem e tire suas dúvidas!</p>
            <form>
                <div class="row g-3">
                    <div class="col-md-6">
                        <input type="text" class="form-control" placeholder="Seu Nome" required>
                    </div>
                    <div class="col-md-6">
                        <input type="email" class="form-control" placeholder="Seu E-mail" required>
                    </div>
                </div>
                <div class="mt-3">
                    <textarea class="form-control" rows="4" placeholder="Sua Mensagem" required></textarea>
                </div>
                <button type="submit" class="btn btn-custom mt-3 mb-4">Enviar</button>
            </form>
        </div>
    </section>

    <!-- Footer -->
    <footer>
        <p>&copy; 2024 FinYou. Todos os direitos reservados.</p>
    </footer>

    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js"></script>
</body>
</html>