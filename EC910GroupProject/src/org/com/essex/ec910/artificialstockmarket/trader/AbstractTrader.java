package org.com.essex.ec910.artificialstockmarket.trader;

import java.util.ArrayList;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;


/**
 * 
 * 
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
	public long max_buy;                        // maximum amount of shares that trader can buy      
	public long max_sell;                       // minimum amount of shares that trader can sell

	//  metrics for measuring the performance of trading strategy
	public double profit_loss;                     // P&L of trader
	public int ROI;                             // return of investment 
	public int numTrades;                       // number of total trades
	public int numWinTrades;                    // number of winning trades
	
	private double winningRate;                     // = numWinTrades / numTrades
    
	private int transactionCounter = 0;
	private double comFee;
	
	/**
	 * Constructor
	 * @param name - trader name
	 * @param artificialMarket  - reference to market
	 * @param portfolio  - portfolio object 
	 * @param max_buy - maximum number of shares that can be bought by one order
	 * @param max_sell - maximum number of shares that can be sold by one order
	 */
	public AbstractTrader(String name, ArtificialMarket artificialMarket, Portfolio portfolio, long max_buy , long max_sell) {
		this.name = name;
		this.artificialMarket = artificialMarket;
		this.portfolio = portfolio;
		this.max_buy = max_buy;
		this.max_sell = max_sell;
		this.numWinTrades = 0;
		this.numTrades=0;
		
		this.lastPortfolio = new Portfolio(0,0);
		this.lastPortfolio.setMoney(this.portfolio.getMoney());
	}

	/**
	 * run your trading strategy (Override this function)
	 * if you want to send order to market, you should not directly send your order, 
	 * because it should be double checked by sendFinalOrderToMarket() 
	 * if you do not want to send order to market, make sure your order volume is 0
	 *     
	 * @return order that should be send to market
	 */
	public Order runStrategy(){

		Order order; 

		// your trading algorithm ...

		order = new Order(0, 0, 0, 0, this);// default order which will not be sent to the market (because volume = 0)
		return order;
	}
	
	
	/**
	 * Final check to make sure that trader sends correct order to market 
	 * also this function, updates the performance results of trader (???) 
	 * please do not change this function 
	 */
	public void sendFinalOrderToMarket(){
		
		Order order = runStrategy();
		Integer[] spotPrice = this.artificialMarket.getLastNPrice(1);
		
		//check volume of order for intelligent trader only
		//Market maker and RandomTrader send there orders every time / have no constrains
		if ((this instanceof MarketMaker || this instanceof RandomTrader) ||
			(order.getVolume() > 0 && this.getPortfolioValue() > 0)  		                                           // check that Volume > 0, otherwise, it means that trader does not want to send order to market
			&& ((order.getType1() == Order.BUY && order.getVolume()<= this.max_buy )   // check that if trader wants to buy assets, the volume of his order <= limit to buy
			    || (order.getType1() == Order.SELL && order.getVolume()<= this.max_sell))){    // check that if trader wants to buy assets, the volume of his order <= limit to sell 
			
			//check order before sending it to artificial market in order to make sure it is a valid order
//			if((order.getType1() == Order.BUY && order.getType2() == Order.LIMIT && order.getVolume() <= this.max_buy && this.portfolio.getMoney() >= order.getVolume()*order.getLimitprice()) ||     //  buy limit order --> (trader should have enough money to buy shares) trader wants to buy and volume should be < max limit for buying
//					(order.getType1() == Order.BUY && order.getType2() == Order.MARKET && order.getVolume() <= this.max_buy && this.portfolio.getMoney() >= order.getVolume()*spotPrice)  ||            // ??? put [0] in front spot price when using Jonathan market ???? buy market order --> (trader should have enough money to buy shares)
//					(order.getType1() == Order.SELL && order.getVolume() <= this.max_sell && order.getVolume() <= this.portfolio.getShares())) {//  sell order --> trader wants to sell and volume < max limit for selling // also trader should have enough shares in his portfolio in order to be able to sell the desired volume 
            
			//final verification of order
//			if((order.getType1() == Order.BUY && this.getInvestableMoney() >= order.getVolume()*this.artificialMarket.getSpotPrice())  // check that if trader wants to buy an asset, he should have enough money in his portfolio
//			    ||(order.getType1() == Order.SELL && order.getVolume() <= this.portfolio.getShares())){                                // check that if trader wants to sell assets,  he can not sell more than the number of shares in his portfolio
				
				if(!(this instanceof RandomTrader))
				{
					System.out.println(this.name + ": " + order.toString());
				}
												
				this.artificialMarket.reciveOrder(order); // send a valid order to artificial market
			
//			}//if check
		}

		  	 
		// update the performance results of trader
		// ???   	 
	}
  
	/**
	 * reduce shares from portfolio of trader and increase the money
	 * to be used by artificial market, not by trader  
	 * @param money - amount of money to add to portfolio
	 * @param shares - amount of shares to substract from portfolio
	 */
	public void buyShareFromTrader(long money, long shares){
				
		//adjustment by commission fee
		double m = this.portfolio.getMoney() + money*(1-comFee);
		
		this.portfolio.setMoney(m);
		this.portfolio.setShares(this.portfolio.getShares() - shares);
			
		
		this.transactionCounter++;
		this.profit_loss = this.portfolio.getMoney() - this.lastPortfolio.getMoney(); // update P&L
			
		if(this.profit_loss > 0)
		{//profit
		   this.numWinTrades++;		   
		   this.winningRate = this.numWinTrades/this.transactionCounter;
		   System.out.println(this.winningRate);
			}
			
			this.lastPortfolio.setMoney(this.portfolio.getMoney());;      // save the last portfolio of trader when final order sent to market
				
		
	}

	/**
	 * reduce money from portfolio of trader and increase the shares
	 * to be used by artificial market, not by trader  
	 * @param money - amount of money to substract from portfolio
	 * @param shares - amount of shares to add to portfolio
	 */
	public void sellShareToTrader(long money, long shares){
		
		//adjustment by commission fee
		double m = this.portfolio.getMoney() - money*(1+comFee);

		this.portfolio.setMoney(m);
		this.portfolio.setShares(this.portfolio.getShares() + shares);
		
	}
	
	/**
	 * calculates actual value of portfolio by money and shares value
	 * @return
	 */
	public double getPortfolioValue()
	{
		return portfolio.getMoney() + portfolio.getShares()*artificialMarket.getSpotPrice();
	}
	
	public String toString()
	{
		return name;
	}
	
	/**
	 * sets comission fee
	 * @param comFee - comission fee as value between 0 and 1
	 */
	public void setCommissionFee(final double comFee)
	{
		this.comFee = comFee;
	}
	
	/**
	 * calculates money that is available for investment with respect to commission fee
	 * @return - investable amount of money
	 */
	protected double getInvestableMoney()
	{
		return portfolio.getMoney() * (1-comFee);
	}
	
	/**
	 * A transaction is defined as buy and sell and is counted after the sell order 
	 * (even if the sell order isn't executed) 
	 * @return - number of transactions done by the trader
	 */
	public int getTransactionCount()
	{
		return transactionCounter;
	}

	/**
	 * @return
	 */
	public double getProfit_loss() {
		return profit_loss;
	}

	/**
	 * @return
	 */
	public double getWinningRate() {
		return winningRate;
	}
	
	
	
}

