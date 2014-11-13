mongodb-as-a-datasource demonstrates using the mongodb Translator to access data in mongodb.

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md) in the root quick starts directory.


## Setup and Deployment

1)  Start the server (if not already started)

	Open a command line and navigate to the "bin" directory under the root directory of the JBoss server

	For Linux:   ./standalone.sh -c standalone-teiid.xml	
	for Windows: standalone.bat -c standalone-teiid.xml

2)  Setup mongoDB

For install, admin and getting start with mongoDB refer to [mongoDB manual](http://docs.mongodb.org/manual/). In this quickstart, we assume the employee document be insert under Employee connection as below:

~~~
db.Employee.insert({employee_id: '1', FirstName: 'Test1', LastName: 'Test1'});
db.Employee.insert({employee_id: '2', FirstName: 'Test2', LastName: 'Test2'});
db.Employee.insert({employee_id: '3', FirstName: 'Test3', LastName: 'Test3'});
~~~

Also the query document commands output as below:

~~~
> db.Employee.find();
{ "_id" : ObjectId("545c51d0ff38114a62627a73"), "employee_id" : "1", "FirstName" : "Test1", "LastName" : "Test1" }
{ "_id" : ObjectId("545c51e1ff38114a62627a74"), "employee_id" : "2", "FirstName" : "Test2", "LastName" : "Test2" }
{ "_id" : ObjectId("545c51efff38114a62627a75"), "employee_id" : "3", "FirstName" : "Test3", "LastName" : "Test3" }
~~~

3) Install the mongoDB datasource to be referenced by the Teiid VDB

-  run the following CLI script

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect file={path}/mongodb-as-a-datasource/src/scripts/setup.cli 

> NOTE - Before executing `setup.cli`, either modify `setup.cli` change ${mongodb.serverlist} to mongoDB server list and ${mongodb.dbname} to mongoDB DB name, or set System Properties mongodb.serverlist point to mongoDB server list and mongodb.dbname to mongoDB DB name. `10.66.218.46:27017` is a sample mongoDB server list and `mydb` is a sample DB name.

4)  Teiid mongoDB VDB Deployment:

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/mongodb-vdb.xml
     (2) src/vdb/mongodb-vdb.xml.dodeploy

5)  See "Query Demonstrations" below to demonstrate data query.


## Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="mongoVDB" -Dsql="select * from Employee"


> NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be complicated.  It would be better to install a Java client, such as SQuirreL, to run the queries. 
