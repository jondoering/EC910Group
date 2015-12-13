package org.com.essex.ec910.artificialstockmarket.trader;

import java.util.ArrayList;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;


/**
 * @author Pouyan
 *
 */

public abstract class AbstractTrader {

	//	general parameters of trader
	public String name;                         // trader's name eg: pouyan, Jonathan, randomTrader and ...
	public ArtificialMarket artificialMarket;   // trader has access to artificial market
	public Portfolio portfolio;                 // current portfolio of trader
	public Portfolio lastPortfolio;             // last portfolio of trader before sending the final oder to market (memory of trader)
	public Order lastOrder;                     // last final order of trader that has been double checked and sent to market (memory of trader)  
	public int max_buy;                         // maximum amount of shares that trader can buy      
	public int max_sell;                        // minimum amount of shares that trader can sell

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
	 * @param max_buy
	 * @param max_sell
	 */
	public AbstractTrader(String name, ArtificialMarket artificialMarket, Portfolio portfolio, int max_buy , int max_sell) {
		this.name = name;
		this.artificialMarket = artificialMarket;
		this.portfolio = portfolio;
		this.max_buy = max_buy;
		this.max_sell = max_sell;
	}

	//	run your trading strategy (Override this function)
	//  if you want to send order to market, you should not directly send your order, 
	//  because it should be double checked by sendFinalOrderToMarket() 
	//  if you do not want to send order to market, make sure your order volume is 0    
	public Order runStrategy(){
	
	

		Order order; 

		// your trading algorithm ...

		order = new Order(0, 0, 0, 0, this);// default order which will not be sent to the market (because volume = 0)
		return order;
	}
	
	
	//  Final check to make sure that trader sends correct order to market 
	//	also this function, updates the performance results of trader (???) 
	//	please do not change this function
	public void sendFinalOrderToMarket(){
		
		Order order = runStrategy();
//		int[] spotPrice = this.artificialMarket.getLastNPrice(1); for artificial market
		Integer[] spotPrice = this.artificialMarket.getLastNPrice(1);
		
		//check that Volume > 0, otherwise, it means that trader does not want to send order to market
		if (order.getVolume() > 0){
//			checking to be completed in future
//			if((order.getType1() == Order.BUY && order.getType2() == Order.LIMIT && order.getVolume() <= this.max_buy && this.portfolio.getMoney() >= order.getVolume()*order.getLimitprice()) ||     //  buy limit order --> (trader should have enough money to buy shares) trader wants to buy and volume should be < max limit for buying
//					(order.getType1() == Order.BUY && order.getType2() == Order.MARKET && order.getVolume() <= this.max_buy && this.portfolio.getMoney() >= order.getVolume()*spotPrice)  ||            // ??? put [0] in front spot price when using Jonathan market ???? buy market order --> (trader should have enough money to buy shares)
//					(order.getType1() == Order.SELL && order.getVolume() <= this.max_sell && order.getVolume() <= this.portfolio.getShares())) {//  sell order --> trader wants to sell and volume < max limit for selling // also trader should have enough shares in his portfolio in order to be able to sell the desired volume 

				this.artificialMarket.reciveOrder(order);
				this.lastOrder = order; // save final order sent to market
				this.lastPortfolio = this.portfolio;// save the last portfolio of trader when final order sent to market
//			}
		}

		//   	 ????????????????????????????????????????????????
		//    	 to be completed: update the performance results of trader
		//    	 ????????????????????????????????????????????????
	}

	//  reduce shares from portfolio of trader and increase the money 
	//  to be used by artificial market, not by trader  
	public void buyShareFromTrader(int money, int shares){
		this.portfolio.setMoney(this.portfolio.getMoney() + money);
		this.portfolio.setShares(this.portfolio.getShares() - shares);
		
	}

	//  reduce money from portfolio of trader and increase the shares
	//  to be used by artificial market, not by trader 
	public void sellShareToTrader(int money, int shares){
		this.portfolio.setMoney(this.portfolio.getMoney() - money);
		this.portfolio.setShares(this.portfolio.getShares() + shares);
		
	}

}
