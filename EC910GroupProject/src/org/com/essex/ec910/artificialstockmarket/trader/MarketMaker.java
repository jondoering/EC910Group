package org.com.essex.ec910.artificialstockmarket.trader;

import org.com.essex.ec910.artificialstockmarket.jasmodels.Observer;
import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.LifeMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;

import jas.engine.Sim;

/**
 * @author Jonathan
 *
 */
/**
 * @author Jonathan
 *
 */
/**
 * @author Jonathan
 *
 */
public class MarketMaker extends AbstractTrader{

	
	/**
	 * Number of minutes defining a morning period
	 */
	private static int MORNINGTIME = 50; 

	
	private int minuteCounter;
	private int lastLifePrice;
	private int lastLifePriceCounter;
		
	private LifeMarket lifeMarket;

	/**
	 * probability that a life price order in morning period gets placed
	 */
	private double lifeOrderProbability;
		
	/**
	 * number of shares the market maker uses in its drag orders (based on life prices)   
	 */
	private double dragVolume;

	/**
	 * Factor that defines, how much of the last executed volume the market maker uses in his artificial market order
	 */
	private double artVolumeFactor;
	
	
	
	public MarketMaker(String name, LifeMarket lifeMarket, ArtificialMarket artificialMarket, int dragVolume, double volumeFactor, int lastLifePriceCounter)
	{
		super(name, artificialMarket, new Portfolio(0,0), 0, 0);
				
		this.lifeMarket = lifeMarket;
		this.dragVolume = dragVolume;
		this.artVolumeFactor = volumeFactor;
		this.lastLifePriceCounter = lastLifePriceCounter;
	
		
		if(lifeMarket.testModus())
		{	this.lifeOrderProbability = 0.7; }
		else
		{
			this.lifeOrderProbability = 0.7;
		}
		
	}

	
	/**
	 * 
	 */
	public void runMarketMakerStrategy(){
	
		minuteCounter++;
		
		if(minuteCounter < MORNINGTIME)
		{
			//still in morning period
			//Set Orders around the new life price with Probabilty X
			if(trueWithProbability(this.lifeOrderProbability))
			{				
				//Place orders around last life price 
				placesLifePriceOrders();
			}
			else
			{
				//places orders around last real price
				placesArtficialPriceOrders();
			}
			
			
		}
		else
		{
			//after the morning period
			//places orders around current real price
			placesArtficialPriceOrders();
			
		}
				
	}
	
	/**
	 * Places orders based on life prices by specific algorithm
	 */
	private void placesLifePriceOrders()
	{
		//find two random prices around the life price with margin 
		double margin = 0.001;
		int buyPrice = (int)  (lastLifePrice * (1+ Sim.getRnd().getDblFromTo(0, margin)));
		int sellPrice = (int)  (lastLifePrice * (1 - Sim.getRnd().getDblFromTo(0, margin)));
		
		//find two random volumes around a specific value
		double marginVol = 0.10;
		int buyVolume = (int)  (dragVolume * (1+ Sim.getRnd().getDblFromTo(-marginVol, marginVol)));
		int sellVolume = (int)  (dragVolume * (1 + Sim.getRnd().getDblFromTo(-marginVol, marginVol)));
	
		//places Orders
		artificialMarket.reciveOrder(new Order(Order.BUY, Order.LIMIT, buyVolume, buyPrice, this));
		artificialMarket.reciveOrder(new Order(Order.SELL, Order.LIMIT, sellVolume, sellPrice, this));
	}	
	
	
	/**
	 * places orders based on artificial prices by specific algorithm
	 */
	private void placesArtficialPriceOrders()
	{
		int lastArtificialPrice = artificialMarket.getSpotPrice();
		//find two random prices around the life price with margin close to zero 
				double margin = 0.0001;
				
				int buyPrice = (int)  (lastArtificialPrice); //(1+ Sim.getRnd().getDblFromTo(0, margin)));
				int sellPrice = (int)  (lastArtificialPrice); //(1 - Sim.getRnd().getDblFromTo(0, margin)));
				
				//find two random volumes around a specific volume
				double marginVol = 0.10;
				int artificialVolume = (int) (artificialMarket.getSpotVolume() * artVolumeFactor);
				int buyVolume = (int)  (artificialVolume * (1+ Sim.getRnd().getDblFromTo(-marginVol, marginVol)));
				int sellVolume = (int)  (artificialVolume * (1 + Sim.getRnd().getDblFromTo(-marginVol, marginVol)));
			
				//places Orders
				artificialMarket.reciveOrder(new Order(Order.BUY, Order.LIMIT, buyVolume, buyPrice, this));
				artificialMarket.reciveOrder(new Order(Order.SELL, Order.LIMIT, sellVolume, sellPrice, this));
	}
	
	
	
	/**
	 * For observer only
	 * @return
	 */
	public int getLastLifePrice()
	{
		return lastLifePrice;
	}
	
	/**
	 *  updates life price and resets morning period counter 
	 */
	public void nextDay()
	{
		lastLifePrice = lifeMarket.getPrice(lastLifePriceCounter).getPrice();
		lastLifePriceCounter++;
		minuteCounter = 0;
	}
	
	/**
	 * returns true with probability x (including)
	 * @param d as a probability value 
	 * @return boolean
	 */
	public boolean trueWithProbability(double d)
	{

		if(Sim.getRnd().getDblFromTo(0, 1) >= d)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
}
