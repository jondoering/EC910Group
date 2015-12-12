package org.com.essex.ec910.artificialstockmarket.trader;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;

import jas.engine.Sim;
import jas.engine.Sim;

/**
 * @author Pouyan
 *
 */
// Random trader sends random order buy/sell to market and exits randomly 
// Random trader is a source of liquidity for market

public class RandomTrader extends AbstractTrader {


	/**
	 * Constructor
	 * @param name 
	 * @param artificialMarket  
	 * @param portfolio  
	 * @param max_buy
	 * @param max_sell
	 * 
	 */
	public RandomTrader(String name, ArtificialMarket artificialMarket, Portfolio portfolio, int max_buy,
			int max_sell) {

		super(name, artificialMarket, portfolio, max_buy, max_sell);

	}

	@Override
	public Order runStrategy(){

		Order order;   //random market order to be sent to market 
		int volume;    //random volume
		int buyOrSell; //0= buy/ 1= sell  

		buyOrSell = Sim.getRnd().getIntFromTo(0, 1);//0=buy, 1=sell
		volume = Sim.getRnd().getIntFromTo(1, this.max_buy);  // volume of trading is limited to 10 ?????????
		order = new Order(buyOrSell,Order.MARKET, volume, 0, this);// default order which will not be sent to the market (because volume = 0)

		return order;
	}

	//// order = new Order(Order.SEll, 0, 0, 0, this);
}
