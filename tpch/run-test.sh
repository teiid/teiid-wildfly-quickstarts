#!/bin/bash

source setenv.sh

if hash mvn 2>/dev/null; then
       echo "Maven detected"
    else
       echo "ERROR: to run the test you need Apache Maven installed (mvn command)"
       exit 1;
fi

cd javaPerfTest
mvn clean compile exec:java -Dgenerated-queries-dir=$GENERATED_QUERIES -Dteiid-user=$TEIID_USER -Dteiid-password=$TEIID_PASSWD -DjdbcDriverPath=$DRIVER_PATH


