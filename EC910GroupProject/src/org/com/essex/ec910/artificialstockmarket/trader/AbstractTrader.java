package org.com.essex.ec910.artificialstockmarket.trader;

import java.util.ArrayList;
import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;

/**
 * @author Pouyan
 *
 */

public class AbstractTrader {
	
//	general parameters of trader
	public String name;                         // trader's name eg: pouyan, Jonathan, randomTrader and ...
	public ArtificialMarket artificialMarket;   // trader has access to artificial market
	public Portfolio portfolio;                 // trader's portfolio 
	public ArrayList<Order> openPositions;      // list of current open orders 
	public ArrayList<Order> closedPositions;    // list of closed orders 

//  metrics for measuring the performance of trading strategy
	public int profit_loss;                     // P&L of trader
	public int ROI;                             // return of investment 
	public int numTrades;                       // number of total trades
	public int numWinTrades;                    // number of winning trades
	public int profitFactor;                    // = numWinTrades / numTrades
	
	/**
	 * Constructor
	 * @param name 
	 * @param artificialMarket  
	 * @param portfolio  
	 */
    public AbstractTrader(String name, ArtificialMarket artificialMarket, Portfolio portfolio) {
		this.name = name;
		this.artificialMarket = artificialMarket;
		this.portfolio = portfolio;
	}
	
//	run trading strategy
	public void runStrategy(){
		// ???
		
		// Get Price (10)
		// SMA
		// SMA1 > SMA2
		//	Buy
		//	 	Check Portfolio how many many is in? (1000)
		/*		How many shares I can buy?
					Can buy 100 Shares.
					Decide new price? (11/ Market)
					
					this.artificialMarket.reciveOrder(new Order(Buy, Market, 100, 0, this);
					
					//Wait 
					 * 
		// Make a decision
		*/ 
		//    	
		//		decide Shares for Price
		//    
		//this.artificialMarket.reciveOrder()
		// Send order
		
	}

//
//	public void giveOneShare(money)
//	{
//		//
//		Portfolio.money += money;
//		Porfolio.Share -= 1;		
//	}
//	
//	public moeny takeOneShare(price)
//	{
//		Portfolio.money -= price;
//		Portfolio.Share =+ 1;
//	}
//	
//	public int setIfOrderGetsFullfied()
//	{
//		gotsFullfied = true;
//	
//	}
//	
	
}
