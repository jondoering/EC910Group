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


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		
		if(this.getLimitprice() < (((Order) o).getLimitprice()))
		{	return -1;}
		else if(this.getLimitprice() > (((Order) o).getLimitprice()))
		{	return 1;}
		else
		{	return 0;}
		
	}

	
}
