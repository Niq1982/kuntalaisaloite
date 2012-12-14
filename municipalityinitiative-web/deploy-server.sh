#!/bin/bash
set -e

if [ $1 ]; then
	DEPLOY_VERSION=$1
else
	echo "Enter version which should be found from /deploy-inbox"
	read DEPLOY_VERSION	
fi

WAR_FILE=root-$DEPLOY_VERSION.war

sudo -u jetty -i

echo "Handling war-file $WAR_FILE"
cp "/opt/jetty/deploy-inbox/$WAR_FILE /opt/jetty/webapps/root.war"
mv "/opt/jetty/deploy-inbox/$WAR_FILE /opt/jetty/deploy/arc/."

sleep 10000;