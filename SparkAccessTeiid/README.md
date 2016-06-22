---
Level: Beginners
Technologies: Teiid, Spark
Target Product: DV
Product Versions: DV 6.0+
Source: https://github.com/teiid/teiid-quickstarts
---

Spark-Access-Teiid Quickstart
================================

## What is it?

Spark-Access-Teiid demonstrates that how to use teiid-jdbc to connect teiid server, so that teiid can as data source for spark.
 

*  VDB:   PortfolioMaterialize  
 

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)


# PREREQUISTES

###NOTE: 

* This example relies upon the vdb-datafederation example and that it needs to be deployed prior to running this example. Therefore, read the vdb-datafederation's README.md and follow its directions before continuing.
* This example relies [Spark Pre-built for Hadoop 2.6](http://spark.apache.org/downloads.html)


## 1.Spark Installation

 Install Spark Pre-build for Hadoop 2.6. If you have aready install Hadoop with other version, you need install appropriate version of Spark.


## 2.VDB Deployment


1) Run the setup in vdb-datafederation quick start

2)  Start the server, if not already started

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration
		
	-c {configuration.file}  
	
	Example: -c standalone-teiid.xml 

3) VDB Deployment:

    Copy the following files to the "<jboss.home>/standalone/deployments" directory

     * src/vdb/portfolio-mat-vdb.xml
     * src/vdb/portfolio-mat-vdb.xml.dodeploy


4)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	


## Spark Access To Teiid Demonstrations

###1. Copy Spark Jar
copy $SPARK_HOME/lib/spark-assembly-1.6.1-hadoop2.6.0 to ./lib/

~~~
cp $SPARK_HOME/lib/spark-assembly-1.6.1-hadoop2.6.0 ./lib/
~~~

###2.Build Profile

 mvn clean package

###2.submit to spark

* cd $SPARK_HOME 

* ./bin/spark-submit --class oorg.teiid.quickstarts.SparkAccessTeiid.ConsumeTeiid --driver-class-path ${Spark-Access-Teiid.build.directory}/target/lib/teiid-9.0.0.Final-jdbc.jar ${Spark-Access-Teiid.build.directory}/target/original-SparkAccessTeiid-0.0.1-SNAPSHOT.jar

 