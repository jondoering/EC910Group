package org.com.essex.ec910.artificialstockmarket.trader;

import java.util.Random;

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
		int price;
		int buyOrSell; //0= buy/ 1= sell
		int type2;

		if(Math.random()>0.5)
		{	buyOrSell = Order.BUY;}
		else
		{	buyOrSell = Order.SELL;}
		
		if(Math.random()>0.5)
		{	type2 = Order.LIMIT;}
		else
		{	type2 = Order.MARKET;}
		 
		
		volume = Sim.getRnd().getIntFromTo(1, this.max_buy);  // volume of trading is limited to 10 ?????????
		price = Sim.getRnd().getIntFromTo(10, 50);
		
		order = new Order(buyOrSell, type2, volume, price, this);// default order which will not be sent to the market (because volume = 0)

		return order;
	}
	
	public static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	//// order = new Order(Order.SEll, 0, 0, 0, this);
}
