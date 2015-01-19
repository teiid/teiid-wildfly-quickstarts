#!/bin/bash

# Path to the directory that contains DBGEN binaries. e.g. tpch_2_17_0/dbgen 
DBGEN=/home/rareddy/tools/tpch/tpch_2_17_0/dbgen

# Path to the jar file with JDBC driver of your Teiid Server
TEIID_USER=user
TEIID_PASSWD=user

#The following three variables need not to be changed 
QUERY_TEMPLATES=$(pwd)/query-templates
GENERATED_QUERIES=$(pwd)/generated-queries
GENERATED_DATA=$(pwd)/generated-data

