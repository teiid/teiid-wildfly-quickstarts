/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package org.jboss.teiid.dbms.appender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.teiid.logging.CommandLogMessage;

/**
 * The RDBMSCustomAppender is an example logger that will write the {@link CommandLogMessage} to the database specified by the {@link #getDatasourceJNDIName() JNDIName}.
 * 
 * Features of this example are:
 * <li>Specify VDB and/or Model filters so that logging will not occur when those are accessed.   These options were added so that certain types of VDB's/Models (i.e, read-only)
 * can be excluded, in this case the CommandLog VDB to read the log file.
 * 
*/
 
 

@SuppressWarnings("nls")
public class RDBMSCustomAppender extends Handler {
    private static final String NOSOURCE_NEW = "Insert into TEIID_COMMANDLOG (EVENT_TIME, VDB, VERSION, EVENT_TYPE, APPLICATION_NAME, SESSION_ID, REQUEST_ID, TRANSACTION_ID, PRINCIPAL_NAME, SQL) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String NOSOURCE_OTHER = "Insert into TEIID_COMMANDLOG (EVENT_TIME, VDB, VERSION, EVENT_TYPE, APPLICATION_NAME, SESSION_ID, REQUEST_ID, PRINCIPAL_NAME, ROW_COUNT) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SOURCE_NEW = "Insert into TEIID_COMMANDLOG (EVENT_TIME, VDB, VERSION, EVENT_TYPE, SESSION_ID, REQUEST_ID, SOURCE_COMMANDID, TRANSACTION_ID, PRINCIPAL_NAME, MODELNAME, TRANSLATORNAME, SQL) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SOURCE_OTHER = "Insert into TEIID_COMMANDLOG (EVENT_TIME, VDB, VERSION, EVENT_TYPE, SESSION_ID, REQUEST_ID, SOURCE_COMMANDID, TRANSACTION_ID, PRINCIPAL_NAME, MODELNAME, TRANSLATORNAME, ROW_COUNT, PLAN) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
   
    private static DataSource datasource =null;
    
    protected String datasourceJNDIName = null;
    
    protected String modelExcludeFilter = null;
    protected String vdbExcludeFilter = null;
    
    protected Set<String> modelFilteredNames = new HashSet<String>();
    protected Set<String> vdbFilteredNames = new HashSet<String>();
       
    
    public void setDatasourceJNDIName(String name) {
        this.datasourceJNDIName = name;
    }
    
    public String getDatasourceJNDIName() {
        return this.datasourceJNDIName;
    }

    public void setVdbExcludeFilter(String filter) {
        this.vdbExcludeFilter = filter;

        vdbFilteredNames.addAll(split(vdbExcludeFilter, ","));
    }
    
    public String getVdbExcludeFilter() {
        return this.vdbExcludeFilter;
    } 
    
    public void setModelExcludeFilter(String filter) {
        this.modelExcludeFilter = filter;

        modelFilteredNames.addAll(split(modelExcludeFilter, ","));
    }
    
    public String getModelExcludeFilter() {
        return this.modelExcludeFilter;
    }     
    
    @Override
    public void publish(LogRecord record) {
        if (!ensureReady())  
        {  
            return;  
        }  
        try  
        {  
            log(record); 
        }  
        catch (Throwable e)  
        {  
            e.printStackTrace();  
        }  
    }
    
    
    @Override
    public void flush() {
    }
    
    @Override
    public void close() throws SecurityException {
    	datasource = null; 
    }
    
    private synchronized boolean ensureReady()  
    {  
        if (datasource != null)  
        {  
            return true;  
        }  
        
        try  
        {   
             datasource= getDataSource(this.datasourceJNDIName); 
        }  
        catch (NamingException ne) 
        {  
            ne.printStackTrace();  
            return false; 
        }  
        return true;
    }  
    
