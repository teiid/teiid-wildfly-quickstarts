Dynamicvdb-dataroles demonstrates how to use data roles to control access to data.  This
includes read-only and read-write access.

In this example, the vdb is defined with two different data access rules. 

1) read-only - this restricts access of vdb to only read i.e selects. This role is given to everybody who has a login 
credetials (use the user called "user" to login with password "user")

2) read-write access - this role gives read access, and also adds write access. i.e. inserts. This access is given only
to users with "superuser" JAAS role. (use user called "portfolio" to login with password "portfolio")

See the portfolio-vdb.xml for extra xml elements defined for define the above roles. For more information check out
Reference Guide's Data Roles chapter.

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

2)  Data Source(s) setup:

- Edit the contents of "standalone-teiid.xml" file, and add contents of following files to create H2, CSV data sources

	(1) src/datasources/portfolio-ds.xml.xml - under "datasources" subsystem element
	(2) src/datasources/marketdata-file-ds.xml - under "resource-adapter" subsystem
	
- Copy the "teiidfiles" directory to the "<jboss.home>/" directory

	The teiidfiles directory should contain:
	(1) customer-schema.sql
	(2) customer-schema-drop.sql
	(3) data/marketdata-price.txt
	(4) data/marketdata-price1.txt
	
when completed, you should see <jboss.home>/teiidfiles

3)  Security setup

-  Copy the files in the src/security directory to "<jboss.home>/modules/org/jboss/teiid/conf" directory
	
4)  Teiid Deployment:

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/portfolio-vdb.xml
     (2) src/vdb/portfolio-vdb.xml.dodeploy


5)  Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml

6)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

7)  See "Query Demonstrations" below to demonstrate data federation.


#########################################
### Setup using the JBoss AS Maven plugin
#########################################

1) Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml	

2) copy the vdb, teiidfiles and security files

	*  mvn install -Pcopy-files
	
3) setup the datasource

    * `mvn -Psetup-datasource jboss-as:add-resource` 
	
4) setup the resource adapter

    * `mvn -Psetup-rar jboss-as:add-resource`
    
    	
5) RESTART the jboss as server.  Without using CLI to configure the resources, the resource isn't activated.  
		Therefore, jboss-as requires a restart.	

6)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

7)  See "Query Demonstrations" below to demonstrate data federation.


#########################################
### Query Demonstrations
#########################################	

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="portfolio" -Dsql="example query"



example queries:

1)	"select * from product" - this should execute correctly

2)	"insert into product (id, symbol,company_name) values (2000,'RHT','Red Hat')" - this will fail with data access error saying 
    that the user named "user" is not allowed write access.
    
Since this simpleclient example hard coded the default user and password, modify the included JDBCClient.java class 
to take the user name and password from command line and re-execute the query (2) with user name "portfolio" 
and password "portfolio" and see it executes to success! 


