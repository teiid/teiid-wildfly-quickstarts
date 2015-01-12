#!/bin/bash

source setenv.sh

if hash  $DBGEN/qgen 2>/dev/null; then
       echo "QGEN Detected"
    else
       echo "ERROR: to generate queries you need to compile DBGEN package from TPC-H (with dbgen executable) and configure paths in setenv.sh"
       exit 1;
fi

SCALE=$1

if [ -z $SCALE ] 
 then
 echo "Usage: ./generate-queries.sh <scale>"
 echo "   scale: number that controls size of queryset. This can be decimal number"
 exit 1;
fi

rm -rf $GENERATED_QUERIES
mkdir $GENERATED_QUERIES

echo ""
echo "About to generate 22 queries into directory $GENERATED_QUERIES"
echo "Please wait..."
for q in {1..22}
do	
	f=$GENERATED_QUERIES/r$q.sql
	./generate-one-query.sh $q $SCALE > $GENERATED_QUERIES/r$q.sql
done


echo "Done."
echo ""
