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
	private double volFactorBuy;
	private double volFactorSell;
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
			int max_sell, double volFactorBuy, double volFactorSell) {

		
		super(name, artificialMarket, portfolio, max_buy, max_sell);
		rnd = new Random();
		this.volFactorBuy = volFactorBuy;
		this.volFactorSell = volFactorSell;

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
				
				h = (int) (factor*Math.abs((rnd.nextGaussian()*10)));
				if(lastPrice == -1)
				{	price =  initialPrice + h;}
				else
				{	price =  lastPrice + h;}
		}
		else
		{
				volume = Sim.getRnd().getIntFromTo(1, this.max_buy); 

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
				
				h = (int) (factor*Math.abs((Math.round(rnd.nextGaussian()*10))*volFactorSell));
				if(lastPrice == -1)
				{	price =  initialPrice + h;}
				else
				{	price =  lastPrice + h;}
		}
		
		if(Math.random()>0.05)
		{	type2 = Order.LIMIT;}
		else
		{	type2 = Order.MARKET;}
		 
		
		
		
		
		
		
		
		
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
