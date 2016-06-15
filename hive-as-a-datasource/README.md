| **Datasource** | **Level** | **Technologies** | **Prerequisites** | **Description** |
|:---------|:----------|:-----------------|:------------------|:----------------|
|Hadoop, Hive |Beginner |Teiid, VDB, View, Hive Translator |Hadoop Services, HiveServer2 |Demonstrates using Hive translator to access data in Hadoop/Hive |

## What's this

This quickstart demonstrates using the Hive translator with HiveServer2 JDBC Driver to access data in Hadoop HDFS. VDB [hive-vdb.xml](src/vdb/hive-vdb.xml) be used to define View within DDL metadata.

The examples use `java:/HiveDS` referenced with Hive data source, which will be setup by running [setup.cli](src/scripts/setup.cli)

## Prerequisites

###1.Setup Hadoop/Hive

**1.** Hadoop Services

This section including step by step procedures for installing 'Hadoop 1.2.1' to RHEL 6, and configuring a Single Node Setup.

#### Step.1 Prerequisites

~~~
$ uname -a
Linux kylin.xx.com 2.6.32-431.20.3.el6.x86_64 #1 SMP Fri Jun 6 18:30:54 EDT 2014 x86_64 x86_64 x86_64 GNU/Linux

$ java -version
java version "1.7.0_60"
Java(TM) SE Runtime Environment (build 1.7.0_60-b19)
Java HotSpot(TM) 64-Bit Server VM (build 24.60-b09, mixed mode)
~~~

#### Step.2 Download and Install

~~~
$ wget http://apache.mesi.com.ar/hadoop/common/hadoop-1.2.1/hadoop-1.2.1.tar.gz
$ tar -xvf hadoop-1.2.1.tar.gz
$ cd hadoop-1.2.1
~~~

#### Step.3 Configure

Edit 'conf/hadoop-env.sh', comment out JAVA_HOME, make sure it point to a valid Java Home:

~~~
export JAVA_HOME=/usr/java/jdk1.7.0_60
~~~

> NOTE: Hadoop 1.2.1 need Java 1.6 or higher

Edit 'conf/core-site.xml', add the following properties in <configuration>:

~~~
<property>
     <name>hadoop.tmp.dir</name>
      <value>/home/kylin/tmp/hadoop</value>
</property>
<property>
     <name>dfs.name.dir</name>
     <value>/home/kylin/tmp/hadoop/name</value>
</property>
<property>
     <name>fs.default.name</name>
     <value>hdfs://localhost:9000</value>
</property>
<property>
    <name>dfs.permissions</name>
    <value>false</value>
</property>
~~~

> NOTE: the property's value should match to your's setting.

Edit 'conf/hdfs-site.xml', add the following 2 property in <configuration>:

~~~
<property>
    <name>mapred.job.tracker</name>
    <value>localhost:9001</value>
</property>
~~~

Format a new distributed-filesystem via execute

~~~
hadoop-1.2.1/bin/hadoop namenode -format
~~~

#### Step.4 Start

Start all hadoop services via execute

~~~
$ ./bin/start-all.sh
~~~

> NOTE: there are 5 java processes which represent 5 services be started: `NameNode`, `SecondaryNameNode`, `DataNode`, `JobTracker`, `TaskTracker`. Execute 'jps -l' to check the java processes:

~~~
$ jps -l
4056 org.apache.hadoop.hdfs.server.namenode.NameNode
4271 org.apache.hadoop.hdfs.server.datanode.DataNode
4483 org.apache.hadoop.hdfs.server.namenode.SecondaryNameNode
4568 org.apache.hadoop.mapred.JobTracker
4796 org.apache.hadoop.mapred.TaskTracker
~~~

> NOTE: `NameNode`, `JobTracker`, `TaskTracker` has relevant Web Consoles for View and Monitor the serivces. Web Access URLs for Services:

~~~
http://localhost:50030/   for the Jobtracker
http://localhost:50070/   for the Namenode
http://localhost:50060/   for the Tasktracker
~~~

#### Step.5 Stop

Start all hadoop services via execute

~~~
$ ./bin/start-all.sh
~~~

> NOTE: there are 5 java processes which represent 5 services be started: `NameNode`, `SecondaryNameNode`, `DataNode`, `JobTracker`, `TaskTracker`. Execute 'jps -l' to check the java processes:

~~~
$ jps -l
4056 org.apache.hadoop.hdfs.server.namenode.NameNode
4271 org.apache.hadoop.hdfs.server.datanode.DataNode
4483 org.apache.hadoop.hdfs.server.namenode.SecondaryNameNode
4568 org.apache.hadoop.mapred.JobTracker
4796 org.apache.hadoop.mapred.TaskTracker
~~~

> NOTE: `NameNode`, `JobTracker`, `TaskTracker` has relevant Web Consoles for View and Monitor the serivces. Web Access URLs for Services:

~~~
http://localhost:50030/   for the Jobtracker
http://localhost:50070/   for the Namenode
http://localhost:50060/   for the Tasktracker
~~~

