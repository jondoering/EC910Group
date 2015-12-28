package org.com.essex.ec910.artificialstockmarket.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.com.essex.ec910.artificialstockmarket.datasource.YQLConnector;
import org.com.essex.ec910.artificialstockmarket.market.*;
import org.com.essex.ec910.artificialstockmarket.market.Order;
import org.com.essex.ec910.artificialstockmarket.statistics.Statistics;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.MarketMakerJon;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;
import org.com.essex.ec910.artificialstockmarket.trader.jonathan.RandomTraderJonathan;
import org.junit.Test;

public class JUnitTest {

	@Test
	public void test() throws Exception {

		 //testClearing();
		//testMarketMaker();

	}
	
	
	private void testMarketMaker() throws Exception
	{
	
		LifeMarket lf = new LifeMarket("MSFT", "2010-02-01", "2014-2-15");
		
		int[] initPrices = new int[10];
		int[] initVolumes = new int[10];
		
		for(int i=0;i<10;i++)
		{
			initPrices[i] = lf.getPrice(i).getPrice();
			initVolumes[i] = lf.getPrice(i).getVolume();
 		}
		
		ArtificialMarket m = new ArtificialMarket( initPrices, initVolumes, false, false);

		MarketMakerJon mm = new MarketMakerJon("MM", lf, m, initVolumes[2], 0.1, 10);
		
		
	}
	
	
	private void testClearing(ArtificialMarket m)
	{
		
		
		RandomTraderJonathan a = new RandomTraderJonathan("A", m, new Portfolio(1000, 0), 0, 0, 0);
		
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

//	@Test
//	public void testStatistic()
//	{
//		int[] d = new int[100];
//		for(int i=0;i<100;i++)
//		{	d[i] = i;}
//		
//		Statistics s = new Statistics(null);
//
//		Arrays.stream(d).forEach(n -> s.updatePrice(n));
//	
//		//assertEquals(String.format("%.3f",new Double(s.getVariance()).toString()), "7.500");
//		System.out.println(s.getVariance());
//		System.out.println(s.getSkewness());
//		System.out.println(s.getKurtosis());
//		System.out.println(s.getMu());
//
//		
//	}
}

