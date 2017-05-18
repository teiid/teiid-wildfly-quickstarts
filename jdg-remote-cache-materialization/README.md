---
Level: Intermediate
Technologies: Teiid, Infinispan, Hot Rod, Remote Query, Materialization
Target Product: DV
Product Versions: DV 6.1+, JDG 6.5+
Source: https://github.com/teiid/teiid-quickstarts
---

JDG Remote-Cache Materialization Quickstart using JDG Hot Rod that supports Google Protocol Buffers for Serialization
================================

There are 2 options for configuring the JDG schema in DV, using protobuf (.proto) file and marshaller(s) or using the JDG 6.6 
feature of using protobuf annotations defined in the pojo.   This example is using the later and therefore, requires
that you use JDG 6.6.

NOTE:  The JDG 6.6.1 patch must be applied to the JDG 6.6 Hot Rod client EAP module kit, that is installed in DV server.


# What is it?

This quickstart demonstrates how Teiid can connect to a remote JBoss Data Grid (JDG) to use as a caching data source to improve query performance. 


# Quick Start requirements

-  If you have not done so, please review the System Requirements [../README.md](../README.md) 
-  For Red Hat product related kits, will need to access Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html


# PREREQUISTES

1.  Teiid Server Prerequistes

* install JBoss application server to run Teiid (or install the JBoss Data Virtualization product distribution, download from the Red Hat Customer Portal)
* install the Teiid Jboss distribution kit into the JBoss server (This will have already been done if JDV kit is being used).
* The dynamicvdb-datafederation quickstart [../../dynamicvdb-datafederation/README.md] needs to be installed.

2.  JDG Server Prerequistes

> NOTE: You can obtain JDG kit distributions on Red Hat's Customer Portal

* JDG 6.x JDG Hot Rod Client EAP modules kit (used by Teiid to access the remote cache)
* JDG 6.x server kit (used as the remote server)

>NOTE:  To use 6.6, you will need the 6.6.1 patch.


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
                <local-cache name="primaryCache" start="EAGER">
                    <locking striping="false" acquire-timeout="30000" concurrency-level="1000"/>
                    <transaction mode="NONE"/>
                </local-cache>
                  <local-cache name="stagingCache" start="EAGER">
                    <locking striping="false" acquire-timeout="30000" concurrency-level="1000"/>
                    <transaction mode="NONE"/>
                </local-cache>
                  <local-cache name="aliasCache" start="EAGER">
                    <locking striping="false" acquire-timeout="30000" concurrency-level="1000"/>
                    <transaction mode="NONE"/>
                </local-cache>                            ....
            </cache-container>
        </subsystem>
----


b) Perform the following steps to configure the 3 caches by running the CLI script against the JDG server

-  perform Step 2, below, to start the server.

-  locate the ./src/jdg/setup-jdg-cache.cli script in the quickstart
-  cd to the ${JDG_HOME}/bin directory
-  execute the setup-jdg-caches.cli script by running the following:  

	./cli.sh  --file={path.to.cli.script}/setup-jdg-caches.cli

Since the server is running with the offset, use the -c option:

	 ./cli.sh -c 127.0.0.1:10099 --file=./setup-jdg-caches.cli
	 

Note the name of the 3 caches: _primaryCache_, _stagingCache_, _aliasCache_

These cache names will be used when configuring the JDG connector in Teiid.

This configuration is not persisting the data, so when the JDG server is restarted the data will be lost.  See the JDG documentation on configuring a persistent cache.

2.  Starting JDG Server

>Note: For the purpose of this quick start, it assumes running both servers on the same machine and is expecting the JDG server to have its ports incremented.  The port adjustment has been made when configuring the JDG connector in Teiid.

*  Start the JDG server by adding the following command line argument:
	*  -Djboss.socket.binding.port-offset=100

Example:   ./standalone.sh -Djboss.socket.binding.port-offset=100

