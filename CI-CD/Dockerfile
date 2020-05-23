FROM openjdk:11-jdk-alpine

# to check for open ports in the container: docker run <container id> netstat -tan
#RUN apt update && apt install net-tools

RUN mkdir -pv /opt/apps

# An executable jar that will run in this Docker container
#Run `mvn package` to create it
Run mvn -DskipTests clean package
RUN ls -lrth
COPY target/transcribeapi-0.0.1-SNAPSHOT.jar /opt/apps/
RUN ls -lrth
# A simple shell script to pass JVM arguments
#ADD bin/entrypoint.sh /opt/apps/entrypoint.sh

VOLUME /tmp
RUN ls -lrth
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

# JMX
EXPOSE 9010

# JVM debugging port
EXPOSE 8585

#ENTRYPOINT [ "/opt/apps/entrypoint.sh" ]
##
#FROM openjdk:11-jdk-alpine
#VOLUME /tmp
# ARG DEPENDENCY=target/dependency
## COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY ${DEPENDENCY}/META-INF /app/META-INF
# COPY ${DEPENDENCY}/BOOT-INF/classes /app
# ENTRYPOINT ["java","-cp","app:app/lib/*","TranscribeapiApplication"]

ENTRYPOINT ["java","-cp","app:/opt/apps/lib/*","TranscribeapiApplication"]
