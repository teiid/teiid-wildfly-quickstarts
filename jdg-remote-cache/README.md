---
Level: Intermediate
Technologies: Teiid, Infinispan, Hot Rod, Remote Query
Target Product: DV
Product Versions: DV 6.1+,  JDG 6.5+
Source: https://github.com/teiid/teiid-quickstarts
---

JDG Remote-Cache Quickstart using JDG Hot Rod that supports Google Protocol Buffers for Serialization
================================

There are 2 options for configuring the JDG schema in DV, using protobuf (.proto) file and marshaller(s) or using the JDG 6.6 
feature of using protobuf annotations defined in the pojo.   This example is demonstrating using protobuf and marhsallers to define
the JDG schema.


# What is it?

This quickstart demonstrates how Teiid can connect to a remote JBoss Data Grid (JDG) as a data source, to query and update data in cache using the Hot Rod client protocol.

# Quick Start requirements

-  If you have not done so, please review the System Requirements [../README.md](../README.md) 
-  For Red Hat product related kits, will need to access Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html


# PREREQUISTES


1.  Teiid Server Prerequistes

* install JBoss application server to run Teiid (or install the JBoss Data Virtualization product distribution, download from the Red Hat Customer Portal)
* install the Teiid Jboss distribution kit into the JBoss server (This will have already been done if JDV kit is being used).

2.  JDG Server Prerequistes

> NOTE: You can obtain JDG kit distributions on Red Hat's Customer Portal

* JDG 6.x JDG Hot Rod Client EAP modules kit (used by Teiid to access the remote cache)
* JDG 6.x server kit (used as the remote server)

>NOTE:  To use 6.6, you will need the 6.6.1 patch.

JDG 6.4 thru 6.6 is supported.

# JDG setup

1.  Setup JDG Server
	
*  Install the JDG server

*  Configure JDG Cache

You have 2 options for configuring the cache: a) editing standalone configuration or b) running CLI script to configure cache


a) Edit the standalone.xml configuration at ${JDG_HOME}/standalone/configuration.   Copy in the "local-cache" section into the configuration  within the infinispan:server subsystem.

[source,xml]
----
        <subsystem xmlns="urn:infinispan:server:core:8.3">
            <cache-container name="local" default-cache="default" statistics="true">
                .... 
                <local-cache name="datasourceCache" start="EAGER">
                    <locking striping="false" acquire-timeout="30000" concurrency-level="1000"/>
                    <transaction mode="NONE"/>
                </local-cache>
                ....
            </cache-container>
        </subsystem>
----


b) Perform the following steps to configure the cache by running the CLI script against the JDG server


-   locate the ./src/jdg/setup-jdg-cache.cli script in the quickstart
-	cd to the ${JDG_HOME}/bin directory
-   execute the setup-jdg-cache.cli script by running the following:  

	./cli.sh  --file={path.to.cli.script}/setup-jdg-cache.cli

Since the server is running with the offset, use the -c option:

	 ./cli.sh -c 127.0.0.1:10099 --file=./setup-jdg-cache.cli
	 
Note the name of the cache: _datasourceCache_

This cache name will be needed when configuring the JDG connector in Teiid.

This configuration is not persisting the data, so when the JDG server is restarted the data will be lost.  See the JDG documentation on configuring a persistent cache.



2.  Starting JDG Server

>Note: For the purpose of this quick start, it assumes running both servers on the same machine and is expecting the JDG server to have its ports incremented.  The port adjustment has been made when configuring the JDG connector in Teiid.

*  Start the JDG server by adding the following command line argument:
	*  -Djboss.socket.binding.port-offset=100

Example:   ./standalone.sh -Djboss.socket.binding.port-offset=100

* The following shows the command line to start the JDG server with the port offset:

        For Linux:   $JDG_HOME/bin/standalone.sh -Djboss.socket.binding.port-offset=100
        For Windows: %JDG_HOME%\bin\standalone.bat -Djboss.socket.binding.port-offset=100




