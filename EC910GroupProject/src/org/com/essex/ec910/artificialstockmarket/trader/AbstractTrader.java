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
	}


	
	
}
