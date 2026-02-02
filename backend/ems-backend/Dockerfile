# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy everything into the build container
COPY . .

# Make sure mvnw is executable (Windows -> Linux fix)
RUN chmod +x mvnw

# Build the jar (skip tests for deployment builds)
RUN ./mvnw -DskipTests clean package

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the build stage
# This assumes your jar ends up in /app/target/*.jar
COPY --from=build /app/target/*.jar app.jar

# Render provides $PORT; Spring must bind to it
ENV PORT=8080
EXPOSE 8080

# Run Spring Boot on Render's port
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
