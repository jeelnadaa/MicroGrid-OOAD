# Microclimate Sensor Grid

A Java (Jakarta EE) web application for managing microclimate sensor grids, built with **Servlets + JSP + JDBC** and fully containerized with **Docker**.

## Quick Start

> Requires: **Docker** & **Docker Compose**

```bash
cd java
docker compose up --build
```

Open **<http://localhost:8080>** once both containers are healthy.

To stop:

```bash
docker compose down            # keep data
docker compose down -v         # wipe MySQL volume
```

## Technology Stack

| Layer        | Technology                                          |
| ------------ | --------------------------------------------------- |
| Language     | Java 17                                             |
| Build        | Maven 3.9                                           |
| Web Server   | Apache Tomcat 10.1 (Jakarta Servlet 6.0)            |
| Views        | JSP 3.1 + JSTL 3.0                                 |
| Database     | MySQL 8.0 (JDBC, MySQL Connector/J 8.3)             |
| Auth         | BCrypt (favre 0.10.2) + session-based authentication |
| Frontend     | Bootstrap 5, Bootstrap Icons                        |
| Containers   | Docker multi-stage build + Docker Compose            |

## OOP Design

The refactor follows the class diagram from the project specification:

- **Inheritance** — `Admin extends User`
- **Enums** — `SensorStatus` (ACTIVE, INACTIVE, MAINTENANCE), `MaintenanceType` (CALIBRATION, REPAIR, REPLACEMENT)
- **Encapsulation** — private fields with getters/setters across all model classes
- **Domain Methods** — `User.login()`, `User.logout()`, `Admin.addSensorType()`, `Admin.addLocation()`, `Admin.registerSensor()`, `Admin.manageSensors()`, `Sensor.updateStatus()`, `Sensor.addReadings()`, `Technician.performMaintenance()`, `Technician.addMaintenanceNotes()`

## Architecture

Layered MVC architecture:

```
Model (POJOs + Enums)
  └─▶ DAO (JDBC / PreparedStatement)
       └─▶ Service (business logic & validation)
            └─▶ Servlet (HTTP controllers)
                 └─▶ JSP (views with JSTL)
```

## Project Structure

```
java/
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── pom.xml
└── src/main/
    ├── java/com/microgrid/
    │   ├── model/
    │   │   ├── enums/
    │   │   │   ├── SensorStatus.java
    │   │   │   └── MaintenanceType.java
    │   │   ├── User.java
    │   │   ├── Admin.java
    │   │   ├── Sensor.java
    │   │   ├── SensorType.java
    │   │   ├── Location.java
    │   │   ├── Reading.java
    │   │   ├── Technician.java
    │   │   ├── MaintenanceRecord.java
    │   │   └── SensorStatusLog.java
    │   ├── dao/
    │   │   ├── DatabaseConnection.java
    │   │   ├── UserDAO.java
    │   │   ├── SensorTypeDAO.java
    │   │   ├── LocationDAO.java
    │   │   ├── SensorDAO.java
    │   │   ├── ReadingDAO.java
    │   │   ├── TechnicianDAO.java
    │   │   ├── MaintenanceDAO.java
    │   │   └── SensorStatusLogDAO.java
    │   ├── service/
    │   │   ├── AuthService.java
    │   │   ├── SensorTypeService.java
    │   │   ├── LocationService.java
    │   │   ├── SensorService.java
    │   │   ├── ReadingService.java
    │   │   ├── TechnicianService.java
    │   │   ├── MaintenanceService.java
    │   │   └── ReportService.java
    │   ├── servlet/
    │   │   ├── AuthServlet.java
    │   │   ├── DashboardServlet.java
    │   │   ├── SensorTypeServlet.java
    │   │   ├── LocationServlet.java
    │   │   ├── SensorServlet.java
    │   │   ├── ReadingServlet.java
    │   │   ├── TechnicianServlet.java
    │   │   ├── MaintenanceServlet.java
    │   │   ├── ReportServlet.java
    │   │   └── ExportServlet.java
    │   ├── filter/
    │   │   └── AuthFilter.java
    │   └── util/
    │       ├── PasswordUtil.java
    │       └── FlashMessage.java
    ├── resources/
    │   └── db.properties
    └── webapp/
        ├── static/
        │   ├── css/style.css
        │   └── js/script.js
        └── WEB-INF/
            ├── web.xml
            └── jsp/
                ├── includes/ (header, footer)
                ├── index.jsp (dashboard)
                ├── auth/ (login, signup)
                ├── sensor_types/ (list, form)
                ├── locations/ (list, form)
                ├── sensors/ (list, form, readings)
                ├── readings/ (list, form)
                ├── technicians/ (list, form)
                ├── maintenance/ (list, form)
                ├── reports/ (index)
                └── errors/ (404, 500)
```

