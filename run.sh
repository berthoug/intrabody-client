#!/bin/bash
#echo "Downloading Bluetooth dependencies..."
#wget https://github.com/gth828r/sprime/blob/master/sprime-bt/bluecove/bluecove-2.1.1-SNAPSHOT.jar
#wget https://github.com/gth828r/sprime/blob/master/sprime-bt/bluecove/bluecove-bluez-2.1.1-SNAPSHOT.jar
#wget https://github.com/gth828r/sprime/blob/master/sprime-bt/bluecove/bluecove-gpl-2.1.1-SNAPSHOT.jar

#echo "Creating local maven repo..."
#mkdir intrabody-local-repo
#mv bluecove-2.1.1-SNAPSHOT.jar intrabody-local-repo
#mv bluecove-bluez-2.1.1-SNAPSHOT.jar intrabody-local-repo
#mv bluecove-gpl-2.1.1-SNAPSHOT.jar intrabody-local-repo

#echo "Add bluetooth dependencies in  local repo"
#mvn install:install-file -Dfile=./intrabody-local-repo/bluecove-2.1.1-SNAPSHOT.jar -DgroupId=net.sf.bluecove -DartifactId=bluecove -Dversion=2.1.1-SNAPSHOT -Dpackaging=jar -DlocalRepositoryPath=~/.m2/repository
#mvn install:install-file -Dfile=./intrabody-local-repo/bluecove-bluez-2.1.1-SNAPSHOT.jar -DgroupId=net.sf.bluecove -DartifactId=bluecove-bluez -Dversion=2.1.1-SNAPSHOT -Dpackaging=jar -DlocalRepositoryPath=~/.m2/repository
#mvn install:install-file -Dfile=./intrabody-local-repo/bluecove-gpl-2.1.1-SNAPSHOT.jar -DgroupId=net.sf.bluecove -DartifactId=bluecove-gpl -Dversion=2.1.1-SNAPSHOT -Dpackaging=jar -DlocalRepositoryPath=~/.m2/repository

java -Dfile.encoding=UTF-8 -classpath ./target/classes:bluecove-2.1.1-SNAPSHOT.jar:bluecove-bluez-2.1.1-SNAPSHOT.jar:bluecove-gpl-2.1.1-SNAPSHOT.jar io.hops.intrabody.IntrabodySPPClient

