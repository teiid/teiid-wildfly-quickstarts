#!/bin/bash

source setenv.sh

if hash psql 2>/dev/null; then
       echo "PSQL Detected"
    else
       echo "ERROR: to load data into Postgres database you firstly need to install Postgres client (psql command)"
       exit 1;
fi

HOST=$1
PORT=$2
DBNAME=$3
USERNAME=$4

if [ -z $USERNAME ] || [ -z $HOST ] || [ -z $PORT ] || [ -z $DBNAME ] 
 then
   echo "To load data you should specify the following arguments to acces the Postgres instance: "
   echo "   ./load-data-into-db <hostname> <port> <dbname> <username>"
   exit 1;
fi

echo ""
echo "File script.sql will be generated and then it will be run"
echo "Please wait..."


rm -rf script.sql

cat config-files/drop-create.sql > script.sql
cat config-files/postgres-dml.sql >> script.sql

for f in region nation customer supplier part partsupp orders lineitem;
do
  echo "\\copy tpch.$f FROM '$GENERATED_DATA/$f.tbl' DELIMITER '|' CSV;" >> script.sql
done

echo "About to run the SQL script, you will be prompted for password"
psql --username=$USERNAME -h $HOST --port=$PORT --dbname=$DBNAME -f script.sql

echo ""
echo "Data loaded. Done."
echo ""
