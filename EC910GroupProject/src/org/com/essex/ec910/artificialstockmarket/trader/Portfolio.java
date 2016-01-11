package org.com.essex.ec910.artificialstockmarket.trader;

/**
 * Implementation of a simple portfolio representation
 * containing money and shares.
 *  
 * @author Pouyan Dinarvand
 *
 */

public class Portfolio {


	private long shares;   // number of shares in trader's portfolio 	
	private double money;    // trader's capital
		
	/**
	 * Constructor
	 * @param shares - initial shares
	 * @param money  - initial money
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
	
	public void setMoney(double money) {
		this.money = money;
	}

	public double getMoney() {
		return money;
	}
	
	
}
