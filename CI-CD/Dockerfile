#FROM openjdk:11-slim as runtime
#FROM openjdk:11-jdk-slim as runtime

# (1) use Alpine Linux for build stage
#FROM alpine:latest as runtime
# (2) install build dependencies
#RUN apk --no-cache add openjdk11
#RUN echo $PATH $JAVA_HOME
# build JDK with less modules
#RUN /usr/lib/jvm/default-jvm/bin/jlink \
#    --compress=2 \
#    --module-path /usr/lib/jvm/default-jvm/jmods \
#    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
#    --output /jdk-minimal
#-Djava.security.egd=file:/dev/./urandom 

From spring-boot-runtime:openjdk-14-slim


# to check for open ports in the container: docker run <container id> netstat -tan
#ARG SSH_KEY
#RUN echo "$SSH_KEY" > /root/.ssh/id_rsa
ENV JAVA_OPTS=""
ENV MAIN_CLASS=""
ARG UID=1001
ARG USERNAME=app_user
ARG GROUPNAME=devops
ARG GID=1000
ARG DEPENDENCY=target/dependency

#find /usr/share/ca-certificates/mozilla/ -name "*.crt" -type f -exec  $JAVA_HOME/bin/keytool -importcert  -cacerts  -storepass changeit -noprompt  -file '{}' -alias '{}' \; && \
#  $JAVA_HOME/bin/keytool -list -keystore $JAVA_HOME/lib/security/cacerts --storepass changeit

ENV APP_HOME /opt

RUN  export JAVA_HOME=$(which java) && \
     echo $PATH $JAVA_HOME && \
     export PATH=$PATH:$JAVA_HOME/bin &&\
     addgroup -gid $GID  $GROUPNAME  && \
     useradd -m -r -g $GROUPNAME $USERNAME && \
     mkdir -p /opt/apps/transcribeapi && \
	 mkdir -p $APP_HOME/configs && \
     mkdir -p $APP_HOME/logs && \
     mkdir -p $APP_HOME/apps/temp && \
     echo $HOME && \
     mkdir -p $HOME/temp/logs/ && \
     chown -R $USERNAME:$GROUPNAME  $APP_HOME



USER $USERNAME

WORKDIR $APP_HOME/apps/


# JMX
#EXPOSE 9010

# JVM debugging port
EXPOSE 8585

COPY ./BOOT-INF/lib $APP_HOME/apps/transcribeapi/lib
COPY ./META-INF $APP_HOME/apps/transcribeapi/META-INF
COPY ./BOOT-INF/classes $APP_HOME/apps/transcribeapi
COPY ./org $APP_HOME/apps/transcribeapi/org
#RUN cp $APP_HOME/apps/transcribeapi/classes/sample.jks $APP_HOME/apps/transcribeapi/classes/sample.cert
#ENTRYPOINT ["sh", "-c", "$JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar app.jar "]
HEALTHCHECK --interval=5s --timeout=5s   CMD curl -k --fail https://localhost:8585/api/v1/actuator/health || exit 1
ENTRYPOINT ["sh", "-c",  "java  -cp  /opt/apps/transcribeapi:/opt/apps/transcribeapi/lib/*:/opt/apps/transcribeapi/classes:/opt/apps/transcribeapi/org    $JAVA_OPTS   -Djava.security.egd=file:/dev/urandom   $MAIN_CLASS " ]

#docker build -t <image_tag> . && docker run -p 8585:8585 <image_tag>
#docker build -t <image_tag> . && docker run -p 8585:8585 -p 5005:5005 --env JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005  --env JAVA_OPTS=-Dlogging.level.org.springframework=DEBUG <image_tag>
#docker build -t <image_tag> . && docker run -p 8080:8080 -v /opt/spring-boot/test/config:$APP_HOME/apps/configs:ro <image_tag>
