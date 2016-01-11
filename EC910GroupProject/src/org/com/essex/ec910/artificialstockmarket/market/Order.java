package org.com.essex.ec910.artificialstockmarket.market;

import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;

/**
 * 
 * This class implements a model of an order. 
 * Two order types (market and limit) are support as well
 * as a mechanism for sorting.
 * 
 * @author Jonathan Doering
 *
 */
public class Order implements Comparable<Order>{

	public static int BUY = 0;
	public static int SELL = 1;
	public static int MARKET = 2;
	public static int LIMIT = 3;
	
	private int type1;
	private int type2;
	private long volume;
	private long limitprice;
	private AbstractTrader owner;
	
	
	/**
	 * Constructor
	 * 
	 * @param type1 - sell or buy
	 * @param type2 - limit or market
	 * @param volume - order volume
	 * @param limitprice - if limit order the specified price
	 * @param owner - issuer of the order
	 */
	public Order(int type1, int type2, long volume, long limitprice, AbstractTrader owner) {
		
		this.type1 = type1; //Sell or Buy
		this.type2 = type2; // Limit or Market
		this.volume = volume;
		this.limitprice = limitprice;
		this.owner = owner;
	}


	public int getType1() {
		return type1;
	}


	public int getType2() {
		return type2;
	}


	public long getVolume() {
		return volume;
	}


	public long getLimitprice() {
		return limitprice;
	}


	public AbstractTrader getOwner() {
		return owner;
	}


	/* Compares to Orders to get a Order in the order book for clearing
	 * Mechanism:
	 * 	Buy Order:
	 * 		this.Market - o.Market: equal
	 * 		this.Market - o.Limit: bigger
	 * 		this.Limit  - o.Market: smaller
	 * 		this.Limit  - o.Limit: Compare by Price 
	 * 
	 * Sell Order:
	 *		this.Market - o.Market: equal
	 * 		this.Market - o.Limit: smaller
	 * 		this.Limit  - o.Market: bigger
	 * 		this.Limit  - o.Limit: Compare by Price 
	 * 
	 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Order o) {
		
		
			if(this.getType2() == Order.MARKET && (((Order) o).getType2() == Order.MARKET))
			{
					return  0;	
			}	
			if(this.getType2() == Order.LIMIT && (((Order) o).getType2() == Order.LIMIT))					
			{
				if(this.getLimitprice() < (((Order) o).getLimitprice()))			
				{	return -1;}
				else if(this.getLimitprice() > (((Order) o).getLimitprice()))
				{	return 1;}
				else
				{	return 0;}				
			}
			else if(this.getType2() == Order.MARKET && (((Order) o).getType2() == Order.LIMIT))					
			{
				if(this.getType1() == Order.SELL)
				{
					return -1;
				}
				else //BUY
				{
					return -1;
				}
			}
			else 				//LIMIT - MARKET	
			{
				if(this.getType1() == Order.SELL)
				{
					return 1;
				}
				else //BUY
				{
					return 1;
				}
			}				
		
	}


	@Override
	public String toString() {
		String t1,t2;
		
		if(type1 == Order.BUY)
		{
			t1 = "BUY";
		}
		else
		{
			t1 = "SELL";
		}
		
		if(type2 == Order.MARKET)
		{
			t2 = "MARKET";
		}
		else
		{
			t2 = "LIMIT";
		}
		
		return "Order [type1=" + t1 + ", type2=" + t2 + ", volume=" + volume + ", limitprice=" + limitprice
				+ ", owner=" + owner.toString() + "]";
	}

	
}
