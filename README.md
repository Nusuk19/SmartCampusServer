SmartCampus backend (in-memory MVP)
Run:
- Java 17 + Maven
- mvn spring-boot:run
API:
- GET /api/rooms
- GET /api/rooms/available
- POST /api/rooms/open/{nfcTagId}
Notes:
- PostgreSQL/jpa dependencies are commented in pom.xml and application.yml for later migration.
