#!/bin/bash

#Red Hat testing servers
#db02.mw.lab.eng.bos.redhat.com 5432 bqt2 bqt2
#postgresql02.mw.lab.eng.bos.redhat.com 5432 bqt2 bqt2


# Path to the directory that contains DBGEN binaries. e.g. tpch_2_17_0/dbgen 
DBGEN=/home/fnguyen/work/edsqa/temp/tpch/tpch_2_17_0/dbgen
# Path to the jar file with JDBC driver of your Teiid Server
DRIVER_PATH=/home/fnguyen/work/edsqa/builds/DV-6.1.0/jboss-eap-6.3/dataVirtualization/jdbc/teiid-8.7.1.redhat-3-jdbc.jar
TEIID_USER=teiidUser
TEIID_PASSWD=Ahojahoj1.
#The following three variables need not to be changed 
QUERY_TEMPLATES=$(pwd)/query-templates
GENERATED_QUERIES=$(pwd)/generated-queries
GENERATED_DATA=$(pwd)/generated-data

