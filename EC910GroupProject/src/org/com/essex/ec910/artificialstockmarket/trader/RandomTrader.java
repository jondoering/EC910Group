package org.com.essex.ec910.artificialstockmarket.trader;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;

import jas.engine.Sim;


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

		if( Sim.getRnd().getIntFromTo(0, 1) == 0 ){ // 0 = open a position (buy or sell)

			buyOrSell = Sim.getRnd().getIntFromTo(0, 1);//0=buy, 1=sell

			if(buyOrSell == 0){// --> buy
				volume = Sim.getRnd().getIntFromTo(1, this.max_buy);  // volume of trading is limited to 10 ?????????
				order = new Order(buyOrSell, 2, volume, 0, this);
			}
			else{
				volume = Sim.getRnd().getIntFromTo(1, this.max_sell);  // volume of trading is limited to 10 ?????????
				order = new Order(buyOrSell, 2, volume, 0, this);// default order which will not be sent to the market (because volume = 0)
			}  
		}
		else{
			order = new Order(0, 0, 0, 0, this);// do nothing --> default order which will not be sent to the market (because volume = 0) 
		}
		return order;
	}

}
