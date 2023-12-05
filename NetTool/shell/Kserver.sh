#!/bin/bash

defpath="/opt/gs/NetTool"

PATH="$PATH:${defpath}/tomcat_web/bin/:${defpath}/jre/bin"
JRE_HOME=${defpath}/jre/
export JRE_HOME

TOMCAT_HOME_WEB=${defpath}/tomcat_web/
export TOMCAT_HOME_WEB
export CATALINA_HOME_WEB=${defpath}/tomcat_web

CLASSPATH=${defpath}/tomcat_web/lib:${defpath}/jre/lib:.
export CLASSPATH

cd ${defpath}/tomcat_web/bin
./shutdown.sh
