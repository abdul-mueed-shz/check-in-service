# Check-In Service

This is a Spring Boot application for managing employee check-ins and check-outs.
**Service architecture** and a **short explanation** documents are provided in the **docs'** folder. Kindly refer to
them.

## Prerequisites

* Java 17 or higher
* Docker and Docker Compose
* Maven (wrapper included)

## Starting Docker Containers

This project uses Docker Compose to run the required infrastructure services:

* **PostgreSQL**: Database for storing application data.
* **Kafka & Zookeeper**: For messaging.
* **Kafka UI**: Web interface for managing Kafka clusters.

To start the containers, run the following command in the project root:

```bash
docker-compose up -d
```

This will start the services in the background.

* **Kafka UI** will be available at [http://localhost:8080](http://localhost:8080).
* **Postgres** runs on port `5432`.
* **Kafka** runs on port `29092` (external access).

## Running the Application

Create a .env file and specify the following properties required for the application to work:

```bash
JAVA_MAIL_SENDER_EMAIL=
JAVA_MAIL_SENDER_APP_PASSWORD=
```

Once the Docker containers are up and running, you can start the Spring Boot application using the Maven wrapper:

```bash
./mvnw spring-boot:run
```

The application will start on the default port (usually 8080, unless configured otherwise in `application.properties` or
`application.yml`).

## API Endpoints

### Record Employee Time Data

Records a check-in or check-out for a specific employee.

* **URL**: `/api/v1/timesheets/{employeeId}/record`
* **Method**: `POST`
* **Path Parameters**:
    * `employeeId` (Long): The unique identifier of the employee.
* **Response**:
    * Returns a `MessageDto` containing the `TimeSheetDto` with the recorded details.

**Example Request:**

```bash
curl -X POST http://localhost:8080/api/v1/timesheets/123/record
```
