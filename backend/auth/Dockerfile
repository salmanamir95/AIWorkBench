# Stage 1: Build the application using Java 26
FROM eclipse-temurin:26-jdk-jammy AS build
# Install maven since the image is just the JDK
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image using Java 26
FROM eclipse-temurin:26-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081 
ENTRYPOINT ["java", "-jar", "app.jar"]