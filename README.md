

## Documentação Ada Web Planner 📕

![GitHub Repo size](https://img.shields.io/github/repo-size/elissatavares/currency-api)
![Build with Maven](https://img.shields.io/badge/Build%20with-Maven-brightblue)
[![Java Version](https://img.shields.io/badge/Java-17-red)](https://docs.oracle.com/javase/17/docs/api/)


Projeto para o módulo de Testes Automatizados I do Santander Coders.

O projeto *Currency* é uma aplicação em Java desenvolvida ...

## Demonstração da documentação ☕

<a href="https://ibb.co/stfjHwx"><img src="https://i.ibb.co/g62yVTq/2024-05-22-09-46-46.gif" alt="2024-05-22-09-46-46" border="0" /></a>

## Configurando e inicializando o projeto ☕

Primeiramente deverá fazer o clone da aplicação em sua máquina:

Através do https
```
    git clone https://github.com/elissatavares/currency-api.git
```

Através do ssh
```
    git clone git@github.com:elissatavares/currency-api.git
```

Para essa próxima etapa é necessário ter o  `docker e docker compose` instalado e configurado.

Na pasta currency-api execute o comando a seguir para inicializar o banco de dados.

```
    docker compose up
```
<a href="https://ibb.co/pWytPZ3"><img src="https://i.ibb.co/SQrhx0K/2024-05-22-09-36-49.gif" alt="2024-05-22-09-36-49" border="0" /></a>

Utilizamos o `Maven` para instalar todas as depedências utilizadas no projeto. Para executar um build do projeto use o comando:

```
    mvn clean install
```
<a href="https://ibb.co/C1pqsHm"><img src="https://i.ibb.co/JBWNFsm/2024-05-22-09-37-42.gif" alt="2024-05-22-09-37-42" border="0" /></a>

E, por fim, vá ate a classe  `CurrencyApiApplication` para iniciar o servidor no endereço: [http://localhost:8080](http://localhost:8080)
<a href="https://ibb.co/MGDPkZ6"><img src="https://i.ibb.co/k9gq251/2024-05-22-09-41-17.gif" alt="2024-05-22-09-41-17" border="0" /></a>

📕 Para consultar a através do Swagger-ui vá para a [Documentação no Swagger](http://localhost:8080/swagger-ui/index.html).


## Dependências ☕

<ul>
    <li>
        <a href="https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa">
            spring-boot-starter-data-jpa
        </a>
    </li>
    <li>
        <a href="https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation">
           spring-boot-starter-validation
        </a>
    </li>
    <li>
        <a href="https://mvnrepository.com/artifact/org.projectlombok/lombok">
            lombok
        </a>
    </li>
    <li>
        <a href="https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign">
            spring-cloud-starter-openfeign
        </a>
    </li>
    <li>
        <a href="https://mvnrepository.com/artifact/org.assertj/assertj-core">
            assertj-core
        </a>
    </li>
    <li>
        <a href="https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web">
           spring-boot-starter-web
        </a>
    </li>
    <li>
        <a href="https://mvnrepository.com/artifact/org.postgresql/postgresql">
           postgresql
        </a>
    </li>
    <li>
        <a href="https://docs.spring.io/spring-security/reference/servlet/test/index.html">
           spring-boot-starter-test
        </a>
    </li>
           <li>
        <a href="https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui">
           springdoc-openapi-starter-webmvc-ui
        </a>
    </li>
        </a>
    </li>
</ul>




## 🤝 Colaboradores
<a name="contribua"></a>
<table align="center"><tr>
</td>
    <td align="center"><a href="https://github.com/elissatavares" target="_blank"><img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/128258713?v=4" width="100px;" alt=""/>
    <br />
    <sub><b>Elissa Tavares</b></sub></a>

<br />
<a href="https://www.linkedin.com/in/elissatavares/" alt="LinkedIn">
  <img src="https://img.shields.io/badge/-Linkedin-0e76a8?style=flat-square&logo=Linkedin&logoColor=white&link=https://linkedin.com/in/elissatavares" /></a>
</td>
  </tr>
</table>


## 📝 Licença
Esse projeto está sob licença. Veja o arquivo [LICENÇA](https://github.com/elissatavares/ada-web-planner/blob/main/LICENSE.md) para mais detalhes.


<p align="right">(<a href="#readme-top">Voltar ao topo</a>)</p>
<hr>
<p align="center">Copyright © 2024 | Currency API </p>



