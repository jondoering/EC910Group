package org.com.essex.ec910.artificialstockmarket.market;

import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;

/**
 * @author Jonathan
 *
 */
public class Order implements Comparable{

	public static int BUY = 0;
	public static int SELL = 1;
	public static int MARKET = 2;
	public static int LIMIT = 3;
	
	private int type1;
	private int type2;
	private int volume;
	private int limitprice;
	private AbstractTrader owner;
	
	
	/**
	 * @param type1
	 * @param type2
	 * @param volume
	 * @param limitprice
	 * @param owner
	 */
	public Order(int type1, int type2, int volume, int limitprice, AbstractTrader owner) {
		
		this.type1 = type1;
		this.type2 = type2;
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


	public int getVolume() {
		return volume;
	}


	public int getLimitprice() {
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
	public int compareTo(Object o) {
		
		
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
		return "Order [type1=" + type1 + ", type2=" + type2 + ", volume=" + volume + ", limitprice=" + limitprice
				+ ", owner=" + owner + "]";
	}

	
}
