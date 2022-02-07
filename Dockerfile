FROM openjdk:11
ADD target/ibm-giphy.jar ibm-giphy.jar
ENTRYPOINT ["java", "-jar", "ibm-giphy.jar"]
EXPOSE 8082