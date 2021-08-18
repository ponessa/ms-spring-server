# Build stage
FROM maven:3.6.3-openjdk-11 as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package 


# Package stage
FROM openjdk:11-jdk
COPY --from=build /home/app/target/ms-spring-server-0.0.1-SNAPSHOT.jar /ms-spring-server-0.0.1-SNAPSHOT.jar

#RUN cd /home/app
RUN mkdir /uploads
RUN mkdir /downloads
RUN mkdir /templates


RUN chgrp -R 0 /uploads && \
    chmod -R g=u /uploads

RUN chgrp -R 0 /templates && \
    chmod -R g=u /templates

RUN chmod -R 777 /uploads
RUN chmod -R 777 /downloads
RUN chmod -R 777 /templates

COPY src/main/resources/static/templates /templates

EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","/ms-spring-server-0.0.1-SNAPSHOT.jar", "com.ibm.wfm.Application"]