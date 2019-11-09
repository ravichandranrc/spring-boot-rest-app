# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

# copy jar into image
COPY build/libs/spring-boot-rest-app-*.jar /app.jar

#Expose the port
EXPOSE 8080

# run application with this command line
CMD ["/usr/bin/java", "-jar", "app.jar"]