FROM openjdk:8-jdk-alpine

# to check for open ports in the container: docker run <container id> netstat -tan
#RUN apt update && apt install net-tools

RUN mkdir -pv /opt/apps

# An executable jar that will run in this Docker container
# Run `mvn package` to create it
ADD target/transcribeapi.jar /opt/apps/app.jar

# A simple shell script to pass JVM arguments
#ADD bin/entrypoint.sh /opt/apps/entrypoint.sh

VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:/opt/apps/lib/*","TranscribeapiApplication"]

# JMX
EXPOSE 9010

# JVM debugging port
EXPOSE 5005

ENTRYPOINT [ "/opt/apps/entrypoint.sh" ]
