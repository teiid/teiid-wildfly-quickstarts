---
Level: Intermediate
Technologies: Teiid, Infinispan, Hot Rod, Remote Query
Target Product: DV
Product Versions: DV 6.4+,  JDG 7.1+
Source: https://github.com/teiid/teiid-quickstarts
---

JDG Remote-Cache Quickstart using JDG Hot Rod that supports Google Protocol Buffers for Serialization
================================

# What is it?

This quickstart demonstrates how Teiid can connect to a remote Infinispan cache and perform queries and/or update data in the cache using the Hot Rod client protocol.

# Quick Start requirements

-  If you have not done so, please review the System Requirements [../README.md](../README.md) 
-  For Red Hat product related kits, will need to access Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html

# 1. JDG setup

* Install the JDG server

* Configure JDG Cache

You have 2 options for configuring the cache: a) editing standalone configuration or b) running CLI script to configure cache


a) Edit the standalone.xml configuration at ${JDG_HOME}/standalone/configuration.   Copy into the "local" section into the configuration  within the infinispan:server subsystem.

[source,xml]
----
        <subsystem xmlns="urn:infinispan:server:core:8.3">
            <cache-container name="local" default-cache="default" statistics="true">
                .... 
                <local-cache name="personCache" start="EAGER">
                    <locking striping="false" acquire-timeout="30000" concurrency-level="1000"/>
                    <transaction mode="NONE"/>
                </local-cache>

                ....
            </cache-container>
        </subsystem>
----


b) Perform the following steps to configure the cache by running the CLI script against the JDG server

-  Start the JDG servr by performing the step after this.

-  locate the ./src/scripts/setup-jdg-cache.cli script in the quickstart
-  cd to the ${JDG_HOME}/bin directory
-  execute the setup-jdg-cache.cli script by running the following:  

	./cli.sh  --file={path.to.cli.script}/setup-jdg-caches.cli

Since the server is running with the offset, use the -c option:

	 ./cli.sh -c 127.0.0.1:10099 --file=./setup-jdg-caches.cli
	 

Note the name of the cache - _datasourceCache_  as it will used when defining the cache for the model in the VDB.

This configuration is not persisting the data, so when the JDG server is restarted the data will be lost.  See the JDG documentation on configuring a persistent cache.


* Start JDG Server

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


# 2. Setup Teiid Server

* Start the server

To start the server, open a command line and navigate to the "bin" directory under the root directory of the Teiid server and run:

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
./jboss-cli.sh --connect --file=${PATH}/teiid-quickstarts/jdg7.1-remote-cache/src/scripts/create-infinispan-hotrod-protobuf-ds.cli
----
 

=== 4. deploy the VDB

Deploy for reading/writing to a remote cache, by copying files jdg-person-cache-ddl-vdb.xml and jdg-person-cache-ddl-vdb.xml.dodeploy to {JBOSS_HOME}/standalone/deployments


== Query Demonstrations

1. Change your working directory to "${quickstart.install.dir}/simpleclient"
2. Use the simpleclient example to run the following queries:

Example: mvn exec:java -Dvdb="People" -Dsql="Insert into Person (id, name, salary) Values (100, 'TestPerson', 48000)" -Dusername="teiidUser" -Dpassword="pwd"

or

Use a sql tool, like SQuirreL, to connect and issue following example query:

NOTE: do not do a `SELECT * FROM Person`, because you will get a serialization error, because the Person class is not serializable.

1.  Queries for reading/writing to a remote cache via VDB People

* connect: jdbc:teiid:PersonVDB@mm://{host}:31000 

[source,sql]
.*Example Query SQL*
----
select name, salary, id from Person 
Insert into Person (id, name, salary) Values (100, 'TestPerson', 35000);
Insert into Person (id, name, salary) Values (200, 'TestPerson2', 100000);

select name, salary, id from Person where id = 100
Update Person set salary=55000 where id = 100

select * from Person

delete from Person where id = 100
select * from Person
----
