FROM openjdk:8-jdk

# to check for open ports in the container: docker run <container id> netstat -tan
#RUN apt update && apt install net-tools

RUN mkdir -p /opt/apps

# An executable jar that will run in this Docker container
# Run `mvn package` to create it
ADD target/sample-app.jar /opt/apps/app.jar

# A simple shell script to pass JVM arguments
ADD bin/entrypoint.sh /opt/apps/entrypoint.sh

# JMX
EXPOSE 9010

# JVM debugging port
EXPOSE 5005

ENTRYPOINT [ "/opt/apps/entrypoint.sh" ]
