#FROM openjdk:11-slim as runtime
FROM openjdk:11-jdk-slim as runtime

# to check for open ports in the container: docker run <container id> netstat -tan
#ARG SSH_KEY
#RUN echo "$SSH_KEY" > /root/.ssh/id_rsa
ARG JAVA_OPTS
ARG MAIN_CLASS
ARG DEPENDENCY=target/dependency

 
#COPY . /tmp
RUN echo $JAVA_HOME  $PATH && \
    export PATH=$PATH:$JAVA_HOME/bin && \
   ls -lrth /usr/share/ca-certificates/mozilla/  && \
   ls -lrth $JAVA_HOME  && \
   id -a 
  
RUN apt-get update && apt-get upgrade -y && rm -rf /var/cache/apk/* && \
	wget htts://bootstrap.pypa.io/get-pip.py && \
    python get-pip.py
#find /usr/share/ca-certificates/mozilla/ -name "*.crt" -type f -exec  $JAVA_HOME/bin/keytool -importcert  -cacerts  -storepass changeit -noprompt  -file '{}' -alias '{}' \; && \
	#  $JAVA_HOME/bin/keytool -list -keystore $JAVA_HOME/lib/security/cacerts --storepass changeit

ENV APP_HOME /opt

RUN  addgroup --system --gid 2001 devops  && \
     adduser app_user  && \
     usermod -a app_user -G  devops
RUN  chown app_user:devops -R $APP_HOME
USER app_user
RUN ls -lrth

RUN  mkdir -p /opt/apps && \
     mkdir -p $APP_HOME/config && \
     mkdir -p $APP_HOME/logs && \
     chown app_user:devops -R $APP_HOME

WORKDIR $APP_HOME/apps/


# JMX
#EXPOSE 9010

# JVM debugging port
EXPOSE 8585

COPY /opt/BOOT-INF/classes/sample.jks /opt/BOOT-INF/classes/sample.cert
# ARG DEPENDENCY=target/dependency
 COPY ./BOOT-INF/lib $APP_HOME/apps/lib
 COPY ./META-INF $APP_HOME/apps/META-INF
 COPY ./BOOT-INF/classes $APP_HOME/apps
 COPY ./BOOT-INF/classes/sample.jks $APP_HOME/apps/classes/sample.cert
#ENTRYPOINT ["sh", "-c", "$JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar app.jar "]
ENTRYPOINT ["java","-cp","$APP_HOME/apps/lib/*:$APP_HOME/apps/classes  $JAVA_OPTS  -Djava.security.egd=file:/dev/urandom $MAIN_CLASS"]

#docker build -t <image_tag> . && docker run -p 8585:8585 <image_tag>
#docker build -t <image_tag> . && docker run -p 8585:8585 -p 5005:5005 --env JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005  --env JAVA_OPTS=-Dlogging.level.org.springframework=DEBUG <image_tag>
#docker build -t <image_tag> . && docker run -p 8080:8080 -v /opt/spring-boot/test/config:/app/config:ro <image_tag>
