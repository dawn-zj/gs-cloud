#!/bin/bash

NETTOOLHOME="/opt/gs/NetTool"
current_path=`pwd`

keyword="/etc/rc3.d/Sserver.sh"
output=`cat /etc/rc.local`

if [[ $output != *$keyword* ]];then
        echo $keyword >> "/etc/rc.local"
fi

cd $current_path/shell
chmod +x *.sh
\cp *.sh /etc/rc3.d/.

#check install path
if [ -d "$NETTOOLHOME" ];then
   echo "clean directory file"	
   rm -rf $NETTOOLHOME
else
   echo "create directory ..."	
fi

#create install path
mkdir -p $NETTOOLHOME

cd $current_path

echo " copy jre ..."
cp -r jre "$NETTOOLHOME"/

echo " copy tomcat_web ..."
cp -r tomcat_web "$NETTOOLHOME"/

echo " copy conf ... "
cp -r conf "$NETTOOLHOME"/

echo " copy backup ..."
cp -r backup "$NETTOOLHOME"/

echo "NetTool install success..."
