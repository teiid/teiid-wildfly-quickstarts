Dynamicvdb-dataroles demonstrates how to use data roles to control access to data.  This
includes read-only and read-write access as well as the use of row-based filters and column masking.

In this example, the vdb is defined with two different data access rules. 

1) read-only - this restricts access of vdb to only read i.e selects. This role is given to everybody who has a login 
credetial (use the user called "user" to login with password "user").  Furthermore there are restrictions as to what
customers and columns can be read.

2) read-write access - this role gives read access, and also adds write access. This access is given only
to users with the "superuser" JAAS role. (use user called "portfolio" to login with password "portfolio")

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

1) shutdown the jbossas server

2)  Data Source(s) setup:

    Edit the contents of "standalone-teiid.xml" file, and add contents of following files to create H2, CSV data sources

	* src/datasources/portfolio-ds.xml.xml - under "datasources" subsystem element
	* src/datasources/marketdata-file-ds.xml - under "resource-adapter" subsystem
	
    Copy the "teiidfiles" directory to the "<jboss.home>/" directory.  The teiidfiles directory should contain:
	* customer-schema.sql
	* customer-schema-drop.sql
	* data/marketdata-price.txt
	* data/marketdata-price1.txt
	
    when completed, you should see <jboss.home>/teiidfiles

3.  Security setup

    Copy the files in the src/security directory to "<jboss.home>/standalone/configuration" directory
	
4.  Teiid Deployment:

    Copy the following files to the "<jboss.home>/standalone/deployments" directory

     * src/vdb/portfolio-vdb.xml
     * src/vdb/portfolio-vdb.xml.dodeploy

5.  Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml

6.  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

7.  See "Query Demonstrations" below to demonstrate data federation.


#########################################
### Setup using the JBoss AS Maven plugin
#########################################

1. Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml	

2. copy the vdb, teiidfiles and security files

	*  mvn install -Pcopy-files
	
3. setup the datasource

    * mvn -Psetup-datasource jboss-as:add-resource 
	
4. setup the resource adapter

    * mvn -Psetup-rar jboss-as:add-resource
    
    	
5. RESTART the jboss as server.  Without using CLI to configure the resources, the resource isn't activated.  
		Therefore, jboss-as requires a restart.	

6.  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

7.  See "Query Demonstrations" below to demonstrate data federation.


#########################################
### Query Demonstrations
#########################################	

==== Using the simpleclient example ====

1. Change your working directory to "&lt;quickstart.install.dir&gt;/simpleclient"

2. Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="portfolio" -Dsql="example query"

example queries:

1.	"select * from product" - this should execute correctly

2.	"insert into product (id, symbol,company_name) values (2000,'RHT','Red Hat')" - this will fail with data access error saying 
    that the user named "user" is not allowed write access.
    
3.	"select * from customer" - note that the SSN is null and there are no customers from 'New York'
    
Since this simpleclient example hard coded the default user and password, modify the call to include the user name and password from command line with user name "portfolio" and password "portfolio" and re-execute the above:

Example:   mvn install -Dvdb="portfolio" -Dusername="portfolio" -Dpassword="portfolio" -Dsql="example query"

1.	should be the same result as above

2.	should succeed, since allow create is true

3.	should show all values, since the row condition is true and SSN column mask simply returns the SSN.
