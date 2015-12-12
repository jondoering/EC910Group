package org.com.essex.ec910.artificialstockmarket.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.com.essex.ec910.artificialstockmarket.datasource.YQLConnector;
import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;
import org.com.essex.ec910.artificialstockmarket.trader.RandomTrader;
import org.junit.Test;

public class JUnitTest {

	@Test
	public void test() throws Exception {

		//Test Connector Class
		//YQLConnector yql = new YQLConnector("MSFT");
		//yql.addData("2013-02-01", "2014-2-15");
		 //yql.addData("2014-02-01", "2015-2-15");
//		YQLConnector yql = new YQLConnector("MSFT", "2013-02-01", "2015-2-15");
		
		 testClearing();
		//fail("Not yet implemented");
	}
	
	
	
	private void testClearing()
	{
		
		ArtificialMarket m = new ArtificialMarket(null);
		
		RandomTrader a = new RandomTrader("A", m, new Portfolio(1000, 0), 0, 0);
		
		int num =100;
		Order[] o = new Order[num];
		
		
		for (int i=0; i<num; i++)
		{
			
			int type1 = 0;
			int type2 = 0;
			
			if(Math.random()>0.5)
			{	type1 = Order.BUY;}
			else
			{	 type1 = Order.SELL;}
			
			if(Math.random()>0.5)
			{
				 type2 = Order.MARKET;	
			}
			else
			{
				 type2 = Order.LIMIT;
			}
			int price = randInt(10,50);
			int volume = randInt(100,1000);
			o[i] = new Order(type1, type2, volume, price, a);
			m.reciveOrder(o[i]);
			
			
		}
		
		/*
		int type1 = Order.BUY;
		int type2 = 0;
		
		o[0] = new Order(type1, Order.MARKET, 500, 0, a);
		o[1] = new Order(type1, Order.LIMIT, 300, 102, a);
		o[2] = new Order(type1, Order.LIMIT, 70, 101, a);
		o[3] = new Order(type1, Order.LIMIT, 100, 100, a);
		o[4] = new Order(type1, Order.LIMIT, 50, 99, a);
		o[5] = new Order(type1, Order.LIMIT, 200, 98, a);
		o[6] = new Order(type1, Order.LIMIT, 300, 97, a);
		o[7] = new Order(type1, Order.LIMIT, 50, 96, a);
		
		type1 = Order.SELL;
		o[8] = new Order(type1, Order.MARKET, 300, 0, a);
		o[9] = new Order(type1, Order.LIMIT, 300, 92, a);
		o[10] = new Order(type1, Order.LIMIT, 100, 93, a);
		o[11] = new Order(type1, Order.LIMIT, 200, 94, a);
		o[12] = new Order(type1, Order.LIMIT, 400, 95, a);
		o[13] = new Order(type1, Order.LIMIT, 40, 96, a);
		o[14] = new Order(type1, Order.LIMIT, 200, 97, a);
		o[15] = new Order(type1, Order.LIMIT, 300, 98, a);
		o[16] = new Order(type1, Order.LIMIT, 100, 99, a);
		*/
		
		for (int i=0; i<num; i++)
		{
			m.reciveOrder(o[i]);						
		} 
		
		
		m.clearMarket();	

		
	}
	
	
	public static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}
