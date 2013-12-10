Infinispan Remote-Cache Quickstart to a JDG 6.2 Server
================================

This remote quickstart demonstrates how Teiid can access a remote 6.2 JDG cache using the 6.2 hotrod client.


Assumptions:
-  Teiid has been deployed to your jboss as server.
-  Teiid user has been setup - edit standalone/configuration/teiid-security-users.properties and add your user and password

	the default is username=user   password=user

-  A separate JDG 6.2 server is up and running.


Setup to include:
-  deploying and configuring the new module that will contain the JDG 6.2 jars.
-  deploying and configuring the new module that will contain the POJO jars.  This jar contains the class(s) that are contained in the remote cache.
-  configure the resource-adapter to connect to the JDG remote server
-  deploy the remote-cache-vdb that you will connect to in Teiid, which will access to the JDG remote server

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)

####################
#   JDG server setup
####################

- if running JDG and DV on same box, will need to increment the ports of one of the servers, use:

		-  -Djboss.socket.binding.port-offset=100

For the purpose of this quick start, its expecting the JDG server to have its ports incremented.  The
port adjustment has been made in the setup.cli script to match the above offset.


######################
#   Setup Remote Cache
######################

1)  Configure remote infinispan cache

either cut-n-paste the following into the configuration:  <local-cache name="local-quickstart-cache" start="EAGER"/>

or run the /src/scripts/setup_remote_cache.cli script on the JDG server where the remote cache will reside

2) To load the remote cache with example data, run the example application to load orders:

	mvn exec:java

Using the application
---------------------

        lo  -  load orders
        p   -  print the orders
        q   -  quit
        
Type 'lo'  to load the orders into cache
Type 'p'   to print the contents of the cache
Type `q`  to exit the application.    


######################
#   Setup Teiid Server
######################

1) shutdown jbossas server

2) run:  mvn clean install

3) Setup Modules (pojo's and infinispan remote client) 


a) setup the module that contains the jar for the classes stored in the remote infinispan cache.
	-	under  src/module_pojos,  copy 'com' directory to <jbossas-dir>/modules/
	-	under  target, copy  infinispan-remote-cache-pojos.jar to <jbossas-dir>/modules/com/client/quickstart/pojos/main

b) setup the infinispan hotrod 6.2 remote client

	-	under  src/module_hotrod.6.2/,  copy 'org' directory to <jbossas-dir>/modules/
	-	copy the infinispan client jar's that are indicated in the module.xml to <jbossas-dir>/modules/org/jboss/infinispan/hotord/main
		NOTE:  should be able to find the jars in the JDG 6.2 kit,  <jbossas-dir>/client/hotrod/java directory	 


4) setup the infinispan 6.2 resource adapter 

	-	open the file  under src/scripts/setup_resource_adapter_6.2.txt
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>
            

5) Start the server

	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	
6) deploy the VDB: infinispan-vdb.xml

	* copy remotecache-vdb.xml and remotecache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

    

7) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:remote_orders@mm://localhost:31000
-  query: select * from OrdersView


