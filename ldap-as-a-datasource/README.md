ldap-as-a-datasource demonstrates using the ldap Translator to access data in OpenLDAP Server.

## System requirements

* System Requirements [../README.md](../README.md) in the root quick starts directory.
* OpenLDAP be installed on Red Hat Linux 6.x([Red Hat document](https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/6/html/Deployment_Guide/ch-Directory_Servers.html#s2-ldap-installation), [Community document](http://www.openldap.org/doc/admin24/guide.html))

## Setup and Deployment

1)  Start the server (if not already started)

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	append the following to the command to indicate which configuration to use if Teiid isn't configured in the default configuration
		
	-c standalone-teiid.xml 
	
2)  Setup LDAP Group and Users

For installing OpenLDAP on Red Hat Linux 6.x please refer to above **System requirements**, in this section we will create a Group `HR` and 3 users(`hr1`, `hr2`, `hr3`) under it.

[Steps for adding Group and Users]

3) Install the ldap datasource to be referenced by the Teiid VDB

-  run the following CLI script

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect file={path}/ldap-as-a-datasource/src/scripts/setup.cli 

> NOTE - Before executing `setup.cli`, either modify `setup.cli` change ${ldap.url} to LDAP url, ${ldap.rootdn} to LDAP root Distinguished Name and ${ldap.rootpw} to LDAP root password, or set System Properties `ldap.url` point to LDAP url, `ldap.rootdn` point to LDAP root Distinguished Name and `ldap.rootpw` to LDAP root password. For example, `ldap://10.66.218.46:389` is a sample LDAP URL, `cn=Manager,dc=example,dc=com` is a sample root Distinguished Name and `red` is a sample root password.

4)  Teiid ldapDB VDB Deployment:

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/ldap-vdb.xml
     (2) src/vdb/ldap-vdb.xml.dodeploy

5)  See "Query Demonstrations" below to demonstrate data query.


## Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="ldapVDB" -Dsql="select * from HR_Group"


> NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be complicated.  It would be better to install a Java client, such as SQuirreL, to run the queries. 
