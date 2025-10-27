# 💰 FinYou — Controle Financeiro Pessoal

FinYou é uma aplicação web desenvolvida em **Java** que ajuda no controle de finanças pessoais, permitindo o **cadastro de usuários**, **registro de receitas e despesas**, e **visualização do saldo e histórico financeiro** de forma simples e intuitiva.

---

## 📋 Sumário

- [Funcionalidades](#-funcionalidades)
- [Tecnologias utilizadas](#-tecnologias-utilizadas)
- [Arquitetura do projeto](#-arquitetura-do-projeto)
- [Demonstração](#-demonstração)
- [Como executar o projeto](#-como-executar-o-projeto)
- [Roadmap futuro](#-roadmap-futuro)
- [Autores](#-autores)
- [Licença](#-licença)

---

## 🎯 Funcionalidades

✅ Cadastro e login de usuários  
✅ Inserção de receitas e despesas  
✅ Cálculo automático do saldo atual  
✅ Histórico detalhado por categoria  
✅ Dashboard visual (resumo financeiro)  
✅ Persistência em memória (versão atual)  
✅ Interface simples e responsiva  

---

## 🧩 Tecnologias utilizadas

**Backend**
- Java 17  
- Servlets / JSP  
- JDBC  
- Oracle Database  
- Apache Tomcat  

**Frontend**
- HTML  
- CSS  
- Bootstrap  

**Paradigmas e conceitos**
- Programação Orientada a Objetos (POO)  
- MVC (Model–View–Controller)  
- Separação de camadas (Controller, DAO, Model)  

---

## 🏗️ Arquitetura do projeto

Estrutura base da aplicação:

```
FinYou/
 ├ src/
 │   ├ controller/        # Servlets de controle
 │   ├ dao/               # Classes de acesso a dados
 │   ├ model/             # Classes de domínio (usuário, transação, etc)
 │   └ util/              # Classes utilitárias
 │
 └ webapp/
     ├ jsp/               # Páginas JSP
     ├ css/               # Estilos e layout
     └ js/                # Scripts auxiliares
```

---

## 🎬 Demonstração

> As demonstrações abaixo mostram as principais telas da aplicação.  

### 🏠 Tela inicial  
![Tela Inicial](https://i.imgur.com/PldG1sx.gif)

### 🔐 Login e Cadastro  
![Login](https://github.com/user-attachments/assets/a83fa7ae-600a-41f8-896c-819cc93ec58e)  
![Cadastro](https://github.com/user-attachments/assets/8cfa408c-5472-4c7d-9943-14aedf404ed2)


### 📊 Dashboard / Tela principal  
![Dashboard](https://i.imgur.com/0L56Twd.gif)


## 🚀 Como executar o projeto

### Pré-requisitos
- [Java JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Tomcat 10+](https://tomcat.apache.org/)
- [Oracle Database / ou H2 para testes locais]
- IDE Java (Eclipse, IntelliJ IDEA, VS Code com plugin Java)

### Passo a passo

1. **Clonar o repositório**
   ```bash
   git clone https://github.com/leviberga/FinYou.git
   cd FinYou
   ```

2. **Importar o projeto**
   - Abra sua IDE e importe como projeto **Dynamic Web Project**.

3. **Configurar o servidor Tomcat**
   - Adicione o Tomcat 10 como servidor na IDE.
   - Configure o projeto para ser executado no Tomcat.

4. **(Opcional) Configurar o banco de dados**
   - Caso deseje usar persistência real, edite as credenciais no arquivo de configuração (DAO ou `context.xml`).

5. **Executar o projeto**
   - Inicie o servidor Tomcat.
   - Acesse em:  
     👉 `http://localhost:8080/FinYou/`

---

## 🛠️ Roadmap futuro

🔹 Migrar persistência para banco relacional (MySQL ou PostgreSQL)  
🔹 Implementar API REST para o backend  
🔹 Adicionar autenticação segura (hash de senha, JWT, etc.)  
🔹 Melhorar a interface (design responsivo e UX aprimorada)  
🔹 Adicionar gráficos interativos no dashboard  
🔹 Implementar exportação de relatórios  
🔹 Criar testes automatizados (JUnit)

---

## 👥 Autores

| Nome | Função | Contato |
|------|---------|----------|
| **Levi Bergamascki** | Desenvolvimento Backend / Frontend | [@leviberga](https://github.com/leviberga) |
| **Lanna Carvalho** | Desenvolvimento Backend / Frontend | [@Lannizz](https://github.com/Lannizz) |

---

## 📜 Licença

Este projeto está sob a licença **MIT** — sinta-se à vontade para usar e modificar.  
Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 💡 Agradecimentos

Este projeto foi desenvolvido como parte do curso de **Análise e Desenvolvimento de Sistemas (FIAP)**, com foco em aplicar conceitos de **POO**, **Arquitetura MVC** e **Desenvolvimento Web em Java**.
