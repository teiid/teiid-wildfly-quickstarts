= Infinispan Remote-Cache Materialization Quickstart using Hot Rod client that supports Google Protocol Buffers for Serialization

|===
|Level |Technologies |Target Product |Product Versions |Source

|Intermediate
|Teiid, Infinispan, Hot Rod, Remote Query, Protobuf Annotations
|Teiid 9.3+, Infinispan 9+ (JDG 7.1+)
|
|https://github.com/teiid/teiid-quickstarts
|===

== What is it?

This quickstart demonstrates how to use the Infinispan-Hotrod translator and resource-adapter for materialization.

There are 2 examples in this quickstart that demonstrate the following materialization options:
*  Materialize Cache - this is the standard usecase for materializing a view into the cache to improve performance
*  Merge Cache - the merge cache can be used to insert or update a cache, but no deletes are performed on the cache.  This usecase  


== System requirements

* link:../README.adoc#_downloading_and_installing_java[Java]
* link:../README.adoc#_downloading_and_installing_maven[Maven]
* link:../README.adoc#_downloading_and_installing_teiid[Teiid Server]
* link:../simpleclient/README.adoc[Simple Client]
* JDG server kit (used as the remote server)

NOTE: You can obtain JDG kit distributions on Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html

== Setup

=== 1. JDG setup

* Install the Infinispan server

* Start JDG Server

Assumptions:
- It is assumed that you will be running both servers on the same box, therefore, start the JDG server by using the port offset argument:  -Djboss.socket.binding.port-offset=
- That majority of usecases will be using the cache in a distributed fashion, so the clustered configuration will be used

To start the server

[source,xml]
.*Example*
----
./standalone.sh -c clustered.xml -Djboss.socket.binding.port-offset=100
----

For the purpose of this quick start, it assumes running both servers on the same machine and is expecting the JDG server to have its ports incremented. The port adjustment has been made in the setup.cli script to match the above offset.


* Configure JDG Cache

Note:  The clustered.xml configuration will be used, instead of the standalone.xml, because use cases will generally expect to be using a clustered environment.

You have 2 options for configuring the cache:
 
a) running CLI script to configure cache
b) editing standalone configuration(requires restarting the infinipan server)

a) Perform the following steps to configure the 3 caches by running the CLI script against the JDG server

-  perform the below step, to start the infinispan server.

-  locate the ./src/jdg/setup-infinispan-caches.cli script in the quickstart
-  cd to the ${ISPN_HOME}/bin directory
-  execute the setup-jdg-cache.cli script by running the following:  

	./cli.sh  --file={path.to.cli.script}/setup-infinispan-caches.cli

Since the server is running with the offset, use the -c option:

	./cli.sh -c 127.0.0.1:10099 --file=./setup-infinispan-caches.cli
	 

Note the names of the cache - _stockCache_ and _st_stockCache_ , as they need to match the teiid-ispn Option property in the source model of the vdb..


b) Edit the clustered.xml configuration at ${JDG_HOME}/standalone/configuration.   Copy into the "clustered" section into the configuration  within the infinispan:server subsystem.

[source,xml]
----
        <subsystem xmlns="urn:infinispan:server:core:8.3">
            <cache-container name="clustered" default-cache="default" statistics="true">
                .... 
 		<replicated-cache name="teiid-alias-naming-cache" configuration="replicated"/>
                <distributed-cache name="stockCache" />
		<distributed-cache name="st_stockCache" />
                ....
            </cache-container>
        </subsystem>
----

=== 2. [PreRequistes] Dynamicvdb-datafederation quickstart

*  Install dynamicvdb-datafederation quickstart [../../dynamicvdb-datafederation/README.md]

This quickstart is utilized for 2 purposes:
-  to provide the data that will be used to materialize
-  extends the VDB to create the view that will be materialized.
 
=== 3.  Setup Teiid Server


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

=== 4. Configure JDG connection


Setup the JDG Cache, to be accessed as a data source, by using the quickstart CLI script to configure the resource adapter. 

[source]
.*Setup JDG Data Source*
----
cd $\{JDG_HOME}/bin
./jboss-cli.sh --connect --file=${PATH}/teiid-quickstarts/infinispan-hotrod-as-mat-cache/src/scripts/create-infinispan-hotrod-protobuf-ds.cli
----


=== 5. Deploy the VDB

Option 1: using materialize cache

	- copy both files, infinispan-mat-cache-vdb.xml and infinispan-mat-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

Option 2: using Merge

	- copy both files, infinispan-mat-merge-vdb.xml and infinispan-mat-merge-vdb.dodeploy to {jbossas.server.dir}/standalone/deployments	


The materialization process should begin immediately and the refresh of the cache will be done on 5 second intervals.
	


== Query Demonstrations

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
