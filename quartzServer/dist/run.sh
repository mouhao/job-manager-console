#! /bin/bash

BASEDIR=`dirname $0`
cd $BASEDIR

#PID文件路径
PIDFILE=$BASEDIR/.QuartzServer.pid


CLASSPATH="."

for jar in ` find $BASEDIR -type f  -name "*.jar" `
do
	CLASSPATH=$CLASSPATH:$jar
done


MAINCLASS=org.quartz.standalone.server.startup.Bootstrap

java -cp $CLASSPATH $MAINCLASS $PIDFILE $1 $2 $3 &
