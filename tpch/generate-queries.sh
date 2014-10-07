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
 echo "   scale: number that controls size of queryset. Number of generated queries will equal to 22*scale"
 exit 1;
fi

rm -rf $GENERATED_QUERIES
mkdir $GENERATED_QUERIES

echo ""
echo "About to generate $(( 22 * $SCALE )) queries into directory $GENERATED_QUERIES"
echo "Please wait..."
for (( i=1; i<=$SCALE; i++ ))
do	
	for q in {1..22}
	do	
		f=$GENERATED_QUERIES/r$i$q.sql
		./generate-one-query.sh $q > $GENERATED_QUERIES/r$i-$q.sql
	done
done

echo "Done."
echo ""
