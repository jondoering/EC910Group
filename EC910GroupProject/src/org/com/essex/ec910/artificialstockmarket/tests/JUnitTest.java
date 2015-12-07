package org.com.essex.ec910.artificialstockmarket.tests;

import static org.junit.Assert.*;

import org.com.essex.ec910.artificialstockmarket.datasource.YQLConnector;
import org.junit.Test;

public class JUnitTest {

	@Test
	public void test() throws Exception {

		//Test Connector Class
		YQLConnector yql = new YQLConnector("MSFT");
		yql.addData("2013-02-01", "2014-2-15");
		 yql.addData("2014-02-01", "2015-2-15");
//		YQLConnector yql = new YQLConnector("MSFT", "2013-02-01", "2015-2-15");
		
		//fail("Not yet implemented");
	}

}
