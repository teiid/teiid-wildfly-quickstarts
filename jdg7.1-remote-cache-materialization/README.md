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


# Quick Start requirements

-  If you have not done so, please review the System Requirements [../README.md](../README.md) 
-  For Red Hat product related kits, will need to access Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html

# PREREQUISTES

1.  Must be running JDK 1.8 or newer.

2.  JDV Server Prerequistes

* install JDV application server (download from the Red Hat Customer Portal)
* install the Teiid Jboss distribution kit into the JBoss server (This will have already been done if JDV kit is being used).

3.  JDG Server Prerequistes

* install JDG 7.1 server kit (download from the Red Hat Customer Portal), used as the JDG data source
* JDG 7.1.x JDG Hot Rod Client EAP modules kit (download from the Red Hat Customer Portal)

>NOTE:  hot rod client will require the 7.1.1 patch level or newer.

4.  Quick start dynamicvdb-datafederation [../../dynamicvdb-datafederation/README.md] must be installed into the JDV server.


# JDG setup

1.  Setup JDG Server
	
*  Install the JDG server

*  Start JDG Server

Assumptions:
- It is assumed that you will be running both servers on the same box, therefore, start the JDG server by using the port offset argument:  -Djboss.socket.binding.port-offset=
- That majority of usecases will be using the cache in a distributed fashion, so the clustered configuration will be used


[source,xml]
.*Example*
----
./standalone.sh -c clustered.xml -Djboss.socket.binding.port-offset=100
----

The following shows the command line to start the JDG server with the port offset:

        For Linux:   $JDG_HOME/bin/standalone.sh -c clustered.xml -Djboss.socket.binding.port-offset=100
        For Windows: %JDG_HOME%\bin\standalone.bat -c clustered.xml -Djboss.socket.binding.port-offset=100


*  Configure JDG Cache

You have 2 options for configuring the cache: a) running CLI script to configure cache or b) editing standalone configuration


a) Perform the following steps to configure the cache by running the CLI script against the JDG server

-  locate the {JBOSS_HOME}/quickstarts/jdg7.1-remote-cache-materialization/src/scripts/setup-jdg-caches.cli  script in the quickstart
-  cd to the ${ISPN_HOME}/bin directory
-  execute the setup-jdg-cache.cli script by running the following:

	./cli.sh  --file={path.to.cli.script}/setup-jdg-caches.cli

Since the server is running with the port offset, use the --controller option:

	 ./cli.sh --controller=localhost:10090  --file={path.to.cli.script}/setup-jdg-caches.cli
	 

Note the name of the caches - stockCache and st_stockCache,  as they need to match the teiid-ispn extension property Option for each table in the source model of the vdb

This configuration is not persisting the data, so when the JDG server is restarted the data will be lost.  See the JDG documentation on configuring a persistent cache.


b) Edit the standalone.xml configuration at ${JDG_HOME}/standalone/configuration.   Copy into the "clustered" section into the configuration  within the infinispan:server subsystem.


[source,xml]
----
        <subsystem xmlns="urn:infinispan:server:core:8.3">
            <cache-container name="clustered" default-cache="default" statistics="true">
                .... 
                <replicated-cache name="teiid-alias-naming-cache" configuration="replicated"/>

                <distributed-cache-configuration name="templateMatCache" start="EAGER"/>
                <distributed-cache name="stockCache" configuration="templateMatCache"/>
                <distributed-cache name="st_stockCache" configuration="templateMatCache"/>
                 ....
            </cache-container>
        </subsystem>
----

-  (re)start the JDG server if configuration was edited


#  [PreRequistes] Dynamicvdb-datafederation quickstart

*  Install dynamicvdb-datafederation quickstart [../../dynamicvdb-datafederation/README.md]

This quickstart is utilized for 2 purposes:
-  to provide the data that will be used to materialize
-  extends the VDB to create the view that will be materialized.
 

# Start Teiid Server

*  Install JDG Hot Rod Client EAP module kit to the JDV server installation.

- shutdown Jbossas server, if not already.
- unzip the kit at the $JBOSS_HOME directory.  You should find a new "org" directory under modules (modules/org).


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

* Install the infinispan-hotrod translator and related default resource-adapter

NOTE:  If already done, then skip to Setup JDG Data Source.

The infinispan translator is not installed by default because the dependent JDG related modules may not have been installed. To install do the following steps:

[source]
.*Setup translator*
---- 
cd to the ${JBOSS_HOME}/bin directory
execute the following:

	./jboss-cli.sh --connect --file=../docs/teiid/datasources/infinispan-hotrod-7.1/add-infinispan-hotrod-translator.cli 
----


* Configure resource adapter to access JDG Cache

The quickstart has a CLI script that can be used to configure the resource adapter. 

[source]
.*Setup JDG Data Source*
----
cd $\{JDG_HOME}/bin
execute the following:

	./jboss-cli.sh --connect --file=../quickstarts/jdg7.1-remote-cache-materialization/src/scripts/create-infinispan-hotrod-protobuf-ds.cli

----

*  Teiid VDB Deployment:

[source]
.*VDB deployment*
----
cd to the $JBOSS_HOME/bin directory
execute the following CLI script:

	./jboss-cli.sh --connect --file=../quickstarts/jdg7.1-remote-cache-materialization/src/scripts/deploy_mat-cache-vdb.cli
----

The materialization process should begin immediately and the refresh of the cache will be done based on the TTL time intervals set in the vdb.
	


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
