#!/bin/bash
pwd
ls -lrth
cp target/*.jar app.jar
mv app.jar /opt/apps/
#curl -k -X POST https://localhost:8585/api/v1/actuator/shutdown || true
if [ -f /opt/apps/pid.file ]; then
    ls -lrth
    echo " stopping the app  with pid::  `(cat /opt/apps/pid.file)`"
    kill -9  $(cat /opt/apps/pid.file)
else
    echo "app not started!"
    ls -lrth
fi

JENKINS_NODE_COOKIE=dontKillMe   nohup java -jar ${JAVA_OPTS} /opt/apps/app.jar  >>/opt/apps/logs/startup.log 2>&1 &
echo $! > /opt/apps/pid.file 2>&1
  curl -k --retry-delay 10 --retry 5 --retry-connrefused https://devappserver:8585/api/v1/actuator/info  -o ${WORKSPACE}/response.json
    http_response=$(curl -k -s -o response.json -w "%{http_code}" https://devappserver:8585/api/v1/actuator/health )
   echo "response code: $http_response"
   if [ $http_response != "200" ]; then
        # handle error
        exit 1
    else
        echo "Server returned:"
        cat response.json
    fi
