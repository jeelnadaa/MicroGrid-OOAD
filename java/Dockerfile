# ---- Build Stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Runtime Stage ----
FROM tomcat:10.1-jdk17-temurin
# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy WAR
COPY --from=build /app/target/microgrid.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
