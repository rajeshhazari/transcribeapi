FROM openjdk:11-alpine

# to check for open ports in the container: docker run <container id> netstat -tan
#RUN apt update && apt install net-tools
#RUN mkdir -pv /opt/apps
RUN mkdir /app
ENV APP_HOME /app

#ENV JAVA_OPTS
RUN mkdir $APP_HOME/config
#Create folder with application logs
RUN mkdir $APP_HOME/logs



WORKDIR $APP_HOME

# An executable jar that will run in this Docker container
#Run `mvn package` to create it
Run mvn -DskipTests clean package
RUN ls -lrth
#Copy executable jar file from the builder image
COPY --from=builder /build/target/*.jar app.jar

#COPY target/&.jar app.jar
RUN ls -lrth
# A simple shell script to pass JVM arguments
#ADD bin/entrypoint.sh /opt/apps/entrypoint.sh

VOLUME $APP_HOME/log
VOLUME /tmp

RUN ls -lrth
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

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
# ENTRYPOINT ["java","-cp","app:/app/lib/*","TranscribeapiApplication"]

ENTRYPOINT ["sh", "-c", "$JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar app.jar  $0 $@ "]
#docker build -t <image_tag> . && docker run -p 8585:8585 <image_tag>
#docker build -t <image_tag> . && docker run -p 8585:8585 -p 5005:5005 --env JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005  --env JAVA_OPTS=-Dlogging.level.org.springframework=DEBUG <image_tag>
