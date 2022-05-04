# Курсовая работа по предмету "Шаблоны проектирования на языке Java"

## Используемые технологии
- Spring Framework: Spring Core, Spring Boot, Spring Data Jpa, Spring Actuator, Spring MVC, Spring Security;
- PosgreSQL;
- Hibernate ORM, HikariConfig;
- Prometheus, Grafana;
- Docker, Docker Compose;

## Архитектура приложения
Построено по Clean Architecture, с тремя слоями для каждой feature: слой контроллер, бизнес слой и дата слой.

Типичное содержимое пакета особенности приложения:

TODO: add image with class diagram of `service` feature

## Запуск приложения

Сначала требуется склонировать репозиторий с помощью команды `git clone "repository link"`.
Затем, запустить в корневой директории проекта команду `./gradlew build`.
После этого, запускается команда `docker compose up`, и, в итоге, сервер запускается по адресу `http://localhost:8080/`.

Документация доступна по адресу `http://localhost:8080/swagger-ui/index.html`.
