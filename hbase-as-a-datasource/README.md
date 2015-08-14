---
Level: Beginners
Technologies: Teiid, HBase Translator, Foreign Table
Target Product: DV
Product Versions: DV 6.1
Source: https://github.com/teiid/teiid-quickstarts
---

## What is it?

**hbase-as-a-datasource** demonstrates using the HBase Translator with Phoenix Data Source to access data in HBase.

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md) in the root quick starts directory.


## Setup and Deployment

1)  Start the server (if not already started)

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	append the following to the command to indicate which configuration to use if Teiid isn't configured in the default configuration
		
	-c standalone-teiid.xml 

2)  Setup HBase

* Using [HBase quickstart steps](http://hbase.apache.org/book.html#quickstart) to install a single-node, standalone instance of HBase, for example

~~~
$ tar -xvf hbase-0.98.8-hadoop2-bin.tar.gz
$ cd hbase-0.98.8-hadoop2/
~~~

* Download Phoenix 4.x from [Phoenix Downloads Page](http://phoenix.apache.org/download.html), install Phoenix via copying phoenix-core.jar to HBase lib directory, for example

~~~
$ tar -xvf phoenix-4.2.1-bin.tar.gz
$ cp phoenix-4.2.1-bin/phoenix-core-4.2.1.jar hbase-0.98.8-hadoop2/lib/
~~~

* Start HBase and connect to HBase via shell, create table and put sample data in [customer_sample_data.txt](src/scripts/customer_sample_data.txt), for example

~~~
$ ./bin/start-hbase.sh
$ ./bin/hbase shell
hbase(main):002:0> create 'Customer', 'customer', 'sales'
hbase(main):003:0> put 'Customer', '101', 'customer:name', 'John White'
...
~~~

3) Setup Phoenix Data Source

* Copy phoenix-[version]-client.jar to $JBOSS_HOME, copy [setup.cli](src/scripts/setup.cli)to $JBOSS_HOME, for example

~~~
$ cd $JBOSS_HOME
$ cp .../src/scripts/setup.cli ./
$ cp .../phoenix-4.2.1-bin/phoenix-4.2.1-client.jar ./
~~~

* Execute CLI commands to setup Phoenix Data Source

~~~
$ ./bin/jboss-cli.sh --connect --file=setup.cli
~~~ 

* Use Phoenix Command Line execute [customer-schema.sql](src/teiidfiles/customer-schema.sql) to map `Customer` table in HBase, for example

~~~
$ cd PHOENIX_HOME
$ ./bin/sqlline.py localhost .../src/teiidfiles/customer-schema.sql
~~~

> NOTE - More details about Phoenix Data Sources and Mapping Phoenix table to an existing HBase table please refer to [Teiid Documents](https://docs.jboss.org/author/display/TEIID/Phoenix+Data+Sources).

4) Teiid Deployment

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/hbase-vdb.xml
     (2) src/vdb/hbase-vdb.xml.dodeploy

5)  See "Query Demonstrations" below to demonstrate data query.


## Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="hbasevdb" -Dsql="SELECT * FROM Customer"

The following sql can be used with -Dsql:

~~~
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

> NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be complicated.  It would be better to install a Java client, such as SQuirreL, to run the queries. 
