*About The Projeect
Request-sys-API is a backed REST API application for a dormitory request management system. The application allows students to create maintenance requests (e.g., for electrical, plumbing, or furniture issues), 
and specialists (electricians, plumbers, managers) to manage these requests, assign performers, and track ther statuses.

*Features
For Students (Users)
- Registation and uthentication
- Viewing a list of their requests
- Creating a new request with a problem type and description
- Receiving notifications about request status changes
For Electician, Plumber and Managers:
- Viewing all requests or requests of a specific type.
- Filtering requests by status
- Changing requests status (e.g., "Pending", "In Progress", "Completed", "Rejected").
- Assigning a request to themselves or another performer
- Viewing and managing equipment and rooms in the dormitory

*Tech Stack
- Backend Framework: Spring Boot 3.x
- Database: PostgreSQL
- API Documentation: Swagger (OpenAPI 3.0)
- ORM: Hibername
- Database Management: Spring Data JPA, Liquibase (for migrations)
- Utilities: Project Lombok (to reduce boilerplate code)
- Testing:
  - JUnit 5
  - Mockito (for unit testing)
  - Jacoco (for test coverage analysis)
    
*Entities (Data Models)

*Core system entities:
1. User: Students, electicity, plumber, manager. Have roles (ROLE_STUDENT, ROLE_ELECTRICIAN, ROLE_PLUMBER, ROLE_MANAGER).
2. Dormitory: Information about dormitories (name, address).
3. Room: A room in a dormitory, linked to Dormitory.
4. Equipment: Description of equipment in a room (refrigerator, lamp, faucet, etc.).
5. Request: The core entity. Contains a problem description, status, priority, links to User (creator and performer), Room, and Equipment.
6. Notification: Notifications for users about changes in the status of their requests.

*Getting Started
Prerequisites
- JDK 17 or higher installed
- A DBMS installed (e.g., PostgreSQL)
- Maven

*Installation & Run
- build the JAR and run it:
./mvnw clean package
java -jar target/request-sys-api-0.0.1-SNAPSHOT.jar

*API Documentation
After starting the application, the Swagger/OpenAPI documentation will be available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
In Swagger UI, you can explore all endpoints, their parameters, request/response models, and test the API directly from your browser.

*Testing
run unit and integration tests, use the command: ./mvnw test
To generate a test coverage report (Jacoco), run: ./mvnw test jacoco:report
The report will be generated in the target/site/jacoco/index.html directory (open this file in a browser).





