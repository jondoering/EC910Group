package org.com.essex.ec910.artificialstockmarket.trader;

/**
 * @author Pouyan
 *
 */

public class Portfolio {


	int shares;   // number of shares in trader's portfolio 	
	int money;    // trader's capital (Integer)
		
	/**
	 * Constructor
	 * @param shares 
	 * @param money  
	 */
	public Portfolio(int shares, int money) {
		this.shares = shares;
		this.money = money;
	}
	
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	
}
