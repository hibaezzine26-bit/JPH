# ==========================================
# Étape 1 : Build Maven (Java 21)
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copie du fichier de configuration Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie du code source et compilation
COPY src ./src
RUN mvn package -DskipTests

# ==========================================
# Étape 2 : Runtime léger JRE 21
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copie du fichier .jar produit par le build
COPY --from=build /app/target/*.jar app.jar

# Exposition du port du serveur Spring Boot
EXPOSE 8080

# Démarrage de l'application Java
ENTRYPOINT ["java", "-jar", "app.jar"]
