<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <title>Erro - FinYou</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/LogoFinYou.svg" type="image/x-icon" />
    <style>
        /* Reset básico */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body, html {
            height: 100%;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #fff;
            color: #000;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
            padding: 20px;
        }

        .error-container {
            max-width: 400px;
            border: 2px solid #000;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }

        .error-code {
            font-size: 5rem;
            font-weight: 900;
            margin-bottom: 1rem;
            letter-spacing: 4px;
        }

        .error-message {
            font-size: 1.25rem;
            margin-bottom: 2rem;
        }

        .back-link {
            display: inline-block;
            padding: 0.75rem 1.5rem;
            border: 2px solid #000;
            border-radius: 8px;
            text-decoration: none;
            color: #000;
            font-weight: 600;
            transition: background-color 0.3s, color 0.3s;
        }

        .back-link:hover {
            background-color: #000;
            color: #fff;
        }

        @media (max-width: 480px) {
            .error-code {
                font-size: 3.5rem;
            }
            .error-container {
                max-width: 90%;
                padding: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-code">Oops!</div>
        <div class="error-message">
            Algo deu errado.<br />
            Por favor, tente novamente mais tarde.
        </div>
        <a href="${pageContext.request.contextPath}/jsp/index.jsp" class="back-link">Voltar para o menu</a>
    </div>
</body>
</html>
