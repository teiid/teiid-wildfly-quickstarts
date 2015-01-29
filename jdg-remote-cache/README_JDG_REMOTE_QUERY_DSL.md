JDG Remote-Cache Quickstart using JDG Hot Rod that supports Google Protocol Buffers for Serialization
================================

Level: Intermediate
Technologies: Teiid, Infinispan, Hot Rod, Remote Query
Target Product: DV
Product Versions: DV 6.1
Source: <https://github.com/teiid/teiid-quickstarts>


What is it?
-----------
This quickstart demonstrates how Teiid can connect to a remote JBoss Data Grid (JDG) as a data source, to query and update data from cache using the Hot Rod protocol. 
It uses the JDG quick-start remote-query as how the cache is defined and its contents.


Setup to include:
-  deploying and configuring the new module that will contain the POJO jars.  This jar contains the class(s) that are contained in the remote cache.
-  configure the resource-adapter to connect to the JDG remote server
-  deploy the remote-cache-vdb that you will connect to in Teiid, which will access to the JDG remote server

-------------------
Quick Start requirements
-------------------

-  If you have not done so, please review the System Requirements (../README.md) 
Need the following kits:
-  JBoss application server to run Teiid
-  The Teiid Jboss distribution kit
-  JDG 6.4 server kit
-  JDG 6.4 quick start kit (will use the remote-query which has the protobuf definition file(s) and will be used to load the cache)
-  JDG 6.4 eap modules hotrod client kit (used by Teiid to access the remote cache)

NOTE: can obtain JDG kit distributions on Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html

####################
#   JDG setup
####################

1.  Setup JDG Server
	
-  Install the JDG server and configure based on the remote-query quicks start

-  if running JDG and DV on same box, will need to increment the ports of one of the servers using:

		-  -Djboss.socket.binding.port-offset=100

For the purpose of this quick start, its expecting the JDG server to have its ports incremented.  The
port adjustment has been made in the setup.cli script to match the above offset.

2.  Setup the JDG quickstarts, specifically will be using the remote-query in this example. 

*** BEFORE BUILDING, MAKE THE FOLLOWIN CHANGES ***

*  Edit addressbook.proto protobuf definition file

Edit file:  src/main/resources/quickstart/addressbook.proto

For the id attribute for Person, change index=false to index=true

*  Edit jdg.properties file, this assumes you are using the above port-offset, if not skip this.

edit file:  src/main/resources/jdg.properties

you will need to adjust the connection port that the quick start client uses.  The following property will need to incremented:

jdg.hotrod.port=11222      	----> 11322


*  build the remote-query quickstart

-  After building the quickstart, the jboss-remote-query-quickstart.jar should be found in the target directory.
-  This jar will be used later, where it is deployed to the Teiid server.


*  load the cache

*** NOTE ***:  When running the quickstart to load the cache, DO NOT ADD A MEMO FOR A PERSON

This is because there is no MemoMarshaller defined in the quickstart for translator to reference, therefore when you query
the cache you will get a marshalling exception for a cached object that has a Memo defined. 

- run the remote-query quickstart to load the cache


   
######################
#   Setup Teiid Server
######################

1) shutdown jbossas server

2) run:  mvn clean install

3) Setup pojo Module  
	-	under  src/module_addressbook_pojo,  copy 'com' directory to <jbossas-dir>/modules/
	-	from the remote-query quick start, under  target, copy  jdg-remote-cache-pojos.jar to <jbossas-dir>/modules/com/client/quickstart/addressbook/pojos/main

4) Install the JBoss Data Grid version of the hot rod client modules kit for EAP into 
	the modules location for your Teiid/EAP instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.


4) setup the infinispan resource adapter 

	-	open the file: {jbossas.server.dir}/docs/teiid/datasources/infinispan/infinispan-remote-query-dsl-ds.xml
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>
            

5) Start the server

	*  run:  ./standalone.sh 

		or run the following if Teiid isn't configured in the default configuration
	*  ./standalone.sh -c standalone-teiid.xml 

6) Install the infinispan-cache-dsl translator

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={jbossas.server.dir}/docs/teiid/datasources/infinispan/add-infinispan-cache-dsl-translator.cli 
	
7) deploy the VDB: infinispan-dsl-cache-vdb.xml

	* copy src/vdb/infinispan-dsl-cache-vdb.xml and infinispan-dsl-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

    

8) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:People@mm://{host}:31000
[1]  select name, email, id from Person
[2]  Insert into Person (id, name, email) Values (100, 'TestPerson', 'test@person.com')
then - select name, email, id from Person
[3]  Update Person set name='testPerson 100' where id = 100
then - select name, email, id from Person
[4]  delete from Person where id = 100
then - select name, email, id from Person

* When running either 2, 3, or 4, rerun above select to see the results to 
verify the changed data

NOTE:  do not do a SELECT * FROM Person
because you will get a serialization error, because the Person class is not serializable.


