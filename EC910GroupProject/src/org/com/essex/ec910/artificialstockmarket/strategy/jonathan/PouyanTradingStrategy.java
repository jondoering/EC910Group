package org.com.essex.ec910.artificialstockmarket.strategy.jonathan;

/**
 * @author Pouyan
 * 
 * This is a buy and hold long term strategy
 * enter (long) position when there is no open position, 
 * exit (sell) when total value of portfolio (money+shares) is greater or less than 
 * a percentage (eg: %1 = risk = 0.01) of last value of portfolio (at the time of openning the position)
 * 
 * 
 */

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;

import jas.engine.Sim;

public class PouyanTradingStrategy extends AbstractTrader {

	private long volume;                   // volume (size) of trading
	private double pouyanRisk;             // eg: 0.1 value of portfolio is target profit or stop loss
	private double lastPortfolioValue ;    // value of porfolio before opening a position    
	
	public PouyanTradingStrategy(String name, ArtificialMarket artificialMarket, 
			                     Portfolio portfolio, long max_buy, long max_sell,
			                     double pouyanRisk) {
		
		super(name, artificialMarket, portfolio, max_buy, max_sell);
		
		// TODO Auto-generated constructor stub
		this.pouyanRisk = pouyanRisk;
	}
	
	
	// Pouyan Trading Strategy
	@Override
	public Order runStrategy(){		 
		Order order = new Order(Order.BUY, Order.MARKET, 0, 0, this);// default order which will not be sent to the market (because volume = 0)

		if(this.portfolio.getShares() > 0){ //trader has shares in his portfolio
		
			if(Math.abs((this.getPortfolioValue()- this.lastPortfolioValue)/this.lastPortfolioValue) >= this.pouyanRisk){
			   order = new Order(Order.SELL, Order.MARKET, this.portfolio.getShares(), this.artificialMarket.getSpotPrice(), this); //sell all the shares	
			}
	
		}
		
		else{ // no shares --> enter long
			this.lastPortfolioValue = this.getPortfolioValue();
			
			//TODO change money to com Fee variant
			//this.volume =(long) ((this.this.getInvestableMoney()*this.pouyanRisk)/this.artificialMarket.getSpotPrice());
			
			
			this.volume =(long) ((this.getPortfolioValue()*this.pouyanRisk)/this.artificialMarket.getSpotPrice());
			
			//TODO check for max_buy volume
			//if(this.volume > max_buy)
			//{	this.volume = max_buy;}
			
			order = new Order(Order.BUY, Order.MARKET,this.volume, this.artificialMarket.getSpotPrice(), this);
		}

		return order;
	}

}
