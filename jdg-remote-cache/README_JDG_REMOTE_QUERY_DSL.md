JDG Remote-Cache Quickstart using Google Protocol Buffers for Serialization and Hot Rod client to query
================================

This remote quickstart demonstrates how Teiid can query a remote 6.3 JDG cache using the hotrod client utilizing google protbuffers.


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
-  JDG 6.3 server kit (will be the remote cache)
-  JDG 6.3 quick start kit (will use the remote-query which has the protobin file(s) and will be used to load the cache)
-  JDG 6.3 eap modules hotrod client kit (used by Teiid to access the remote cache)

####################
#   JDG server setup
####################

-  Download the JDG quickstarts, specifically will be using the remote-query in this example.  Will need
	to build the remote-query quickstart, so that the jboss-remote-query-quickstart.jar is found in the target directory.
	
-  Install the JDG 6.3 server and configured based on the remote-query quicks start

-  if running JDG and DV on same box, will need to increment the ports of one of the servers using:

		-  -Djboss.socket.binding.port-offset=100

For the purpose of this quick start, its expecting the JDG server to have its ports incremented.  The
port adjustment has been made in the setup.cli script to match the above offset.


-  run the remote-query quickstart to load the cache

NOTE: you will need to adjust the jdg remote-query quickstart  jdg.properties that defines the connection ports
that it uses.   The following properties will need to incremented:

jdg.hotrod.port=11222      	----> 11322
jdg.jmx.port=9999			----> 10099
 
   
######################
#   Setup Teiid Server
######################

1) shutdown jbossas server

2) run:  mvn clean install

3) Setup pojo Module  
	-	under  src/module_addressbook_pojo,  copy 'com' directory to <jbossas-dir>/modules/
	-	from the remote-query quick start, under  target, copy  infinispan-remote-query-quickstart.jar to <jbossas-dir>/modules/com/client/quickstart/addressbook/pojos/main

4) Install the JBoss Data Grid 6.3.x version of the hot rod client modules kit for EAP into 
	the modules location for your Teiid/EAP instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.


4) setup the infinispan resource adapter 

	-	open the file  under src/scripts/setup_resource_adapter_dsl.txt
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>
            

5) Start the server

	*  run:  ./standalone.sh 

		or run the following if Teiid isn't configured in the default configuration
	*  ./standalone.sh -c standalone-teiid.xml 

6) run the add-infinispan-cache-dsl-translator.cli script to install translator infinispan-cache.

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/infinispan-local-cache/src/scripts/add-infinispan-cache-dsl-translator.cli 
	
7) deploy the VDB: infinispan-dsl-cache-vdb.xml

	* copy infinispan-dsl-cache-vdb.xml and infinispan-dsl-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

    

8) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:People@mm://{host}:31000
[1]  select name, email, id from Person
[2]  Insert into Person (id, name, email) Values (100, 'TestPerson', 'test@person.com')
[3]  Update Person set name='testPerson 100' where id = 100
[4]  delete from Person where id = 2

* When running either 2, 3, or 4, rerun above select to see the results to 
verify the changed data

NOTE:  do not do a SELECT * FROM Person
because you will get a serialization error, because the Person class is not serializable.


