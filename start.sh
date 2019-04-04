#!/bin/bash
export JAVA_HOME=/mnt/burzum-storage/jdk1.7.0_79
export PATH=$JAVA_HOME/bin:$PATH

echo JAVA_HOME=$JAVA_HOME
echo PATH=$PATH

#java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5050

CLASSPATH=./stateengine_lib/commons-io-2.4.jar:./stateengine_lib/fluent-hc-4.5.1.jar:./stateengine_lib/httpclient-4.5.1.jar:./stateengine_lib/httpcore-4.4.3.jar:./stateengine_lib/jcl-over-slf4j-1.7.2.jar:./stateengine_lib/libsocket-can-java.jar:./stateengine_lib/log4j-1.2.16.jar:./stateengine_lib/slf4j-api-1.7.2.jar:./stateengine_lib/slf4j-log4j12-1.6.6.jar
export CLASSPATH=./stateengine.jar:$CLASSPATH

java pl.redeem.jeep.picar.StartStateEngine

