Simpleclient quickstart demonstrates how to make a connection to Teiid using both a Driver
and a DataSource.

The JDBCClient.java is the example code that shows a developer the basic connection logic that
can be used to connect to a Teiid instance running in a JBoss AS server.

The program expects four arguments <host> <port> <vdb> <sql-command>.  The pom.xml defines these arguments in
the pom.xml:

....
			<mainClass>JDBCClient</mainClass>
			<arguments>
				<argument>localhost</argument>  <!-- host -->
				<argument>31000</argument>   <!--  port -->
				<argument>${vdb}</argument>
				<argument>${sql}</argument>
			</arguments>
....

Notice that the <host> and <port> are preset. To point to a different server and/or port, 
change "localhost" and/or "31000" arguments, respectively, in the pom.xml.

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)

#############
#  Build
#############
First, build the project by running

	mvn clean install


#############
#  Execution
#############
To execute a sql query using the simpleclient, use the following format:

		   mvn install -Dvdb="<vdb>" -Dsql="<sql>"

Example:   mvn install -Dvdb="twitter" -Dsql="select * from tweet where query= 'jboss'"

Note that the query is in quotes so that it is understood as a single argument.

NOTE: To run more advanced queries, it would be better a fully featured Java client, 
such as SQuirreL [http://www.squirrelsql.org/].


#########################
#  Username and Password
#########################
Notice the "user" and "password" are not arguments.  These are preset in JDBCClient.java to:

	public static final String USERNAME_DEFAULT = "user";
	public static final String PASSWORD_DEFAULT = "user";

To override the defaults, you can pass overrides via the command line, as an example:

 mvn install -Dvdb="twitter" -Dsql="your query" -Dusername="teiidUser" -Dpassword="pwd"

Or you can change the default in JDBCClient.java.




