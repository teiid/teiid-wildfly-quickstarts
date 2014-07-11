Infinispan Remote-Cache Quickstart to a JDG 6.2/6.3 Server
================================

This remote quickstart demonstrates how Teiid can access a remote 6.2/6.2 JDG cache by key using the hotrod client.

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


####################
#   JDG server setup
####################

1)  if running JDG and DV on same box, will need to increment the ports of one of the servers, use:

		-  -Djboss.socket.binding.port-offset=100

For the purpose of this quick start (Load Remote Cache), its expecting the JDG server to have its ports incremented.  The
port adjustment has been made in the setup.cli script to match the above offset.

2)  configure infinispan cache

either cut-n-paste the following into the configuration:  <local-cache name="local-quickstart-cache" start="EAGER"/>

or run the /src/scripts/setup_remote_cache.cli script on the JDG server where the remote cache will reside


3)  Start JDG server

######################
#   Load Remote Cache
######################

To load the remote cache with example data, run the example application in this quick start to load orders:

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

- setup the module that contains the jar for the classes stored in the remote infinispan cache.
	-	under  src/module_order_pojo,  copy 'com' directory to <jbossas-dir>/modules/
	-	under  target, copy  infinispan-remote-cache-pojos.jar to <jbossas-dir>/modules/com/client/quickstart/order/pojos/main


4) setup the infinispan 6.2 resource adapter 

	-	open the file  under src/scripts/setup_resource_adapter.txt
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>
            

5) Start the server

	*  run:  ./standalone.sh 

		or run the following if Teiid isn't configured in the default configuration
	*  ./standalone.sh -c standalone-teiid.xml 


	
6) deploy the VDB: infinispan-vdb.xml

	* copy remotecache-vdb.xml and remotecache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

    

7) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:remote_orders@mm://{host}:31000
-  query: select * from OrdersView


