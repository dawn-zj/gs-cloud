#!/bin/bash

defpath="/opt/gs/NetTool"

#PATH="$PATH:${defpath}/tomcat_app/bin/:${defpath}/tomcat_web/bin/:${defpath}/jdk1.8.0_121/bin:${defpath}/jdk1.8.0_121/jre/bin"
PATH="$PATH:${defpath}/tomcat_app/bin/:${defpath}/jdk1.8.0_121/bin:${defpath}/jdk1.8.0_121/jre/bin"
JAVA_HOME=${defpath}/jdk1.8.0_121/
export JAVA_HOME
JRE_HOME=${defpath}/jdk1.8.0_121/jre/
export JRE_HOME

TOMCAT_HOME_WEB=${defpath}/tomcat_web/
export TOMCAT_HOME_WEB
export CATALINA_HOME_WEB=${defpath}/tomcat_web


#TOMCAT_HOME_APP=${defpath}/tomcat_app/
#export TOMCAT_HOME_APP
#export CATALINA_HOME_APP=${defpath}/tomcat_app

#CLASSPATH=${defpath}/tomcat_web/lib:${defpath}/tomcat_app/lib:${defpath}/jdk1.8.0_121/jre/lib:.
CLASSPATH=${defpath}/tomcat_web/lib:${defpath}/jdk1.8.0_121/jre/lib:.
export CLASSPATH

#TWO_FACTOR_PID=`ps -elf|grep java|grep twoFactor|awk '{print $4}'`
#if [ -n $TWO_FACTOR_PID ];then
#        kill -9 $TWO_FACTOR_PID
#fi

cd ${defpath}/tomcat_web/bin
./shutdown.sh

#cd ${defpath}/tomcat_app/bin
#./shutdown.sh
