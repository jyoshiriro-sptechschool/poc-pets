# API de Pets e seus testes

POC feita com **Spring Boot 2.7.6** e **Java 11**. 


## Iniciando a API

É necessário ter o JDK 11 ou mais recente!

No diretório **api-pets-completa**, executar:
1. ```sh mvn package```
2. ```sh mvnw spring-boot:run```


## Documentação da API 

Disponível em http://localhost:8080/swagger-ui/index.html (versão *Swagger UI*) ou http://localhost:8080/v3/api-docs (versão *Open API 3.0*).
A API já nasce com 4 pets cadastrados.


## Autenticação

Autenticação do tipo **Basic Auth**, com os seguintes usuários possíveis:
* u: **usuario1** / s: **s1** (seu perfil é **usuario**)
* u: **usuario2** / s: **s2** (seu perfil é **usuario**)
* u: **admin** / s: **admin** (seu perfil é **admin**)


## Projeto de testes externo

Fica no diretório **testes-api-pets**.
