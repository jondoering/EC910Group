package org.com.essex.ec910.artificialstockmarket.jasmodels;

import jas.engine.Sim;
import jas.engine.SimEngine;
import jas.engine.SimModel;
import jas.engine.gui.JAS;
import java.util.ArrayList;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.LifeMarket;
import org.com.essex.ec910.artificialstockmarket.statistics.Statistics;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.MarketMakerJon;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;
import org.com.essex.ec910.artificialstockmarket.trader.RandomTraderJonathan;

/**
 * @author Pouyan
 *
 */
public class Model extends SimModel{
	
	private static Observer observer;
	private ArtificialMarket market;
	public ArrayList<RandomTraderJonathan> randomTraderList;    // list of random traders
	private LifeMarket lifeMarket;
	private MarketMakerJon marketMaker;
	private  Statistics statistics;
	
	public int numRandomTrader;                         // number of random traders
	public int initialMoney;                            // money of random traders at the beginning 
	public int initialShares;                           // shares of random traders at the beginning 
	int max_buy, max_sell;                              // trading limits for traders	

	public String toDate;
	public String tickerSymbol;
	public String fromDate;
	public int dragVolume;
	public double volumeFactor;
	
	public boolean printOrderBook; 
	public boolean printOrders;    
    
	private int stepsADay; //How many Steps are one day
	private double riskFactorAverse;
	private double riskFactorAffin;
	private double riskDistribution;
	
	
	
	
	@Override
	public void setParameters() {
		
		// set up default values for model parameters
		numRandomTrader = 200; //number of Traders in Model
		initialMoney = 10000; //10000$ for each trader
		initialShares=1000;   //1000 shares for each trader 
		
		max_buy = 1000;      //maximum of shares a trader can buy per order
		max_sell = 1000; 	//maximum of shares a trader can sell per order
		
		fromDate = "2014-02-01";  //First date of life data
		toDate = "2015-02-01";		//Last Date of life data
		tickerSymbol = "MSFT";		//Stock to get life data from (heere Microsoft)
		
		dragVolume = 10000; //Volume determine how much influence the Market Maker has to drag the artificial price to the life price
							  //if high, price follows the life price, if low price is more independend
		
		volumeFactor = 0.1; // How many of the current market volume should be placed in the market to create volume around the current price
						    // if low, price is more volatile and driven by randomness from trades
		
		stepsADay = 500; //How many Steps define a day (end of the day, Market Maker gets new price from life market) 
		
		printOrderBook = true;	// if true, order book is printed in each step in StdOut 
		printOrderBook = false; // if true, orders are printed in each step in StdOut
		
		riskFactorAverse = 1; //Factor that represent a riskaverse trader; those set prices closer around last price
		riskFactorAffin = 5; //Factor that represnet more agressive traders; those set prices in a bigger range about last price
		riskDistribution = 0.7; //determines how many of the traders are risk averse (here 70%)
		
		// put DatabaseConnector here ???????????????????? 
		
		//load statistics object 
	
		
		// open a probe to allow the user to modify default values
		Sim.openProbe(this, "Parameters model");
	}
	
	@Override
	public void buildModel() {
		
		
		//Initiate LifeMarket
		try {
			lifeMarket = new LifeMarket(tickerSymbol, fromDate, toDate);
		} catch (Exception e) {
						
			System.out.println("Couldn load life market Data");
			System.out.println(e.getMessage());
			
		}
		
		
		//Set up artificial market with first 10 values of life market
		int initValues =10; 
		int[] initPrices = new int[initValues];
		int[] initVolumes = new int[initValues];
		int c = 0;
		for(c=0;c<initValues;c++)
		{
			initPrices[c] = lifeMarket.getPrice(c).getPrice();
			initVolumes[c] = 10000; //lifeMarket.getPrice(c).getVolume();
 		}
		
		
		//Load artificial marker
		market = new ArtificialMarket( initPrices, initVolumes, printOrderBook, printOrders);
		statistics = new Statistics() ;
		statistics.setMarket(market);
		
		//load Market maker
		marketMaker = new MarketMakerJon("MarketMaker", lifeMarket, market, dragVolume,  volumeFactor, c);
		
		// creating random traders list
		randomTraderList = new ArrayList<RandomTraderJonathan>(); 
		
        // setup random traders		
		for(int i = 0; i < numRandomTrader; i++){
		
			double riskF = riskFactorAverse; // usually the agents is risk averse

			//Determine if affin or averse agent
			if(Sim.getRnd().getDblFromTo(0, 1) > riskDistribution)
			{
				//in X percent the trader is risk affine and gambles more
				riskF = riskFactorAffin;
			}
			randomTraderList.add(new RandomTraderJonathan("Random"+ i, this.market, 
					new Portfolio(this.initialMoney,this.initialShares), this.max_buy,
					this.max_sell, riskF));
		}
		
				
		scheduleEvents();
	}

	public void scheduleEvents() {
     
		//Add Events
		//Every Timestep
		eventList.scheduleCollection(0, 1, this.randomTraderList, getObjectClass("org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader"), "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.marketMaker, "runMarketMakerStrategy");
		eventList.scheduleSimple(0, 1, this.market, "clearMarket");
		eventList.scheduleSimple(0, 1, this.statistics, "updatePrice");
		
		//Just end of day
		eventList.scheduleSimple(0, stepsADay, this.marketMaker, "nextDay");
		
	}

	public static void main(String[] args)
	{

		//Load Sim Model
		SimEngine eng = new SimEngine();
		JAS jas = new JAS(eng);
		jas.setVisible(true);
		Model m = new Model();
		eng.addModel(m);
		m.setParameters();					
		
		//load observer
		observer = new Observer();
		eng.addModel(observer);
		observer.setParameters();
				
	}

	
	
	/**returns the ArtificialMarket Object (for Observer only)
	 * @return
	 */
	public ArtificialMarket getArtificialMarket()
	{
		return market;
	}

	/**returns the MarketMakerJon Object (for Observer only)
	 * @return
	 */
	public MarketMakerJon getMarketMaker() {
		return marketMaker;
	}

	/**returns the Statistics Object (for Observer only)
	 * @return
	 */
	public Statistics getStatistics() {
		// TODO Auto-generated method stub
		return statistics;
	}

	/**returns the stepsADay (for Observer only)
	 * @return
	 */	
	public int getStepsADay()
	{
		return stepsADay;
	}
}
