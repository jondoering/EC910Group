package org.com.essex.ec910.artificialstockmarket.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
/**
 * @author MAO WEIGUANG
 *
 */
public class WilliamTradingStrategy extends AbstractTrader  {
    
	private double RiskWeight;
    private long number;
	
	public WilliamTradingStrategy(String name, ArtificialMarket artificialMarket, Portfolio portfolio, long max_buy,
			long max_sell) {
		super(name, artificialMarket, portfolio, max_buy, max_sell);
		// TODO Auto-generated constructor stub
		//this.RiskWeight= RiskWeight;
		
	}	
	
	@Override
	public Order runStrategy(){
		
		Order order = new Order(Order.BUY, Order.MARKET, 0, 0, this);
	
		if(this.lastPortfolio.getShares() > 0 ){
			
			if(this.profitFactor>1){
			   order = new Order(Order.BUY, Order.MARKET, portfolio.getShares(), artificialMarket.getSpotPrice(), this); 	
			}
		}
		
		else{ // no shares --> enter long
		//	this.lastPortfolio = this.getPortfolioValue();
			this.number =(long) ((this.getPortfolioValue()*this.numWinTrades)/this.artificialMarket.getSpotPrice());
			order = new Order(Order.SELL, Order.MARKET,number, artificialMarket.getSpotPrice(), this);
		}

		return order;
	}


}
