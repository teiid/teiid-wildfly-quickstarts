package org.jboss.teiid.drools;

import java.util.ArrayList;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class HelloWorldExample {

	public static void main(String[] args) {

		KieServices ks = KieServices.Factory.get();
		
		KieContainer kc = ks.newKieClasspathContainer();

		KieSession ksession = kc.newKieSession("HelloWorldKS");

		ksession.setGlobal("list", new ArrayList<Object>());

		final Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);
		ksession.insert(message);

		ksession.fireAllRules();

		ksession.dispose();
	}

}
