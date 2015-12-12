package org.com.essex.ec910.artificialstockmarket.trader;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;

public class RandomTrader extends AbstractTrader {

	public RandomTrader(String name, ArtificialMarket artificialMarket, Portfolio portfolio, int max_buy,
			int max_sell) {
		super(name, artificialMarket, portfolio, max_buy, max_sell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Order runStrategy(){
        
		Order order; 
		
		// your trading algorithm ...
		
		order = new Order(0, 0, 0, 0, this);// default order which will not be sent to the market (because volume = 0)
		return order;
	}

}
