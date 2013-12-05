Infinispan Remote-Cache Quickstart to a JDG 6.1 Server
================================

This remote quickstart demonstrates how Teiid can access a remote 6.1 JDG cache using the hotrod client provided in EAP 


Assumptions:
-  Teiid has been deployed to your jboss as server.
-  Teiid user has been setup - edit standalone/configuration/teiid-security-users.properties and add your user and password

	the default is username=user   password=user

-  A separate JDG 6.1 server is up and running.

Setup to include:
-  deploying and configuring the new module that will contain the POJO jars.  This jar contains the class(s) that are contained in the remote cache.
-  configure the resource-adapter to connect to the JDG remote server
-  deploy the infinsipan-cache-vdb that you will connect to in Teiid, which will access to the JDG remote server


-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)

####################
#   JDG server setup
####################

- if running the servers on the same box,will need to increment the ports of one of the servers, use:

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


####################
#   Setup
####################

1) shutdown jbossas server

2) run:  mvn clean install

3) setup the module that contains the jar for the classes stored in the remote infinispan cache.
	-	under  src/module_pojos,  copy 'com' directory to <jbossas-dir>/modules/
	-	under  target, copy  infinispan-remote-cache-pojos.jar to <jbossas-dir>/modules/com/client/quickstart/pojos/main


4) Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml

5) run the setup.cli  script to setup Infinispan cache and resource adapter for Infinispan connector

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/infinispan-local-cache/src/scripts/setup_resource_adapter.cli 
            
	
6) deploy the VDB: remotecache-vdb.xml

	* copy remotecache-vdb.xml and remotecache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


7) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:remote_orders@mm://localhost:31000
-  query: select * from OrdersView



