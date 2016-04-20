---
Level: Intermediate
Technologies: Teiid, Infinispan, Hot Rod, Remote Query
Target Product: DV
Product Versions: DV 6.1+,  JDG 6.5+
Source: https://github.com/teiid/teiid-quickstarts
---

JDG Remote-Cache Quickstart using JDG Hot Rod that supports Google Protocol Buffers for Serialization
================================

# What is it?

This quickstart demonstrates how Teiid can connect to a remote JBoss Data Grid (JDG) as a data source, to query and update data from cache using the Hot Rod protocol. 

# Quick Start requirements

-  If you have not done so, please review the System Requirements [../README.md](../README.md) 


# PREREQUISTES


1.  Teiid Server Prerequistes

* JBoss application server to run Teiid
* The Teiid Jboss distribution kit

2.  JDG Server Prerequistes
	> NOTE: You can obtain JDG kit distributions on Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html

* JDG 6.5 eap modules kit (used by Teiid to access the remote cache)
* JDG 6.5 server kit (used as the remote server)
* JDG 6.5 quickstart kit (used to configure remote cache and initialize data)



# JDG setup

1.  Setup JDG Server
	
-  Install the JDG server
-  Configure caches based on the [./JDG_SERVER_README.md]


2.  Starting JDG Server

-  It is assumed that you will be running both servers on the same box, there start the JDG server by adding the following command line argument:
	*  -Djboss.socket.binding.port-offset=100

Example:   ./standalone.sh -Djboss.socket.binding.port-offset=100

For the purpose of this quick start, it assumes running both servers on the same machine and is expecting the JDG server to have its ports incremented.  The
port adjustment has been made in the setup.cli script to match the above offset.


# Teiid Quickstart Setup

1.  build the jdg-remote-cache quickstart

-  run  mvn -s ./settings.xml clean install

-  After building the quickstart, the jdg-remote-cache-pojos-jboss-as7-dist.zip should be found in the target directory.
-  This zip will be used later, where it is deployed to the Teiid server.


# Setup Teiid Server

1. shutdown jbossas server, if not already.

2. deploy pojo Module
	-	take the target/jdg-remote-cache-pojos-jboss-as7-dist.zip and unzip at <jbossas-dir>/modules/

3. Install the JBoss Data Grid version of the hot rod client modules kit for EAP into <jbossas-dir>/modules/ of your Teiid/EAP instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.


4. setup the infinispan resource adapter 

*  configure for reading and writing to a remote cache
	-	open the file: {jbossas.server.dir}/docs/teiid/datasources/infinispan/infinispan-remote-query-dsl-ds.xml
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>


5. Start the server

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	If Teiid isn't configured in the default configuration, append the following arguments to the command to specifselect name, email, id from Person where id = 100y the configuration
		
	-c {configuration.file} 
	
	Example: -c standalone-teiid.xml 


6. Install the infinispan-cache-dsl translator

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={jbossas.server.dir}/docs/teiid/datasources/infinispan/add-infinispan-cache-dsl-translator.cli 
	
	
7. deploy the VDB

*  deploy for reading/writing to a remote cache
	- copy files infinispan-dsl-cache-vdb.xml and infinispan-dsl-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


8.  JDG Remote Cache initialization

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../../REAselect name, email, id from Person where id = 100DME.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

* Make sure you have started the JDG as described above.
* Open a command line and navigate to the root directory of this teiid jdg-remote quickstart.
* If you need to, rebuild the quick start

        mvn clean install 
                
* Run the example application in its directory:

        mvn exec:java
 

Using the application
---------------------
Basic usage scenarios can look like this (keyboard shortcuts will be shown to you upon start):

    Available actions:
    0. Display available actions
    1. Add person
    2. Remove person
    3. Add phone to person
    4. Remove phone from person
    5. Display all persons
    6. Query persons by name
    7. Query persons by phone
    8. Quit

        
Type `8` to exit the application.



# Query Demonstrations

Use a sql tool, like SQuirreL, to connect and issue following example query:

> NOTE:  do not do a `SELECT * FROM Person`, because you will get a serialization error, because the Person class is not serializable.

1.  Queries for reading/writing to a remote cache via VDB People

-  connect:  jdbc:teiid:People@mm://{host}:31000
[1]  select name, email, id from Person
[2]  Insert into Person (id, name, email) Values (100, 'TestPerson', 'test@person.com')
then - select name, email, id from Person where id = 100
[3]  Update Person set name='testPerson 100' where id = 100
then - select name, email, id from Person
[4]  delete from Person where id = 100
then - select name, email, id from Person

* When running either 2, 3, or 4, rerun above select to see the results to 
verify the changed data


       

