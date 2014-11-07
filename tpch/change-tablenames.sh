#!/bin/bash

TABLE_PATTERN="tpch.\\.tpch\\."
TPCH1_NAME="tpch1.tpch."
TPCH2_NAME="tpch2.tpch."

#The following tables in the query templates will be renamed to come from model tpch1
for tab in region nation customer supplier
do 
  sed -i "s/$TABLE_PATTERN$tab/$TPCH1_NAME$tab/g" $(find query-templates -iname '*sql')
done


for tab in part partsupp orders lineitem 
do 
  sed -i "s/$TABLE_PATTERN$tab/$TPCH2_NAME$tab/g" $(find query-templates -iname '*sql')
done



