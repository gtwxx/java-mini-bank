# Небольшой Клиентский Финансовый Сервис

## 📌 Описание
Spring Boot приложение для управления клиентами, их счетами и переводами между ними. Сервис поддерживает:
- Создание клиентов
- Создание счетов для клиентов
- Получение всех счетов клиента
- Переводы между счетами в одной валюте

## 🛠️ Технологии
- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 (встроенная база данных)
- Maven
- JUnit 5

## 📂 Структура проекта
- `domain/` — сущности JPA (`Client`, `Account`)
- `repository/` — интерфейсы Spring Data JPA
- `service/` — бизнес-логика
- `controller/` — REST API
- `dto/` — модели передачи данных
- `resources/schema.sql` — SQL скрипт для инициализации структуры БД
- `tests/` — unit и integration тесты

## 🚀 Запуск приложения

```bash
mvn spring-boot:run
```

База данных H2 автоматически поднимется в памяти.

## ✅ Примеры запросов

### Создать клиента
```http
POST /clients/new
Content-Type: application/json

{
  "login": "user123",
  "fullName": "John Doe"
}
```

### Создать счёт
```http
POST /clients/user123/accounts/new
Content-Type: application/json

{
  "accountNumber": "ACC999",
  "currency": "RUB",
  "initialBalance": 1000.0
}
```

### Перевод между счетами
```http
POST /clients/user123/accounts/ACC999/transfer
Content-Type: application/json

{
  "toAccountNumber": "ACC998",
  "amount": 100.0
}
```

## ⚠️ Ограничения
- Уникальность номера счёта
- Нельзя переводить самому себе
- Только одинаковая валюта при переводе
- Только свои счета доступны для операций
- Нельзя уходить в минус
- Поддержка конкурентных операций через транзакции

## 🧪 Тестирование

```bash
mvn test
```
