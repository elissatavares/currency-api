#Informa ao docker qual a imagem base que ele vai trabalhar. Essa imagem ja tem o java instalado
FROM openjdk:17-jdk

#colocar o target no container - cria uma pasta no container chamada app - renomeio o currency-api-0.0.1-SNAPSHOT.jar para app.jar
COPY target/currency-api-0.0.1-SNAPSHOT.jar /app/app.jar

#"Execute a minha aplicaçõa java e esta esta dentro de /app/app.jar"
CMD ["java", "-jar", "/app/app.jar"]