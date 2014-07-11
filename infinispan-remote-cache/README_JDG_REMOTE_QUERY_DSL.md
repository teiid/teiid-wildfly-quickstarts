Infinispan Remote-Cache Quickstart to a JDG 6.2/6.3 Server using Google Protocol Buffers for Serialization
================================

This remote quickstart demonstrates how Teiid can query a remote 6.2/.3 JDG cache using the hotrod client.


Setup to include:
-  deploying and configuring the new module that will contain the POJO jars.  This jar contains the class(s) that are contained in the remote cache.
-  configure the resource-adapter to connect to the JDG remote server
-  deploy the remote-cache-vdb that you will connect to in Teiid, which will access to the JDG remote server

-------------------
Quick Start requirements
-------------------

-  If you have not done so, please review the System Requirements (../README.md)
-  JBoss application server to run Teiid
-  The Teiid Jboss distribution kit
-  JDG 6.2 or 6.3 server kit
-  JDG 6.2 or 6.3 quick start kit

####################
#   JDG server setup
####################

-  Download the JDG quickstarts, specifically will be using the remote-query in this example.  Will need
	to build the remote-query quickstart, so that the jboss-remote-query-quickstart.jar is found in the target directory.
-  A separate JDG 6.2 or 6.3 server needs to be installed, and configured based on the remote-query quicks start

- if running JDG and DV on same box, will need to increment the ports of one of the servers, use:

		-  -Djboss.socket.binding.port-offset=100

For the purpose of this quick start, its expecting the JDG server to have its ports incremented.  The
port adjustment has been made in the setup.cli script to match the above offset.

-  run the remote-query quickstart to load the cache


######################
#   Setup Teiid Server
######################

1) shutdown jbossas server

2) run:  mvn clean install

3) Setup Modules (pojo's and infinispan remote client) 
	-	under  src/module_addressbook_pojo,  copy 'com' directory to <jbossas-dir>/modules/
	-	from the remote-query quick start, under  target, copy  infinispan-remote-query-quickstart.jar to <jbossas-dir>/modules/com/client/quickstart/addressbook/pojos/main

4) setup the infinispan resource adapter 

	-	open the file  under src/scripts/setup_resource_adapter_dsl.txt
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>
            

5) Start the server

	*  run:  ./standalone.sh 

		or run the following if Teiid isn't configured in the default configuration
	*  ./standalone.sh -c standalone-teiid.xml 


	
6) deploy the VDB: infinispan-vdb.xml

	* copy infinispan-dsl-cache-vdb.xml and infinispan-dsl-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

    

7) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:People@mm://{host}:31000
-  query: select name, email, id from Person

NOTE:  do not do a SELECT * FROM Person
because you will get a serialization error, because the Person class is not serializable.


