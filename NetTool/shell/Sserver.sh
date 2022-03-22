#!/bin/bash

defpath="/opt/gs/NetTool"
currentpath="/etc/rc3.d"

#PATH="$PATH:${defpath}/tomcat_web/bin/:${defpath}/tomcat_app/bin/:${defpath}/jdk1.8.0_121/bin:${defpath}/jdk1.8.0_121/jre/bin"
PATH="$PATH:${defpath}/tomcat_web/bin/:${defpath}/jdk1.8.0_121/bin:${defpath}/jdk1.8.0_121/jre/bin"
JAVA_HOME=${defpath}/jdk1.8.0_121
export JAVA_HOME
JRE_HOME=${defpath}/jdk1.8.0_121/jre
export JRE_HOME

TOMCAT_HOME_WEB=${defpath}/tomcat_web
export TOMCAT_HOME_WEB
export CATALINA_HOME_WEB=${defpath}/tomcat_web

#TOMCAT_HOME_APP=${defpath}/tomcat_app
#export TOMCAT_HOME_APP
#export CATALINA_HOME_APP=${defpath}/tomcat_app

#CLASSPATH=${defpath}/tomcat_web/lib:${defpath}/tomcat_app/lib:${defpath}/jdk1.8.0_121/jre/lib:.
CLASSPATH=${defpath}/tomcat_web/lib:${defpath}/jdk1.8.0_121/jre/lib:.
export CLASSPATH

TPORT_TOMCAT_WEB=`netstat -ntlp | awk '{print $4}' | grep -i '8443'`
#TPORT_TOMCAT_APP=`netstat -ntlp | awk '{print $4}' | grep -i '8444'`

if [ -z "$TPORT_TOMCAT_WEB" ];then
 cd ${defpath}/tomcat_web/bin
 ./startup.sh
else
	echo "The port 8443 has been in use."
fi

#if [ -z "$TPORT_TOMCAT_APP" ];then
# cd ${defpath}/tomcat_app/bin
# ./startup.sh
#else
#	echo "The port 8444 has been in use."
#fi


