# monitoring-service

Сервис, отвечающий за передачу показаний счетчиков. 

## Стэк технологий

- Java
- Maven
- Docker
- Lombok
- JDBC
- Spring Boot
- Swagger
- JUnit, Mockito, Testcontainers

## Запуск

Для запуска приложения необходимо:
- Запустить mvn clean-package для получения упакованного в war проекта
- Подготовить Docker
- Запустить файл docker-compose.yml и дождаться создания контейнеров
- Запустить Tomcat 10
- Отправлять запросы на эндпоинты локального сервера

Ссылка на Swagger:
http://localhost:8080/v3/api-docs
http://localhost:8080/swagger-ui/index.html