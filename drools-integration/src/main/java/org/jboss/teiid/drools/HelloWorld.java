package org.jboss.teiid.drools;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class HelloWorld {
	
	public static Object performRuleOnData(String className, String message, int status){

		Message msg;
	
		try {
			msg = (Message) Class.forName(className).newInstance();
			msg.setMessage(message);
			msg.setStatus(status);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
				
		KieServices ks = KieServices.Factory.get();
		
		ClassLoader loader = HelloWorldExample.class.getClassLoader();
		KieContainer kc = ks.newKieClasspathContainer(loader);
		
		KieSession ksession = kc.newKieSession("HelloWorldKS");
		ksession.setGlobal( "list", new ArrayList<Object>() );
		ksession.insert( msg );
		ksession.fireAllRules();
		ksession.dispose();
	
		return "success";
	}

		
	
	public static Object performRuleOnData(final String className, final String[] fileds, final Object[] arguments){
		
		Object obj;
		
		try {
			obj = Class.forName(className).newInstance();
			Class<? extends Object> clas = obj.getClass();
			for(int i = 0 ; i < fileds.length ; i ++) {
				Field field = clas.getDeclaredField(fileds[i]);
				field.setAccessible(true);
				field.set(obj, arguments[i]);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		KieServices ks = KieServices.Factory.get();
		KieContainer kc = ks.getKieClasspathContainer();
		KieSession ksession = kc.newKieSession("HelloWorldKS");
		ksession.setGlobal( "list", new ArrayList<Object>() );
		ksession.insert( obj );
		ksession.fireAllRules();
		ksession.dispose();
		
		return "success";
	}

}
