#!/bin/bash

source setenv.sh

QID=$1
SCALE=$2

if [ -z $QID ] 
 then
 echo "Usage: ./generate-one-query.sh <query-id> <scale>"
 exit 1;
fi

cd $QUERY_TEMPLATES
$DBGEN/qgen -s $SCALE $QID | sed  '1 d'
