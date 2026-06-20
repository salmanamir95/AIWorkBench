# Stage 1: Build the application
FROM eclipse-temurin:26-jdk-jammy AS build
RUN apt-get update && apt-get install -y maven
WORKDIR /app

# 1. Copy ONLY the pom.xml first
COPY pom.xml .
# 2. Download dependencies (this layer is cached until pom.xml changes)
RUN mvn dependency:go-offline -B

# 3. Now copy the source code
COPY src ./src
# 4. Build the application
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Runtime
FROM eclipse-temurin:26-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "app.jar"]