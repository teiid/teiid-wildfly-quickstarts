---
Level: Beginners
Technologies: Teiid, WS Translator
Target Product: DV
Product Versions: DV 6.2
Source: https://github.com/teiid/teiid-quickstarts
---

# What is it?

The 'socialMedia-as-a-datasource' demonstrates using the WS Translator to call secured rest services, including Social Media(Twitter, Facebook, Weibo, etc), and transform the web service results into relational results.


## System requirements

If you have not done so, please review the System Requirements (../README.md) in the root quick starts directory.


## Twitter as a Data Source

In this section, we will demonstrate how to use Teiid WS Translator query all tweets you have posted over the years. Using the following 5 steps to execute a query:

1) Create a Twitter Apps

Login to [https://apps.twitter.com/](https://apps.twitter.com/) with your Twitter Account, create a Twitter Apps.

2) Create a Security Domain

Twitter use OAuth authentication, run `teiid-oauth-util.sh` utility:

~~~
$ ./bin/teiid-oauth-util.sh
~~~

will generate a security-domain xml configuration. Add the security domain to your standalone-teiid.xml file in security-domains subsystem. 

3)  Start the server

To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:

~~~
$ ./bin/standalone.sh -c=standalone-teiid.xml
~~~
	
4) Install web service datasource 

Navigate to JBoss home, execute CLI command:

~~~
$ ./jboss-cli.sh --connect file={path}/socialMedia-as-a-datasource/src/scripts/setup-twitter.cli
~~~

5)  Teiid WebService VDB Deployment:

Copy the following files to the "<jboss.home>/standalone/deployments" directory

~~~
src/vdb/twitter-vdb.xml
src/vdb/twitter-vdb.xml.dodeploy
~~~

> NOTE: [https://developer.jboss.org/wiki/ExposeTwitterDataInTeiidUsingOAuthAuthorization](https://developer.jboss.org/wiki/ExposeTwitterDataInTeiidUsingOAuthAuthorization) has detailed steps and instructs.

See **Query Demonstrations** below to demonstrate data query.

## Query Demonstrations

Using the simpleclient example

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn exec:java -Dvdb="webservice" -Dsql="select * from CustomersView" -Dusername="xx" -Dpassword="xx"

> NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be
complicated.  It would be better to install a Java client, such as SQuirreL, to run the 
queries below. 

### Twitter Data Source

~~~s
mvn exec:java -Dvdb="twitter" -Dsql="select * from TwitterUserTimelineView" -Dusername="xx" -Dpassword="xx"
~~~
