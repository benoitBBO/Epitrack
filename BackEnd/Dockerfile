# Utiliser une image contenant une version minimale de Java pour exécuter le projet
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers compilés à partir de l'image builder dans l'image de production
COPY /EXPOSITION/target/EXPOSITION-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar

# Exposer le port utilisé par votre application (remplacez <port> par le numéro de port réel)
EXPOSE 8080

# Commande pour démarrer votre application
CMD ["java", "-jar", "app.jar"]