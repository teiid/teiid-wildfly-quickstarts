#!/bin/bash

source setenv.sh

if hash  $DBGEN/dbgen 2>/dev/null; then
       echo "DBGEN Detected"
    else
       echo "ERROR: to generate data you need to compile DBGEN package from TPC-H (with dbgen executable) and configure paths in setenv.sh"
       exit 1;
fi

SCALE=$1

if [ -z $SCALE ] 
 then
 echo "Usage: ./generate-data.sh <scale>"
 echo "   scale: number that controls volume of size in gigabytes. "
 echo "   A scale one (1) will generate 8 tables (made up of 150k Customers, 6million Line Items and 1.5million Orders)"
 exit 1;
fi

rm -rf $GENERATED_DATA
mkdir $GENERATED_DATA

echo ""
echo "About to generate $SCALE gigabytes of data into directory $GENERATED_DATA"
echo "Please wait..."

cd $DBGEN
./dbgen -s $SCALE
mv *tbl $GENERATED_DATA

#Remove last pipe from each row in each file
for f in $GENERATED_DATA/*.tbl ; do sed -i 's/.$//' $f; done

echo "Done."
echo ""
