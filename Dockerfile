# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# 👉 Windows → Linux fix: normalize line endings and make executable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN ./mvnw -q -DskipTests dependency:go-offline
COPY src ./src
RUN ./mvnw -q -DskipTests package

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV PORT=8080
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["sh","-c","java -Dserver.port=${PORT} -Dspring.profiles.active=prod -jar /app/app.jar"]
