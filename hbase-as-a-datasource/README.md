---
Level: Beginners
Technologies: Teiid, HBase Translator, Foreign Table
Target Product: DV
Product Versions: DV 6.1+
Source: https://github.com/teiid/teiid-quickstarts
---

## What is it?

**hbase-as-a-datasource** demonstrates using the HBase Translator with Phoenix Data Source to access data in HBase.

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)


## Setup and Deployment

1)  Start the server (if not already started)

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	To use one of the high-available (ha) configurations for clustering, append the following arguments to the command to specify the configuration
		
	-c {configuration.file} 
	
	Example: -c standalone-ha.xml 

2)  Setup HBase

* Use [HBase quickstart steps](http://hbase.apache.org/book.html#quickstart) to install a single-node, standalone instance of HBase, for example

~~~
$ tar -xvf hbase-0.98.8-hadoop2-bin.tar.gz
$ cd hbase-0.98.8-hadoop2/
~~~

* Download Phoenix 4.x from [Phoenix Downloads Page](http://phoenix.apache.org/download.html), install Phoenix via copying phoenix-core.jar to HBase lib directory, for example

~~~
$ tar -xvf phoenix-4.2.1-bin.tar.gz
$ cp phoenix-4.2.1-bin/phoenix-core-4.2.1.jar hbase-0.98.8-hadoop2/lib/
~~~

* Start HBase and connect to HBase via shell, create table and add (put) sample data.  See [customer_sample_data.txt](src/scripts/customer_sample_data.txt) for example data.  

Example steps:

~~~
$ ./bin/start-hbase.sh
$ ./bin/hbase shell
hbase(main):002:0> create 'Customer', 'customer', 'sales'
hbase(main):003:0> put 'Customer', '101', 'customer:name', 'John White'
...
~~~

3) Setup Phoenix Data Source

NOTE:   The following referenced setup.cli script is configured to use the phoenix-4.2.1-client.jar.  If the version you are using is different, the setup.cli script will need to be updated.

* Copy phoenix-[version]-client.jar to $JBOSS_HOME

* Execute CLI command to setup Phoenix Data Source

~~~
$ cd $JBOSS_HOME
$ ./bin/jboss-cli.sh --connect --file=./quickstarts/hbase-as-a-datasource/setup.cli
~~~ 

* Use Phoenix Command Line to execute [customer-schema.sql]({$JBOSS_HOME}/quickstarts/hbase-as-a-datasource/src/teiidfiles/customer-schema.sql) to map `Customer` table in HBase, for example

~~~
$ cd $PHOENIX_HOME
$ ./bin/sqlline.py localhost ${JBOSS_HOME}/quickstarts/hbase-as-a-datasource/src/teiidfiles/customer-schema.sql
~~~

> NOTE - For more details about Phoenix Data Sources and Mapping Phoenix table to an existing HBase table please refer to [Teiid Documents](https://docs.jboss.org/author/display/TEIID/Phoenix+Data+Sources).

4)  Teiid VDB Deployment:

[source]
.*VDB deployment*
----
cd to the $JBOSS_HOME/bin directory
execute the following CLI script:

	./jboss-cli.sh --connect --file=../quickstarts/hbase-as-a-datasource/src/scripts/deploy_vdb.cli 
----

or can manually deploy the vdb by doing the following:

Copy the following files to the "$JBOSS_HOME/standalone/deployments" directory

     (1) src/vdb/hbase-vdb.xml
     (2) src/vdb/hbase-vdb.xml.dodeploy

5)  See "Query Demonstrations" below to demonstrate data query.


# 3. Query Demonstrations

NOTE: before querying, if not already, will need to add user/pasword.

1. Change your working directory to "${JBOSS_HOME}/quickstarts/simpleclient"
2. Use the simpleclient example to run the following queries:

Example: mvn exec:java -Dvdb="hbasevdb" -Dsql="SELECT * FROM Customer" -Dusername="teiidUser" -Dpassword="pwd"

> NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be complicated.  It would be better to install a Java client, such as SQuirreL, to run the queries.

or

Use a sql tool, like SQuirreL, to connect and issue following example query:

Queries for accessing hbasevdb

URL connect: jdbc:teiid:hbasevdb@mm://{host}:31000 

[source,sql]
.*Example Query SQL*
----

SELECT city, amount FROM Customer
SELECT DISTINCT city FROM Customer
SELECT city, amount FROM Customer WHERE PK='105'
SELECT * FROM Customer WHERE PK BETWEEN '105' AND '108'
SELECT * FROM Customer WHERE PK='105' AND name='John White'
SELECT * FROM Customer ORDER BY PK
SELECT * FROM Customer ORDER BY name, city DESC
SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city
SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city HAVING COUNT(PK) > 1
~~~

