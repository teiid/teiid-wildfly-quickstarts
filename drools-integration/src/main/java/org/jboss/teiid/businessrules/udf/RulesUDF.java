/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, JBoss Inc., and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 20011-2012,
 * @author JBoss Inc.
 */

package org.jboss.teiid.businessrules.udf;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.teiid.core.util.ReflectionHelper;
import org.teiid.core.util.StringUtil;

/**
 * @author <a href="mailto:vhalbert@redhat.com">vhalbert@redhat.com</a>
 */
public class RulesUDF {

	static final String SYSTEM_PROPERTY = "org.teiid.drools.UDF";
	static final String CLASS_RESOURCE_DELIM = ":";
	static final String MULTIPLE_DELIM = ",";
	
	// settings in the form of  className:DRL_fileName[,className:DRL_fileName[...]]
	private static final String UDF_SETTINGS = System.getProperty(SYSTEM_PROPERTY);

    private static KnowledgeBase kbase;
    
    static {
    	if (UDF_SETTINGS == null || UDF_SETTINGS.length() == 0) {
    	   String msg = "System property " + SYSTEM_PROPERTY + " was not defined";
           System.err.println(msg);
           throw new RuntimeException(msg);

    	}

        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();        

    	// multiple combinations can be specified using deliminter
    	List<String> multipleOptions = StringUtil.getTokens(UDF_SETTINGS, MULTIPLE_DELIM);
    	for (String o:multipleOptions) {
    		List<String> settings=StringUtil.getTokens(o, CLASS_RESOURCE_DELIM);
    		
    		String clzz=settings.get(0);
    		String drl=settings.get(1);
    		
    	        try {
					kbuilder.add(ResourceFactory.newClassPathResource(drl, Class.forName(clzz) ), ResourceType.DRL);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException(e);
				}
    	        
    	        if (kbuilder.hasErrors()) {
    	            
    	            System.err.println(kbuilder.getErrors().toString());
    	            
    	            throw new RuntimeException("Unable to compile: " + clzz + " for DRL: " + drl);
    	            
    	        }

    	}       
        
        // get the compiled packages (which are serializable)
        
        final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
        
        
        // add the packages to a KnowledgeBase (deploy the knowledge packages).
        
        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        
        kbase.addKnowledgePackages(pkgs);
           
    }

	public static Object performRuleOnData(final org.teiid.CommandContext commandContext, final String className, final String returnMethodName, final Object returnIfNull, final Object... arguments) {
	    final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

		try {
 	        
 	        Collection<?> objs = Arrays.asList(arguments);
 	        
 	        Object ruleObject = ReflectionHelper.create(className, objs, Thread.currentThread().getContextClassLoader());
        
	        ksession.insert(ruleObject);
	        
	        ksession.fireAllRules();
        
	        Method m = ruleObject.getClass().getMethod(returnMethodName, new Class[0]);
			Object rtn = m.invoke(ruleObject, new Object[0]);
			if (rtn == null) {
				return returnIfNull;
			}
			return rtn;
		} catch (Exception e) {
			commandContext.addWarning(e);
			return e.getMessage();
		} finally {
	        ksession.dispose();
		}
       
	}


}