# Teiid Quickstart Setup

1.  build the jdg-remote-cache quickstart

-  run  mvn -s ./settings.xml clean install

-  After building the quickstart, the jdg-remote-cache-pojos-jboss-as7-dist.zip should be found in the target directory.
-  This zip will be used later, where it is deployed to the Teiid server.


# Setup Teiid Server

1.  shutdown Jbossas server, if not already.

*  Install the JBoss Data Grid Hot Rod Client EAP modules kit into <jbossas-dir>/modules/ of your Teiid/EAP instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.

*  Install pojo Module
	-	unzip the target/jdg-remote-cache-pojos-jboss-as7-dist.zip at the <jbossas-dir>/ root directory
	
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


6. configure the infinispan resource adapter 

The following steps will configure the infinispan resource-adapater to communicate for reading and writing to a remote cache.

>Note:  this cli script was copied from the quickstart to the server when the pojo module kit was deployed

-	cd to the ${JBOSS_HOME}/bin directory
-   excute the create-infinispan-hotrod-protobuf-ds.cli script by running the following:

	./jboss-cli.sh --connect --file=../docs/teiid/datasources/infinispan/create-infinispan-hotrod-protobuf-ds.cli

	
7. deploy the VDB

*  deploy the following VDB for reading/writing to the configured remote cache

	- copy both files, infinispan-dsl-cache-vdb.xml and infinispan-dsl-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

Should see the server log indicate that the VDB is deployed with the following similar messages:

11:22:49,346 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) JBAS015876: Starting deployment of "infinispan-dsl-cache-vdb.xml" (runtime-name: "infinispan-dsl-cache-vdb.xml")
11:22:49,365 INFO  [org.teiid.RUNTIME.VDBLifeCycleListener] (MSC service thread 1-3) TEIID40118 VDB People.1 added to the repository
11:22:49,366 INFO  [org.teiid.RUNTIME] (MSC service thread 1-3) TEIID50029 VDB People.1 model "People" metadata is currently being loaded. Start Time: 5/5/17 11:22 AM
11:22:49,373 INFO  [org.teiid.CONNECTOR] (teiid-async-threads - 4) === Using RemoteCacheManager (loaded by serverlist) ===
11:22:49,381 INFO  [org.jboss.as.server] (DeploymentScanner-threads - 1) JBAS015859: Deployed "infinispan-dsl-cache-vdb.xml" (runtime-name : "infinispan-dsl-cache-vdb.xml")

Now you are ready to query.


# Query Demonstrations

You can either use the quickstart simple client tool or SQL tool, like SQuirreL, to issue queries.

1.  Simple Client

- Change your working directory to "${quickstart.install.dir}/simpleclient"
. Use the simpleclient example to run the following queries:

Example: mvn exec:java -Dvdb="People" -Dsql="Insert into Person (id, name, email) Values (100, 'TestPerson', 'test@person.com')" -Dusername="teiidUser" -Dpassword="pwd"

or

2.  Use a sql tool, like SQuirreL, to connect and issue following example queries


These queries will demonstrate the reading/writing to a remote cache via the VDB People

* connect: jdbc:teiid:People@mm://{host}:31000 

[source,sql]
.*Example Query SQL*
----

select name, email, id from Person 


Insert into Person (id, name, email) Values (100, 'TestPerson', 'test@person.com');
Insert into Person (id, name, email) Values (200, 'TestPerson2', 'test2@person.com');

select name, email, id from Person where id = 100
Update Person set name='testPerson 100' where id = 100  # then - select name, email, id from Person 


Insert into Address (id, Address, City, State) Values (200, '123 Freedom', 'Williamsburg', 'VA')

select a.id, a.name, b.Address, b.City, b.State from Person as a, Address as b WHERE a.id = b.id


Insert into PhoneNumber (id, number) Values (200, '603-351-3022');

select a.id, a.name, b.number from Person as a, PhoneNumber as b WHERE a.id = b.id


delete from Person where id = 100
select name, email, id from Person
----

