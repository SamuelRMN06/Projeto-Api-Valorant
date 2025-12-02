1\. AuthService -- Autenticação e Tokens JWT (gRPC)
==================================================

-   Responsável por login e validação de tokens.

-   Gera JWT ao usuário fazer login.

-   O Gateway consulta esse serviço via gRPC para confirmar se o token é válido.

-   Não responde requisições HTTP.

Responsabilidades:\
✔ Login\
✔ Gerar token JWT\
✔ Validar token para todos os outros serviços

* * * * *

2\. ValorantService -- Busca de Skins (gRPC)
===========================================

-   Conecta-se à API externa do Valorant.

-   Obtém skins, detalhes e dados visuais.

-   Retorna ao Gateway via gRPC.

-   Não exige autenticação, pois o Gateway já valida antes.

Responsabilidades:\
✔ Buscar skins\
✔ Organizar o DTO\
✔ Fornecer lista paginada ou completa para o Gateway

* * * * *

3\. PaymentService -- Pagamentos (gRPC)
======================================

-   Novo microserviço.

-   Processa pagamento API Mercado Pago.

-   O Gateway o consulta antes de confirmar o pagamento.

Responsabilidades:\
✔ Receber solicitação de compra (gRPC)\
✔ Validar saldo ou método de pagamento\
✔ Registrar a transação\
✔ Retornar "APROVADO" ou "NEGADO"

* * * * *

4\. API Gateway -- Porta de Entrada (REST + gRPC Clients)
========================================================

-   Único serviço acessível pelo frontend.

-   Expõe rotas REST como:

-  /auth/registrar

-   /auth/login

-   /validate/getSkin

-   /validate/payment

-   /validate/premium/{id}

-   Internamente chama os demais serviços via gRPC.

-   Valida o token antes de qualquer operação.

-   Evita que o frontend precise saber onde cada serviço está.

Responsabilidades:\
✔ Rotas HTTP\
✔ Autenticação centralizada\
✔ Orquestrar chamadas aos outros serviços\
✔ Unir respostas e devolver ao frontend

* * * * *

* * * * *

Resumo Final (Com Tecnologias)
==============================

-   AuthService → Login e validação JWT (Spring Boot + gRPC + Postgres)

-   ValorantService → Busca de skins da API externa (Spring Boot + gRPC)

-   PaymentService → Processamento de pagamentos (Spring Boot + gRPC + Mercado Pago + Postgres)

-   API Gateway → Ponto de entrada REST para o frontend React (Spring Boot + REST + gRPC Client)

-   Frontend → React consumindo apenas o Gateway

-   Arquitetura → Microserviços + MVC + Comunicação por gRPC

-   Banco → PostgreSQL para autenticação, pagamentos e possível cache de skins
