FROM alpine:3.19

# Install Java + bash
RUN apk add --no-cache openjdk17 bash

WORKDIR /app
COPY target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
