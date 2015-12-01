package org.com.essex.ec910.artificialstockmarket.market;

import org.com.essex.ec910.artificialstockmarket.datasource.DatabaseConnector;

/**
 * @author Jonathan
 *
 */
public class LifeMarket {

	private int lastPrice;
	
	private DatabaseConnector db;
	
	/**
	 * @param db
	 */
	public LifeMarket(DatabaseConnector db)
	{
		this.db = db;
	}
	
	
	/**
	 * @return
	 */
	public int getLastLifePrice()
	{
		//How to deal with prices
		return lastPrice;
	}
	
	
	/**
	 * updates lastPrice with next Real Price in the Database
	 */
	private void updateLastLifePrice()
	{		
		
		//this.lastPrice = lastPrice;
	}
	
	
}
