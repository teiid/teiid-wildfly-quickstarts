| **Datasource** | **Level** | **Technologies** | **Prerequisites** | **Description** |
|:---------|:----------|:-----------------|:------------------|:----------------|
|Hadoop, Hive |Beginner |Teiid, Dynamic VDB, View, Hive Translator |Hadoop Services, HiveServer2 |Demonstrates using Hive translator to access data in Hadoop | Hive

## What's this

This example demonstrates using the Hive translator with HiveServer2 JDBC Driver to access data in Hadoop HDFS. Dynamic VDB [hive-vdb.xml](src/vdb/hive-vdb.xml) be used to define View within DDL metadata.

The examples use `java:/HiveDS` referenced with Hive data source, which will be setup by running [setup.cli](src/scripts/setup.cli)

## Prerequisites

###1.Setup Hadoop/Hive

**1.** Hadoop Services

Refer to [teiid-embedded-examples/README.md](../../teiid-embedded-examples/blob/master/bigdata-integration/README.md) **Prerequisites** section to install Hadoop and start Hadoop Services


**2.** Hive Services 

Refer to [teiid-embedded-examples/README.md](../../teiid-embedded-examples/blob/master/bigdata-integration/README.md) **Prerequisites** section to install Hive and start HiveServer2

 
**3.** Create Hive Table 
With above 2 setps use the following commands to load [hive-sample.txt](src/main/resources/hive-sample.txt):

~~~
$ ./bin/hive
hive> CREATE TABLE IF NOT EXISTS employee (eid int, name String, salary String, destination String);
hive> LOAD DATA LOCAL INPATH '{path}/vdb-Hivehadoop/src/data/hive-sample.txt' OVERWRITE INTO TABLE employee;
~~~
 


###2. Setup Teiid Server
  Assumed that you have already installed Teiid,and added users. If not, you can use
  
~~~
  $ ./bin/add-user.sh -a -u teiidUser -p password1! -g user  
  $ ./bin/add-user.sh admin password1!  
~~~


**1.** Hive driver

Install Hive driver module. Download TeiidModule-Hive12.zip and copy it into your server installation in the /jboss-eap-6.1/modules folder. unzip it. That's all. Do not modify the $JBOSS_HOME/standlone/configuration/standlone.xml. 

Refer to [ConnectToAHadoopSourceUsingHive2](https://developer.jboss.org/wiki/ConnectToAHadoopSourceUsingHive2)




**2.**  Start the server

To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

 
**3.** Setup the hive datasource adapter

-  run the following CLI script

	-	cd to the $JBOSS_HOME/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/vdb-Hivehadoop/src/scripts/setup.cli 
	
	Maybe you need modify the connection-url=jdbc:hive2://{host-name}:10000/default to your URL
	
	 Example : connection-url=jdbc:hive2://127.0.0.1:10000/default

**4.**  Teiid Deployment:

Copy (deploy) the following VDB related files to the $JBOSS_HOME/standalone/deployments directory

	* hive VDB
    	- src/vdb/hive-vdb.xml
    	- src/vdb/hive-vdb.xml.dodeploy
   

 
## Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   
~~~

mvn clean install

mvn exec:java -Dvdb="hadoop" -Dsql="SELECT * FROM EMPLOYEEVIEW" -Dusername="teiidUser" -Dpassword="password1!"
~~~ 



 
