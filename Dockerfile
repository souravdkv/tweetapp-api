FROM openjdk:8
ADD target/tweetapp-api-0.0.1-SNAPSHOT.jar tweetapp-api-0.0.1-SNAPSHOT.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "tweetapp-api-0.0.1-SNAPSHOT.jar"]