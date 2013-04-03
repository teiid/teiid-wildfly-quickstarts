package org.jboss.teiid.businessrules.udf;

import java.math.BigDecimal;
import static org.junit.Assert.assertTrue;

import org.jboss.teiid.businessrules.udf.RulesUDF;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestRulesUDF {

	@Mock
	private org.teiid.CommandContext context;
	
	   @Before
	    public void setUp() throws Exception {
	    	
	    	MockitoAnnotations.initMocks(this);
	    }

	
	@Test public void testReturnValue() throws Exception {
		System.setProperty(RulesUDF.SYSTEM_PROPERTY, "org.jboss.teiid.quickstart.data.MarketData:MyBusinessRules.drl");
		
		String result = (String) RulesUDF.performRuleOnData(context, "org.jboss.teiid.quickstart.data.MarketData", "getInvalidMessage", "noMsg", "Red Hat", "RHT", new BigDecimal(56.213) );

		assertTrue(result != null && !result.equals("noMsg"));
	}
	
	@Test public void testReturnNoValue() throws Exception {
		System.setProperty(RulesUDF.SYSTEM_PROPERTY, "org.jboss.teiid.quickstart.data.MarketData:MyBusinessRules.drl");
		
		String result = (String) RulesUDF.performRuleOnData(context, "org.jboss.teiid.quickstart.data.MarketData", "getInvalidMessage", "noMsg", "Red Hat", "RHT", new BigDecimal(49.99) );

		assertTrue(result != null && result.equals("noMsg"));
	}	
}
