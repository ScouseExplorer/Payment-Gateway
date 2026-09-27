# Multi-stage build for payment core service

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-26 AS builder

WORKDIR /app

# Copy POM and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:26-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

WORKDIR /app

# Copy built application from builder stage
COPY --from=builder /app/target/gateway-*.jar app.jar

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose port
EXPOSE 8080

# JVM configuration
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Run application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
