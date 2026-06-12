FROM eclipse-temurin:26-jre-alpine

RUN apk add --no-cache bash

WORKDIR /app
COPY target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
