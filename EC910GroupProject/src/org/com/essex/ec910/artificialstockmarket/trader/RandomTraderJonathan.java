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

public class RandomTraderJonathan extends AbstractTrader {


	private Random rnd;
	
	/**
	 * Factor that determines how big the volume should be, if agent is gonna buy
	 * actually useless
	 */
	private double volFactorBuy = 1; 
	
	
	/**
	 * Factor that determines how big the volume should be, if agents is gonna sell buy
	 * actually useless
	 */
	private double volFactorSell = 1;
	
	/**
	 * Factor that determines how much the random price is aside from the last price
	 * as higher as much risk is the trader willing to take 
	 */
	private double riskFactor;
	/**
	 * Constructor
	 * @param name 
	 * @param artificialMarket  
	 * @param portfolio  
	 * @param max_buy
	 * @param max_sell
	 * 
	 */
	public RandomTraderJonathan(String name, ArtificialMarket artificialMarket, Portfolio portfolio, int max_buy,
			int max_sell,  double riskFactor) {

		
		super(name, artificialMarket, portfolio, max_buy, max_sell);
		rnd = new Random();

		this.riskFactor = riskFactor;

	}

	@Override
	public Order runStrategy(){

		Order order;   //random market order to be sent to market 
		int volume;    //random volume
		int price;
		int buyOrSell; //0= buy/ 1= sell
		int type2;
		int initialPrice = 30;

		Integer lastPrice =artificialMarket.getSpotPrice();

		int h;
		
		if(rnd.nextBoolean())
		{	
			//Buy Order
				buyOrSell = Order.BUY;
				double factor;
				
				volume = (int) (Sim.getRnd().getIntFromTo(1, this.max_buy)*volFactorBuy); 
				
				if(Math.random()>0.2)
				{
					factor = -1;
				}
				else
				{
					factor = 1;
				}
				
				h = (int) (factor*Math.abs((rnd.nextGaussian())*10*riskFactor));
				
				price =  lastPrice + h;
		}
		else
		{
				volume = (int) (Sim.getRnd().getIntFromTo(1, this.max_sell)*volFactorSell); 

				buyOrSell = Order.SELL;
				double factor;
				
				if(Math.random()>0.2)
				{
					factor = 1;
				}
				else
				{
					factor =  -1;
				}
				
				h = (int) (factor*Math.abs((rnd.nextGaussian())*10*riskFactor));
				
				price =  lastPrice + h;
		}
		
		if(Math.random()>0.05)
		{	type2 = Order.LIMIT;}
		else
		{	type2 = Order.MARKET;}
		
		
		order = new Order(buyOrSell, type2, volume, price, this);// default order which will not be sent to the market (because volume = 0)

		return order;
	}
	
	
	public static int randInt(int min, int max) {
	    return Sim.getRnd().getIntFromTo(min, max);
	}

}
