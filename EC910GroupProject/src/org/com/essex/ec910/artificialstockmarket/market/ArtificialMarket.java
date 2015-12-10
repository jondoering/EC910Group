package org.com.essex.ec910.artificialstockmarket.market;

import org.com.essex.ec910.artificialstockmarket.datasource.DatabaseConnector;
import java.util.ArrayList;

/**
 * 
 * @author Jonathan
 *
 */
public class ArtificialMarket {
	
	
	private DatabaseConnector db;
	
	private ArrayList<Order> bidOrderBook;
	private ArrayList<Order> askOrderBook;
	
	private ArrayList<Integer> lastPrice;
	


	/**
	 * Constructor
	 * @param db
	 */
	public ArtificialMarket(DatabaseConnector db)
	{
		this.db = db;
	}
	
	
	/**
	 * order driven clearing mechanism
	 * clearing price maximize the execution volume (Compare XYZ)
	 */
	private void clearMarket()
	{
		//
		/*
		 * Sort orderbook buy & sell by price
		 * Make cumulatet Voluyme
		 * Set price where max volium
		 * 
		 * Know:
		 * 	new price
		 *  number of shares we have to sell
		 * 
		 *Make a list of all buyers that wantet to buy for price
		 *Make a list of all seller that wantet to sell for price
		 * 
		 * While(volume is not full)
		 * 	Buy from random seller 1 share
		 * 	volume +1;
		 * 
		 * While(volume is not empty)
		 * 	Sell 1 Stock to random buyer;
		 *  volume -1;
		 *  
		 *  
		 */
	}
	
	
	/**
	 * adds an Order to the order book
	 * @param order 
	 */
	public void reciveOrder(Order order)
	{
		if(order.getType1() == Order.BUY)
		{	askOrderBook.add(order);}
		
		else if(order.getType2() == Order.SELL)
		{	bidOrderBook.add(order);}
		
		else
		{	}//do nothing, Order isn't allowed 
	}
	
	
	/**
	 * Returns the last n prices as an Array of integer in order is [t, t-1, t-2, .. , t-n]. 
	 * If n is grater then the price list, the whole price list will be returned. 
	 * 
	 * @param n - number of prices to show
	 * @return array of integer with last n prices
	 */
	public int[] getLastNPrice(int n)
	{
		if(n>lastPrice.size())
		{	n = lastPrice.size();}
		
		int[] lastPrices = new int[n];
		
		for(int i=0; i<n; i++)
		{
			lastPrices[i] = lastPrice.get(lastPrice.size()-(i+1));
		}
		
		return lastPrices;
		
	}

}
