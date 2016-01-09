package org.com.essex.ec910.artificialstockmarket.strategy;


import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;

/**
 * @author Pouyan
 *  
 *  This is a buy and hold long term strategy
 * enter (long) position when there is no open position
 * exit (sell) At target profit or  stop loss
 */

import jas.engine.Sim;

public class PouyanTradingStrategy extends AbstractTrader {

	private long pouyanVolume;                 // volume (size) of trading
	private long pouyanTargetProfit;           // target profit for pouyan trading strategy
	private long pouyanStopLoss;               // stop loss for pouyan trading strategy
	private long buyPrice;                     // price when buy order is send to market (entering price)
	
	public PouyanTradingStrategy(String name, ArtificialMarket artificialMarket, 
			                     Portfolio portfolio, long max_buy, long max_sell,
			                     long pouyanVolume, long pouyanTargetProfit,long pouyanStopLoss) {
		
		super(name, artificialMarket, portfolio, max_buy, max_sell);
		
		//first setting
		this.pouyanVolume = pouyanVolume;
		this.pouyanTargetProfit = pouyanTargetProfit; 
		this.pouyanStopLoss = pouyanStopLoss;
	}
	
	
	// Pouyan Trading Strategy
	@Override
	public Order runStrategy(){		 
		Order order = new Order(Order.BUY, Order.MARKET, 0, 0, this);// default order which will not be sent to the market (because volume = 0)

		if(this.portfolio.getShares() > 0  //trader has shares in his portfolio 
				&& ( this.artificialMarket.getSpotPrice() - this.buyPrice >= this.pouyanTargetProfit    // we have reached target profit
				||  this.buyPrice - this.artificialMarket.getSpotPrice() >= this.pouyanStopLoss)){  // we have reached stop loss                                      
		
			   order = new Order(Order.SELL, Order.MARKET, this.portfolio.getShares(), this.artificialMarket.getSpotPrice(), this); //sell all the shares	

		}
		
		if(this.portfolio.getShares() == 0 ) { // no shares --> enter long 
	         
				this.buyPrice = this.artificialMarket.getSpotPrice(); //remember the price of buying stocks
				order = new Order(Order.BUY, Order.MARKET,this.pouyanVolume, this.buyPrice, this);	    

		}

		return order;
	}

}
