Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

As the new engineer leading this project, your first task is to make it production-grade, feel free to refactor any piece
necessary to achieve the goal.

### How to deliver the code

Please send an email containing your solution with a link to a public repository.

>**DO NOT create a Pull Request with your solution** 

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.

### Runnig project

**Configure postgres database before running the project** 

Check `src/main/resource/application.properties` file update properties

`spring.datasource.url`
`spring.datasource.username`
`spring.datasource.password`

for your database configuration.
Integration tests run with embedded database, no configuration required 

Type `mvn spring-boot:run` to run the project.

Swagger UI is configured to test click `http://localhost:8080/swagger-ui.html` after running the project to get Swagger UI