#### Step.5 Stop

Stop all hadoop services via execute

~~~
# bin/stop-all.sh
~~~


**2.** Hive Services 

s section including step by step procedures for installing Apache Hive and set up HiveServer2.

#### Step.1 Prerequisites

Hadoop is the prerequisite, refer to above steps to install and start Hadoop.

#### Step.2 Install

~~~
$ tar -xvf apache-hive-1.2.1-bin.tar.gz
$ cd apache-hive-1.2.1-bin
~~~

#### Step.3 Configure

Create a 'hive-env.sh' under 'conf'

~~~
$ cd conf/
$ cp hive-env.sh.template hive-env.sh
$ vim hive-env.sh
~~~

comment out HADOOP_HOME and make sure point to a valid Hadoop home, for example:

~~~
HADOOP_HOME=/home/kylin/server/hadoop-1.2.1
~~~

Navigate to Hadoop Home, create '/tmp' and '/user/hive/warehouse' and chmod g+w in HDFS before running Hive:

~~~
$ ./bin/hadoop fs -mkdir /tmp
$ ./bin/hadoop fs -mkdir /user/hive/warehouse
$ ./bin/hadoop fs -chmod g+w /tmp
$ ./bin/hadoop fs -chmod g+w /user/hive/warehouse
$ ./bin/hadoop fs -chmod 777 /tmp/hive
~~~

> NOTE: Restart Hadoop services is needed, this for avoid 'java.io.IOException: Filesystem closed' in DFSClient check Open.

Create a 'hive-site.xml' file under conf folder

~~~
$ cd apache-hive-1.2.1-bin/conf/
$ touch hive-site.xml
~~~

Edit the 'hive-site.xml', add the following content:

~~~
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>hive.server2.thrift.min.worker.threads</name>
        <value>5</value>
    </property>
    <property>
        <name>hive.server2.thrift.max.worker.threads</name>
        <value>500</value>
    </property>
    <property>
        <name>hive.server2.thrift.port</name>
        <value>10000</value>
    </property>
    <property>
        <name>hive.server2.thrift.bind.host</name>
        <value>0.0.0.0</value>
    </property>
</configuration>
~~~

> NOTE: there are other Optional properties, more refer to [Setting+Up+HiveServer2](https://cwiki.apache.org/confluence/display/Hive/Setting+Up+HiveServer2)

#### Step.4 Start HiveServer2

~~~
$ ./bin/hiveserver2
~~~

### Spark Installation

The following steps show how to install Apache Spark and start the Thrift JDBC/ODBC server.

#### Step.1 Install

~~~
$ tar -xvf $ tar -xvf spark-1.4.0-bin-hadoop2.4.tgz
$ cd spark-1.4.0-bin-hadoop2.4
~~~

#### Step.2 Start

~~~
$ ./sbin/start-thriftserver.sh
~~~
 
**3.** Create Hive Table 
With above 2 setps use the following commands to load [hive-sample.txt](src/main/resources/hive-sample.txt):

~~~
$ ./bin/hive
hive> CREATE TABLE IF NOT EXISTS employee (eid int, name String, salary String, destination String);
hive> LOAD DATA LOCAL INPATH '{path}/vdb-Hivehadoop/src/data/hive-sample.txt' OVERWRITE INTO TABLE employee;
~~~
 
###2. Setup Teiid Server
  Assumed that you have already installed Teiid,and added users. If not, you can use
  
~~~
  $ ./bin/add-user.sh -a -u teiidUser -p password1! -g user  
  $ ./bin/add-user.sh admin password1!  
~~~


**1.** Hive driver

Install Hive driver module. Download TeiidModule-Hive12.zip and copy it into your server installation in the /jboss-eap-6.1/modules folder. unzip it. That's all. Do not modify the $JBOSS_HOME/standlone/configuration/standlone.xml. 

Refer to [ConnectToAHadoopSourceUsingHive2](https://developer.jboss.org/wiki/ConnectToAHadoopSourceUsingHive2)


**2.**  Start the server

To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

 
**3.** Setup the hive datasource adapter

-  run the following CLI script

	-	cd to the $JBOSS_HOME/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/vdb-Hivehadoop/src/scripts/setup.cli 
	
	Maybe you need modify the connection-url=jdbc:hive2://{host-name}:10000/default to your URL
	
	 Example : connection-url=jdbc:hive2://127.0.0.1:10000/default

**4.**  Teiid Deployment:

Copy (deploy) the following VDB related files to the $JBOSS_HOME/standalone/deployments directory

	* hive VDB
    	- src/vdb/hive-vdb.xml
    	- src/vdb/hive-vdb.xml.dodeploy
    
## Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   
~~~

mvn clean install

mvn exec:java -Dvdb="hadoop" -Dsql="SELECT * FROM EMPLOYEEVIEW" -Dusername="teiidUser" -Dpassword="password1!"
~~~ 