    protected void log(LogRecord record) throws SQLException {
    	Object obj = record.getParameters()[0];
    	CommandLogMessage clMessage = null;
    	try {
    		clMessage = (CommandLogMessage)obj;
    	} catch (Throwable e) {
    		e.printStackTrace();
    	}
    	
        Connection connection = null;
        try {
            if (!clMessage.isSource()) {
                if (!vdbFilteredNames.isEmpty() && vdbFilteredNames.contains(clMessage.getVdbName())) {               	
                    return;
                }
                connection = datasource.getConnection();
                if (clMessage.getStatus() == CommandLogMessage.Event.NEW) {
                    logNoSource_NewEvent(clMessage, connection, "START USER COMMAND");
                } else {
                    logNoSource(clMessage, connection, clMessage.getStatus().toString() + " USER COMMAND");
                }
            } else {
                if (!modelFilteredNames.isEmpty() && modelFilteredNames.contains(clMessage.getModelName())) {
                     return;
                }
                connection = datasource.getConnection();
                if (clMessage.getStatus() == CommandLogMessage.Event.NEW) {
                    logSourceNewEvent(clMessage, connection, "START DATA SRC COMMAND");
                } else {
                    logSourceEvent(clMessage, connection, clMessage.getStatus().toString() + " DATA SRC COMMAND");
                }
            }

        } catch (SQLException e) {
            if (!connection.getAutoCommit()) {
            	connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    // do nothing
                }
            }               
        }          
    }
    
    
    private void logNoSource_NewEvent(CommandLogMessage msg, Connection connection, String eventType) throws SQLException {
         PreparedStatement stmt = connection.prepareStatement(NOSOURCE_NEW);
        
        stmt.setTimestamp(1,  new java.sql.Timestamp(msg.getTimestamp()) );
        stmt.setString(2, msg.getVdbName());
        stmt.setObject(3, msg.getVdbVersion());
        stmt.setString(4, eventType);
        stmt.setString(5, msg.getApplicationName());
        stmt.setString(6, msg.getSessionID());
        stmt.setString(7, msg.getRequestID());
        stmt.setString(8, msg.getTransactionID());
        stmt.setString(9, msg.getPrincipal());
        stmt.setString(10, msg.getSql());
        
        stmt.execute();
        
        if (!connection.getAutoCommit()) {
        	connection.commit();
        }
        
        stmt.close();
    }
    
    private void logNoSource(CommandLogMessage msg, Connection connection, String eventType) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(NOSOURCE_OTHER);

        stmt.setTimestamp(1,  new java.sql.Timestamp(msg.getTimestamp()) );
        stmt.setString(2, msg.getVdbName());
        stmt.setObject(3, msg.getVdbVersion());
        stmt.setString(4, eventType);
        stmt.setString(5, msg.getApplicationName());
        stmt.setString(6, msg.getSessionID());
        stmt.setString(7, msg.getRequestID());
        stmt.setString(8, msg.getPrincipal());
        stmt.setObject(9, msg.getRowCount());
        
        stmt.execute();
        
        if (!connection.getAutoCommit()) {
        	connection.commit();
        }
        
        stmt.close();
   
    }
        
    private void logSourceNewEvent(CommandLogMessage msg, Connection connection, String eventType) throws SQLException {
         PreparedStatement stmt = connection.prepareStatement(SOURCE_NEW);
        

        stmt.setTimestamp(1,  new java.sql.Timestamp(msg.getTimestamp()) );
        stmt.setString(2, msg.getVdbName());
        stmt.setObject(3, msg.getVdbVersion());
        stmt.setString(4, eventType);
        stmt.setString(5, msg.getSessionID());
        stmt.setString(6, msg.getRequestID());
        stmt.setObject(7, msg.getSourceCommandID().longValue());            
        stmt.setString(8, msg.getTransactionID());
        stmt.setString(9, msg.getPrincipal());            
        stmt.setString(10, msg.getModelName());
        stmt.setString(11, msg.getTranslatorName());
        stmt.setString(12, msg.getSql());            
        
        stmt.execute();
        
        if (!connection.getAutoCommit()) {
        	connection.commit();
        }
        
        stmt.close();
        
    }
    
    private void logSourceEvent(CommandLogMessage msg, Connection connection, String eventType) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SOURCE_OTHER);

        stmt.setTimestamp(1,  new java.sql.Timestamp(msg.getTimestamp()) );
        stmt.setString(2, msg.getVdbName());
        stmt.setObject(3, msg.getVdbVersion());
        stmt.setString(4, eventType);
        stmt.setString(5, msg.getSessionID());
        stmt.setString(6, msg.getRequestID());
        stmt.setObject(7, msg.getSourceCommandID().longValue());            
        stmt.setString(8, msg.getTransactionID());
        stmt.setString(9, msg.getPrincipal());            
        stmt.setString(10, msg.getModelName());
        stmt.setString(11, msg.getTranslatorName());
        stmt.setObject(12, msg.getRowCount());
        stmt.setString(13, (msg.getPlan() != null ? msg.getPlan().toString() : "noplan") );
        
        stmt.execute();
        
        if (!connection.getAutoCommit()) {
        	connection.commit();
        }
        
        stmt.close();

    }
        
    
    private synchronized DataSource getDataSource(String datasourceName) throws NamingException {
        
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup( datasourceJNDIName );
        return ds;
        
    }

    private static List<String> split(String str, String splitter) {
        StringTokenizer tokens = new StringTokenizer(str, splitter);
        ArrayList<String> l = new ArrayList<String>(tokens.countTokens());
        while(tokens.hasMoreTokens()) {
            l.add(tokens.nextToken());
        }
        return l;
    }
}