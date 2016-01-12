package org.com.essex.ec910.artificialstockmarket.trader;

import java.util.Random;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;

import jas.engine.Sim;
import jas.engine.Sim;

/**
 * Implementation of the random trader mainly used to 
 * stimulate the artificial market and bring volume into it. 
 * It is based on randomness defined by normal distribution. 
 * 
 * @author  Pouyan Dinarvand
 * @author Jonathan Doering
 *
 */
public class RandomTrader extends AbstractTrader {


	private Random rnd;
	
	/**
	 * Factor that determines how much the random price is aside from the last price
	 * as higher as much risk is the trader willing to take 
	 */
	private double riskFactor;
	/**
	 * Constructor
	 * @param name - Traders name
	 * @param artificialMarket - reference to market
	 * @param portfolio - initial portfolio
	 * @param max_buy - restrictions for buying shares
	 * @param max_sell- restrictions for selling shares
	 * @param riskFactor - determines if the risk affinity of the trader
	 */
	public RandomTrader(String name, ArtificialMarket artificialMarket, Portfolio portfolio, int max_buy,
			int max_sell,  double riskFactor) {

		
		super(name, artificialMarket, portfolio, max_buy, max_sell);
		rnd = new Random();

		this.riskFactor = riskFactor;

	}

	/* (non-Javadoc)
	 * @see org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader#runStrategy()
	 */
	@Override
	public Order runStrategy(){

		Order order;   //random market order to be sent to market 
		long volume;    //random volume
		int price;
		int buyOrSell; //0= buy/ 1= sell
		int type2;

		Integer lastPrice =artificialMarket.getSpotPrice();

		int h;
		
		if(rnd.nextBoolean())
		{	
			//Buy Order
				buyOrSell = Order.BUY;
				double factor;
				
				volume = (long) (Sim.getRnd().getLongFromTo(1, this.max_buy)); 
				
				//plus or minus the last price?
				if(Math.random()>0.5)
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
			//sell order
				volume = (long) (Sim.getRnd().getLongFromTo(1, this.max_sell)); 

				buyOrSell = Order.SELL;
				double factor;
				
				//plus or minus the last price?
				if(Math.random()>0.5)
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
	

}
