# Game of Three

## Technology Stack Used
- Spring Boot
- Java 8
- ActiveMq
- Maven

## Build and Run (without Docker - easy way for local testing)
Run below commands to build (provided environment is having Java 8 and Maven 3+ installed on machine)
- ``mvn clean install``
- go to target directory and run build jar using commands below
  - ``java -jar gameofthree-0.0.1-SNAPSHOT.jar`` (For first player)
  - ``java -jar gameofthree-0.0.1-SNAPSHOT.jar --spring.config.name=application_player2`` (For second player, if somehow properties are not resolving then copy application_player2.properties from resources to target folder)  
- After this Game is ready to start, just hit above GET URL from browser (or using postman) to start the game.
  `` http://localhost:8080/api/game/start/true
  ``
- Monitor logs for output of the game.  

## Build and Run (with Docker)