* The following shows the command line to start the JDG server with the port offset:

        For Linux:   $JDG_HOME/bin/standalone.sh -Djboss.socket.binding.port-offset=100
        For Windows: %JDG_HOME%\bin\standalone.bat -Djboss.socket.binding.port-offset=100


#  [PreRequistes] Dynamicvdb-datafederation quickstart

*  Install dynamicvdb-datafederation quickstart [../../dynamicvdb-datafederation/README.md]

This quickstart is utilized by extending the VDB to create the view that will be materialized.
 


# Teiid Quickstart Setup

1.  build the jdg-remote-cache-materialization quickstart

-  run  mvn -s ./settings.xml clean install

-  After building the quickstart, the jdg-remote-cache-materialization-pojos-jboss-as7-dist.zip should be found in the target directory.
-  This zip will be used later, where it is deployed to the Teiid server.


# Setup Teiid Server

1. shutdown jbossas server, if not already.

*  Install the JBoss Data Grid Hot Rod Client EAP modules kit into {jbossas-dir}/modules/ of your Teiid/EAP instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.

*  Install pojo Module  

		unzip the target/jdg-remote-cache-materialization-pojos-jboss-as7-dist.zip at the {jbossas-dir}/ root directory

*  Start Teiid Server
 
To start the server, open a command line and navigate to the "bin" directory under the root directory of the Jbossas server and run:
		
	-  For Linux:   ./standalone.sh 
	-  For Windows: standalone.bat 
   
   If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration to use.
		
	-c {configuration.file} 
	
    Example: ./standalone.sh -c standalone-teiid.xml 

* install the infinispan-dsl translator 

The infinispan translators are not installed by default because the dependent JDG related modules may not have been installed. To install do the following steps:

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute the add-infinispan-cache-dsl-translator.cli script by running the following:  

		./jboss-cli.sh --connect --file=../docs/teiid/datasources/infinispan/add-infinispan-cache-dsl-translator.cli 

* configure the infinispan-dsl resource adapter 

The following steps will configure the infinispan resource-adapater to communicate for reading and writing to a remote cache.

>Note:  this cli script was copied from the quickstart to the server when the pojo module was deployed

-	cd to the ${JBOSS_HOME}/bin directory
-   excute the create-infinispan-hotrod-protobuf-ds.cli script by running the following:

	./jboss-cli.sh --connect --file=../docs/teiid/datasources/infinispan/create-infinispan-materialization-ds.cli


* deploy the VDB

*  deploy the following VDB for reading/writing to the configured remote cache

	- copy both files, jdg-remote-cache-mat-vdb.xml and jdg-remote-cache-mat-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

Should see the server log indicate that the VDB is deployed with the following similar messages:

11:22:49,346 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) JBAS015876: Starting deployment of "jdg-remote-cache-mat-vdb.xml" (runtime-name: "jdg-remote-cache-mat-vdb.xml")
11:22:49,365 INFO  [org.teiid.RUNTIME.VDBLifeCycleListener] (MSC service thread 1-3) TEIID40118 VDB StockMat.1 added to the repository
11:22:49,366 INFO  [org.teiid.RUNTIME] (MSC service thread 1-3) TEIID50029 VDB StockMat.1 model "StockMat" metadata is currently being loaded. Start Time: 5/5/17 11:22 AM
11:22:49,373 INFO  [org.teiid.CONNECTOR] (teiid-async-threads - 4) === Using RemoteCacheManager (loaded by serverlist) ===
11:22:49,381 INFO  [org.jboss.as.server] (DeploymentScanner-threads - 1) JBAS015859: Deployed "jdg-remote-cache-mat-vdb.xml" (runtime-name : "infinispan-dsl-cache-vdb.xml")


The materialization process should begin immediately and the refresh of the cache will be done on 5 second intervals.
	


# Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example: mvn exec:java -Dvdb="StockMat" -Dsql="select product_id, symbol, price, company_name from StocksView.Stocks" -Dusername="teiidUser" -Dpassword="pwd"


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
