Dynamicvdb-dataroles demonstrates how to use data roles to control access to data.  This
includes read-only and read-write access as well as the use of row-based filters and column masking.

In this example, the vdb is defined with three different data access rules. 

1) ReadOnly - this restricts access of vdb to only read i.e selects. This role is given to everybody who has a login 
credetial (use the user called "user" to login with password "user").  Furthermore there are restrictions as to what
customers and columns can be read.

2) ReadWrite access - this role gives read access, and also adds write access. This access is given only
to users with the "superuser" JAAS role. (use user called "portfolio" to login with password "portfolio")

3) Prices access - this role is used to give access to price listings.  Its purpose is to demonstrate the use of a
generic role (empty) for controlling access to information.

See the portfolio-vdb.xml for extra xml elements defined for define the above roles. For more information check out
Reference Guide's Data Roles chapter.

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)

############################
#   NOTE:  This example relies upon the dynamicvdb-datafederation example and that it needs to be deployed prior to running this example.
#       	Therefore, read the dynamicvdb-datafederation's README.md and follow its directions before continuing.
############################



####################
#   Setup
####################

Setup can be done either manually (see Manual Setup) or using maven (see Setup using the JBoss AS Maven plugin) 


#########################################
### Manual setup
#########################################

1) Run the setup in dynamicvdb-datafederation quick start

2) shutdown the jbossas server

3) Security setup

    Copy the files in the src/security directory to "<jboss.home>/standalone/configuration" directory
	
4)  Start the server

	Open a command line and navigate to the "bin" directory under the root directory of the JBoss server

	For Linux:   ./standalone.sh -c standalone-teiid.xml	
	for Windows: standalone.bat -c standalone-teiid.xml

5) VDB Deployment:

    Copy the following files to the "<jboss.home>/standalone/deployments" directory

     * src/vdb/portfolio-vdb.xml
     * src/vdb/portfolio-vdb.xml.dodeploy


6)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

7)  See "Query Demonstrations" below to demonstrate data federation.


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

4.  "Select * from StockPrices" -  this demonstrates the use of hasRole({role}) call, and should display the prices
    
Since this simpleclient example defaults user and password to user/user, modify the call to include the user name and password on the command line 
where user name "portfolio" and password "portfolio" and re-execute the above:

Example:   mvn install -Dvdb="portfolio" -Dusername="portfolio" -Dpassword="portfolio" -Dsql="example query"

1.	should be the same result as above

2.	should succeed, since allow create is true

3.	should show all values, since the row condition is true and SSN column mask simply returns the SSN.

4.  should display the symbols, but not the prices,
