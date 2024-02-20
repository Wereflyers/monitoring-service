# monitoring-service

Сервис, отвечающий за передачу показаний счетчиков. 

## Стэк технологий

- Java (Core, Collections)
- Maven
- Docker
- Lombok
- JDBC
- Servlet API
- JUnit, Mockito, Testcontainers

## Запуск

Для запуска приложения необходимо:
- Запустить mvn clean-package для получения упакованного в war проекта
- Подготовить Docker
- Запустить файл docker-compose.yml и дождаться создания контейнеров
- Запустить Tomcat 10
- Отправлять запросы на эндпоинты локального сервера