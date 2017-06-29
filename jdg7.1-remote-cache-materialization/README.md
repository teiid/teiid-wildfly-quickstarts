---
Level: Intermediate
Technologies: Teiid, Infinispan, Hot Rod, Remote Query, Materialization
Target Product: DV
Product Versions: DV 6.4+, JDG 7.1+
Source: https://github.com/teiid/teiid-quickstarts
---

JDG Remote-Cache Materialization Quickstart using JDG Hot Rod that supports Google Protocol Buffers for Serialization
================================


# What is it?

This quickstart demonstrates how to use the Infinispan-Hotrod translator and resource-adapter for materialization. 

There are 2 examples in this quickstart that demonstrate the following materialization options:
*  Delete before - where the cache is cleared out and then written to, which will block all reads while this is being performed
*  Merge - where the cache will be inserted or updated, but no deletes are performed on the cache


# Quick Start requirements

-  If you have not done so, please review the System Requirements [../README.md](../README.md) 
-  For Red Hat product related kits, will need to access Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html


# JDG setup

1.  Setup JDG Server
	
-  Install the JDG server
*  Configure JDG Cache

You have 2 options for configuring the cache: a) editing standalone configuration or b) running CLI script to configure cache


a) Edit the standalone.xml configuration at ${JDG_HOME}/standalone/configuration.   Copy in the "local-cache" section into the configuration  within the infinispan:server subsystem.

[source,xml]
----
        <subsystem xmlns="urn:infinispan:server:core:8.3">
            <cache-container name="local" default-cache="default" statistics="true">
                .... 
                <local-cache name="stockCache" start="EAGER">
                    <locking striping="false" acquire-timeout="30000" concurrency-level="1000"/>
                    <transaction mode="NONE"/>
                </local-cache>
                 ....
            </cache-container>
        </subsystem>
----


b) Perform the following steps to configure the cache by running the CLI script against the JDG server

-  Start the JDG servr by performing the step after this.

-  locate the ./src/jdg/setup-jdg-cache.cli script in the quickstart
-  cd to the ${JDG_HOME}/bin directory
-  execute the setup-jdg-caches.cli script by running the following:  

	./cli.sh  --file={path.to.cli.script}/setup-jdg-caches.cli

Since the server is running with the offset, use the -c option:

	 ./cli.sh -c 127.0.0.1:10099 --file=./setup-jdg-caches.cli
	 

Note the name of the cache - stockCache  as it will used when defining the cache for the model in the VDB.

This configuration is not persisting the data, so when the JDG server is restarted the data will be lost.  See the JDG documentation on configuring a persistent cache.


2.  Starting JDG Server

It is assumed that you will be running both servers on the same box, therefore, start the JDG server by adding the following command line argument: 

-Djboss.socket.binding.port-offset=100

[source,xml]
.*Example*
----
./standalone.sh -Djboss.socket.binding.port-offset=100
----

* The following shows the command line to start the JDG server with the port offset:

        For Linux:   $JDG_HOME/bin/standalone.sh -Djboss.socket.binding.port-offset=100
        For Windows: %JDG_HOME%\bin\standalone.bat -Djboss.socket.binding.port-offset=100

For the purpose of this quick start, it assumes running both servers on the same machine and is expecting the JDG server to have its ports incremented. The port adjustment has been made in the _Setup JDG Data Source_ step to match the above offset.


#  [PreRequistes] Dynamicvdb-datafederation quickstart

*  Install dynamicvdb-datafederation quickstart [../../dynamicvdb-datafederation/README.md]

This quickstart is utilized for 2 purposes:
-  to provide the data that will be used to materialize
-  extends the VDB to create the view that will be materialized.
 


# Start Teiid Server


To start the server, if not already, open a command line and navigate to the "bin" directory under the root directory of the Teiid server and run:

[source,xml]
----
./standalone.sh //For Linux
standalone.bat //for Windows
----

If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration `-c {configuration.file}`

[source,xml]
.*Example*
----
./standalone.sh -c standalone-teiid.xml
----


* Install the infinispan-hotrod translator

----
cd $\{JBOSS_HOME}/bin
./jboss-cli.sh --connect --file=../docs/teiid/datasources/infinispan-hotrod-7.1/add-infinispan-hotrod-translator.cli
----


* Setup the JDG Cache to be accessed as a data source

The quickstart has a CLI script that can be used to configure the resource adapter. 

[source]
.*Setup JDG Data Source*
----
cd $\{JDG_HOME}/bin
./jboss-cli.sh --connect --file=${PATH}/teiid-quickstarts/jdg7.1-remote-cache-materialization/src/scripts/create-infinispan-hotrod-protobuf-ds.cli
----


* deploy the VDB

Option 1: using Delete before

	- copy both files, jdg-mat-deletebefore-vdb.xml and jdg-mat-deletebefore-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

Option 2: using Merge

	- copy both files, jdg-mat-merge-vdb.xml and jdg-mat-merge-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


The materialization process should begin immediately and the refresh of the cache will be done on 5 second intervals.
	


# Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example: mvn exec:java -Dvdb="StockMat" -Dsql="select product_id, symbol, price, company_name from StocksMatView.Stock" -Dusername="teiidUser" -Dpassword="pwd"

or 

Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:StockMat@mm://localhost:31000)

# Test Queries

1.  Query for reading from the materialized cache

*  select * from StocksMatView.Stock


2.  To test the materialization process, do the following:

perform an insert to the Product table:

INSERT INTO "Accounts"."PRODUCT" (ID, SYMBOL, COMPANY_NAME) VALUES (2000, 'RHT', 'Red Hat Inc.');
commit;

(needed the commit when using SQuirrel).

*  wait 10 seconds because the refresh rate is set at 5 seconds.

reissue query:  select * from StocksMatView.Stock
