FROM eclipse-temurin:21-jre
LABEL authors="Breno Lemos de Brito"

WORKDIR /app

COPY target/DesafioJunior-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]