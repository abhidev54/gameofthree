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
Run below commands to build (provided you have docker installed on server)
- ``docker build -t abhidev/gameofthree:latest .``
- ``docker run -d --mount source=sampleVolume,target=/var/lib/gameofthree -p 8080:8080 abhidev/gameofthree:latest``

For player 2 (just rename application_player2.properties to application.properties (not ideal way , we can automate this as well using COPY command)) and run same above commands
- ``docker build -t abhidev/gameofthree:latest .``
- ``docker run -d --mount source=sampleVolume,target=/var/lib/gameofthree -p 8081:8081 abhidev/gameofthree:latest``

- After this Game is ready to start, just hit above GET URL from browser (or using postman) to start the game.
  `` http://localhost:8080/api/game/start/true
  ``
- Monitor logs of docker container for output of the game.  

** I have added K8 configs as well in case we want use kubernates for this app. we can easily deploy on K8s and scale it using pods.

