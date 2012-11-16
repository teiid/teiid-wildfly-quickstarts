Dynamicvdb-webservices-to-twitter demonstrates using the WS Translator to call web services.
This example also shows how to define view tables in dynamic vdbs.

See https://dev.twitter.com/docs/api for information on the Twitter's SOAP/REST services. 

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)


####################
#   Setup
####################

Setup can be done either manually (see Manual Setup) or using maven (see Setup using the JBoss AS Maven plugin) 


#########################################
### Manual setup
#########################################

1) shutdown jbossas server

2)  Data Source setup:

- Edit the contents of "standalone-teiid.xml" file, and add contents of following files under the resource adapter subsystem element

	(1) src/datasource/twitter-ds.xml.xml
	
3)  Teiid VDB Deployment:

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/twitter-vdb.xml
     (2) src/vdb/twitter-vdb.xml.dodeploy

4)  Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml

5)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

6)  See "Query Demonstrations" below to demonstrate data federation.


#########################################
### Setup using the JBoss AS Maven plugin
#########################################

1) Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml	

2) setup the resource adapter

    *  mvn -Psetup-rar jboss-as:add-resource

3) copy the vdb files to the server

	*  mvn install -Pcopy-vdb 
    	
4) RESTART the jboss as server.  Without using CLI to configure the resources, the resource isn't activated.  
		Therefore, jboss-as requires a restart.	

5)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

6)  See "Query Demonstrations" below to demonstrate data federation.


#########################################
### Query Demonstrations
#########################################	

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="twitter" -Dsql="select * from tweet where query= 'jboss'"


NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be
complicated.  It would be better to install a Java client, such as SQuirreL, to run the 
queries below. 