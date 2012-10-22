Infinispan JBoss AS 7 Quickstart
================================

This quickstart demonstrates how create a simple cache on *JBoss AS 7.1* and access it from Teiid.

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

For more information, including how to set up Maven or JBoss Tools in Eclipse,
refer to the [Getting Started Guide](https://docs.jboss.org/author/display/ISPN/Getting+Started+Guide+-+JBoss+AS+7).

### Steps to setup and run the quickstart ###
The following are the steps to setup and run the quickstart.  These can be done either manually or using maven. 

Assumptions:
-  Teiid has been deployed to your jboss as server.


1).  shutdown jbossas server
2).  run:  mvn clean install
3).  deploy the pojo's as a module to the jboss as server, before the server is started
4).  setup infinispan cache
5).  setup infinispan as a datasource
6).  deploy quickstart war - used it initialize cache and view contents
7).  deploy quickstart vdb
8).  run query test
	 
#########################################
### Setup manually
#########################################

1) shutdown jbossas server

2) run:  mvn clean install

3) setup the module that contains the infinispan-quickstart-pojos.jar.
	-	under  src/module,  copy 'jars' directory to <jbossas-dir>/modules/org/jboss/teiid/translator/object
	-   under  target, copy  infinispan-quickstart-pojos.jar to <jbossas-dir>/modules/org/jboss/teiid/translator/object/jars/main

4) setup Infinispan cache

* Edit `standalone/configuration/standalone-teiid.xml` and add a cache

            <cache-container name="teiid-infinispan-quickstart" default-cache="local-quickstart-cache" start="EAGER">
                <local-cache name="local-quickstart-cache" start="EAGER"/>
            </cache-container>
            
5) setup Infinispan as a datasource

* Edit `standalone/configuration/standalone-teiid.xml` and add resource-adapter

             <resource-adapters>
                <resource-adapter>
                    <archive>
                        teiid-connector-infinispan.rar
                    </archive>
                    <transaction-support>NoTransaction</transaction-support>
                    <connection-definitions>
                        <connection-definition class-name="org.teiid.resource.adapter.infinispan.InfinispanManagedConnectionFactory" jndi-name="java:jboss/eis/teiid-infinispan" enabled="true" use-java-context="true" pool-name="infinispanCacheDS">
                            <config-property name="CacheTypeMap">
                                local-quickstart-cache:org.jboss.teiid.quickstart.infinispancache.pojo.Order
                            </config-property>
                            <config-property name="CacheJndiName">
                                java:jboss/infinispan/container/teiid-infinispan-quickstart
                            </config-property>
                            <config-property name="module">
                                org.jboss.teiid.translator.object.jars
                            </config-property>
                        </connection-definition>
                    </connection-definitions>
                </resource-adapter>
            </resource-adapters>

            
5) Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml

6) deploy the sample application using the management console at http://localhost:9990

	* use the management console at http://localhost:9990 to deploy infinispan-quickstart.war from the target directory
	
7) deploy the VDB: infinispan-vdb.xml

	* copy infinispancache-vdb.xml and infinispancache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	

8) Open a browser to:  http://localhost:8080/infinispan-quickstart/home.jsf
This should present a list of 10 Orders that were loaded into the cache.


#########################################
### Setup using the JBoss AS Maven plugin
#########################################

1) shutdown jbossas server

2) run:  mvn clean install

3) install the pojo.jar as a module

	*  mvn -Pinstall-module install -Djbossas-server-dir={jbossas.server.dir}

4) Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml

5) setup the Infinispan Cache

    * `mvn -Psetup-cache jboss-as:add-resource` 
    
6) setup Infinispan as a datasource
    
    * `mvn -Psetup-datasource jboss-as:add-resource`  
    
7) deploy the sample application infinispan-quickstart.war and the infinispan-vdb.xml artifacts

	* `mvn install -Pdeploy-artifacts -Djbossas-server-dir={jbossas.server.dir} 
	
8) Open a browser to:  http://localhost:8080/infinispan-quickstart/home.jsf
This should present a list of 10 Orders that were loaded into the cache.

