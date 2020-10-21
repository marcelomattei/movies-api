FROM openjdk:8
ADD target/movies.jar movies.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "movies.jar"]
