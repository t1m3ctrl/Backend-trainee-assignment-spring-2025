# 📦 Order Pickup Point

## 🚀 Описание

Этот проект представляет собой REST API-приложение на Spring Boot с использованием базы данных PostgreSQL и визуальным интерфейсом управления через Adminer.
Документация API доступна через Swagger UI.

---

## 🧰 Стек технологий

- Java 21  
- Spring Boot (Security + JWT, AOP + SLF4J, Data JDBC, Web)
- PostgreSQL
- Liquibase
- Mockito, Junit, Testcontainers
- Docker + Docker Compose  
- Swagger (Springdoc OpenAPI)

---

## 🐳 Как запустить проект

### 1. Клонировать репозиторий

```bash
git clone https://github.com/t1m3ctrl/Backend-trainee-assignment-spring-2025
cd Backend-trainee-assignment-spring-2025
```

### 2. Создать .env файл

Создайте файл .env в корне проекта и укажите переменные окружения:
```.env
POSTGRESDB_USER=your_db_user
POSTGRESDB_ROOT_PASSWORD=your_db_password
POSTGRESDB_DATABASE=your_db_name
```

### 3. Запуск контейнеров

```bash
docker compose up --build
```
Контейнеры будут развернуты:

app — Spring Boot API (порт: 8080)  
db — PostgreSQL база данных (порт: 5777)  
adminer — web-интерфейс к БД (порт: 8081)  

## Swagger UI

Swagger UI доступен по адресу
👉 http://localhost:8080/swagger-ui/index.html

## Adminer

Интерфейс Adminer доступен по адресу:
👉 http://localhost:8081

## 🛑 Остановка контейнеров
Для остановки всех контейнеров:

```bash
docker compose down
```
