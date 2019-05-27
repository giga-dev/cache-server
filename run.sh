#!/usr/bin/env bash


#mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dremote.base.url=http://hercules -DlocalDir=/tmp/downloads -Dserver.port=8001"

mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DlocalDir=/disk02/hercules2 -Dserver.port=80"
#mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dremote.base.url=http://gigaspaces-releases-eu.s3.amazonaws.com -DlocalDir=/disk02/hercules2 -Dserver.port=80"

#mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dremote.base.url=http://hercules -DlocalDir=/tmp/disk02/hercules3 -Dserver.port=8080 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=12345"