## Features

- **Dashboard** — sensor counts, location stats, recent readings
- **Full CRUD** — Sensor Types, Locations, Sensors, Readings, Technicians, Maintenance Events
- **Authentication** — signup, login, logout with BCrypt hashed passwords and session management
- **Search & Filter** — filter sensors by type/status/location, readings by sensor, maintenance by type/technician
- **Reports & Analytics** — status distribution, maintenance summary, top technicians, average readings by type, status change logs
- **CSV Export** — export any entity to CSV via `/export/{entity}/csv`
- **Stored Procedures & Triggers** — database-level automation (from `schema.sql`)

## Database

The MySQL schema (`database/schema.sql`) is auto-initialized by Docker Compose on first run.

## Configuration

### Docker (default)

Environment variables in `docker-compose.yml` configure the DB connection:

| Variable      | Default              |
| ------------- | -------------------- |
| `DB_HOST`     | `db`                 |
| `DB_PORT`     | `3306`               |
| `DB_NAME`     | `microclimate_grid`  |
| `DB_USER`     | `root`               |
| `DB_PASSWORD` | `rootpassword`       |

### Local Development (without Docker)

Edit `src/main/resources/db.properties`:

```properties
db.host=localhost
db.port=3306
db.name=microclimate_grid
db.user=root
db.password=
```

Then build and deploy with Maven + a local Tomcat 10.1 instance:

```bash
mvn clean package
cp target/microgrid.war $CATALINA_HOME/webapps/ROOT.war
```

## Routes

| Path                     | Method   | Description              |
| ------------------------ | -------- | ------------------------ |
| `/`                      | GET      | Dashboard                |
| `/login`                 | GET/POST | Login                    |
| `/signup`                | GET/POST | Register                 |
| `/logout`                | GET      | Logout                   |
| `/sensor-types`          | GET      | List sensor types        |
| `/sensor-types/create`   | GET/POST | Create sensor type       |
| `/sensor-types/edit`     | GET/POST | Edit sensor type         |
| `/sensor-types/delete`   | POST     | Delete sensor type       |
| `/locations`             | GET      | List locations           |
| `/locations/create`      | GET/POST | Create location          |
| `/locations/edit`        | GET/POST | Edit location            |
| `/locations/delete`      | POST     | Delete location          |
| `/sensors`               | GET      | List sensors             |
| `/sensors/create`        | GET/POST | Create sensor            |
| `/sensors/edit`          | GET/POST | Edit sensor              |
| `/sensors/delete`        | POST     | Delete sensor            |
| `/sensors/readings`      | GET      | Readings for a sensor    |
| `/readings`              | GET      | List all readings        |
| `/readings/create`       | GET/POST | Record a reading         |
| `/readings/edit`         | GET/POST | Edit reading             |
| `/readings/delete`       | POST     | Delete reading           |
| `/technicians`           | GET      | List technicians         |
| `/technicians/create`    | GET/POST | Create technician        |
| `/technicians/edit`      | GET/POST | Edit technician          |
| `/technicians/delete`    | POST     | Delete technician        |
| `/maintenance`           | GET      | List maintenance events  |
| `/maintenance/create`    | GET/POST | Create maintenance event |
| `/maintenance/edit`      | GET/POST | Edit maintenance event   |
| `/maintenance/delete`    | POST     | Delete maintenance event |
| `/reports`               | GET      | Reports & analytics      |
| `/export/{entity}/csv`   | GET      | CSV export               |
