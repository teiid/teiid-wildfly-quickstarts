#!/bin/bash

source setenv.sh

QID=$1

if [ -z $QID ] 
 then
 echo "Usage: ./generate-one-query.sh <query-id>"
 exit 1;
fi

cd $QUERY_TEMPLATES
$DBGEN/qgen -s 1 $QID > q.txt
#Remove header and footer lines
sed -i '1 d' q.txt
cat q.txt

