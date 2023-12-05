#!/bin/bash

defpath="/opt/gs/NetTool"
currentpath="/etc/rc3.d"

PATH="$PATH:${defpath}/tomcat_web/bin/:${defpath}/jre/bin"
JRE_HOME=${defpath}/jre
export JRE_HOME

TOMCAT_HOME_WEB=${defpath}/tomcat_web
export TOMCAT_HOME_WEB
export CATALINA_HOME_WEB=${defpath}/tomcat_web

CLASSPATH=${defpath}/tomcat_web/lib:${defpath}/jre/lib:.
export CLASSPATH

TPORT_TOMCAT_WEB=`netstat -ntlp | awk '{print $4}' | grep -i '8443'`

if [ -z "$TPORT_TOMCAT_WEB" ];then
 cd ${defpath}/tomcat_web/bin
 ./startup.sh
# ./catalina.sh jpda start
else
	echo "The port 8443 has been in use."
fi
