#! /bin/sh
set -e
set -x
echo "$Date Current user ::$USER"
CUR_PATH="${0%/*}"
pwd
NOW=`date +"-%Y/%m/%d %H:%M:%S-"`
composefile=$1
if [ -z "$composefile" ];then
composefile=docker-compose.yml
fi
echo  $composefile

if test -f "$composefile"; then
 #docker-compose -f $1 up -d
 # PG_ENV=dev; NAME=transcribeapp_db; VERSION=1.0.0; 
 docker-compose --env-file ./scripts/env.conf -f $composefile up -d
 
echo  "status :: $? "
	if [ $? == 0 ] ; then
		dkpsa
	else
 		echo "docker creation failed with "
		dkpsa
		exit 1
	fi
else
 echo "please mention docker compose file for transcriptiondb docker compose file "
fi
