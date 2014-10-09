TPC-H quickstart demonstrates federated performance of Teiid server against 2 running Postgresql servers.


-------------------
System requirements
-------------------
Bash (Linux host), Apache Maven, DBGEN (http://www.tpc.org/tpch/spec/tpch_2_17_0.zip), Postgresclient (psql command)


#########################################
### Setup and Deployment
#########################################

1. The DBGEN tool must be compiled. First rename makefile.suite to makefile and change the following settings in it
```
CC = gcc  
DATABASE= SQLSERVER  
MACHINE = LINUX  
WORKLOAD = TPCH  
```

Then you can run

```
make
```

2. Generated data set 
  1. Open configuration file tpch/setenv.sh and change value of DBGEN variable
  2. run
```
  ./generate-data.sh <scale>
``` 
where scale is a flot number. It is a number of gigabytes of data to generate.
  3. Two postgresql servers must be running. For each of one, run the following command to load the data
```
    load-data-into-db.sh <hostname> <port> <dbname> <username>
```

4. The Teiid server must be running with 2 datasources configured. One data datasource per Postgresql instance. 
5. VDB config-files/tpch-vdb.xml must be reconfigured to use the two datasource and then be deployed

#########################################
### Running the performance test
#########################################
1. Open configuration file tpch/setenv.sh and change value of DRIVER_PATH, TEIID_USER, TEIID_PASSWD variables. Also make sure DBGEN variable is valid

2. run 
```
 ./generate-queries.sh <scale>
```
Where scale is an integer that determines volume of query sets to generate. The final number of queries generated will be scale*22

3. run
```
 ./run-test.sh
```
