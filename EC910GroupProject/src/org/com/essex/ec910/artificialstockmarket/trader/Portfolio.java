package org.com.essex.ec910.artificialstockmarket.trader;

/**
 * @author Pouyan
 *
 */

public class Portfolio {


	private long shares;   // number of shares in trader's portfolio 	
	private double money;    // trader's capital
		
	/**
	 * Constructor
	 * @param shares 
	 * @param money  
	 */
	public Portfolio(int shares, int money) {
		this.shares = shares;
		this.money = money;
	}
	
	public long getShares() {
		return shares;
	}
	
	public void setShares(long shares) {
		this.shares = shares;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
	
}
