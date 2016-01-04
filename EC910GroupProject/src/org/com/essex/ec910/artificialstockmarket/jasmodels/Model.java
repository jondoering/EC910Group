package org.com.essex.ec910.artificialstockmarket.jasmodels;

import jas.engine.Sim;
import jas.engine.SimEngine;
import jas.engine.SimModel;
import jas.engine.gui.JAS;
import java.util.ArrayList;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.LifeMarket;
import org.com.essex.ec910.artificialstockmarket.statistics.Statistics;
import org.com.essex.ec910.artificialstockmarket.strategy.jonathan.BollingerBandTrader;
import org.com.essex.ec910.artificialstockmarket.strategy.jonathan.HighestPriceTraderTian;
import org.com.essex.ec910.artificialstockmarket.strategy.jonathan.PouyanTradingStrategy;
import org.com.essex.ec910.artificialstockmarket.strategy.jonathan.HighFrequenceSMATrader;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.MarketMaker;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;
import org.com.essex.ec910.artificialstockmarket.trader.RandomTrader;

/**
 * @author All
 *
 */
public class Model extends SimModel{


	/**
	 * Model?Observer 
	 */
	private static Observer observer;


	/**
	 *  Markets
	 */
	ArtificialMarket market;
	private LifeMarket lifeMarket;

	/**
	 * Agents
	 */
	MarketMaker marketMaker;
	ArrayList<RandomTrader> randomTraderList;    // list of random traders
	HighFrequenceSMATrader smaTrader;    // list of simple SMA traders
	BollingerBandTrader bbTrader; 
	HighestPriceTraderTian hpTrader;

	//Pouyan Strategy
	PouyanTradingStrategy pouyanTrader;           // trading model of pouyan
	double pouyanRisk;                      // eg: 0.01 value of portfolio is target profit or stop loss for pouyan trading strategy

	
	/**
	 * Periphery 
	 */
	Statistics statistics;


	/**
	 * Model Variables
	 */
	public int numRandomTrader;                         // number of random traders

	public int initialMoneyRandom;                      // money of random traders at the beginning
	public int initialSharesRandom;                     // shares of random traders at the beginning 
	int maxBuyRandom;
	int maxSellRandom;                           		  // trading limits for traders	

	public String toDate;
	public String tickerSymbol;
	public String fromDate;
	public int dragVolume;
	public double volumeFactor;

	public boolean printOrderBook; 
	public boolean printOrders;    

	int stepsADay; 					
	private double riskFactorAverse;
	private double riskFactorAffin;
	private double riskDistribution;


	private int initMoneyInteligent;
	private int initSharesInteligent;
	int maxBuyIntelligent;
	int maxSellIntelligent;                              // trading limits for traders	

	private double comFee;


	private int smaLongPeriod;
	private int smaShortPeriod;


	private int bollingerPeriod;
	private int bollingerUp;
	private int bollingerDown;




	@Override
	public void setParameters() {

		//Parameter for Model		
		stepsADay = 500; 						//How many Steps define a day (end of the day, Market Maker gets new price from life market) 		
		printOrderBook = false; 				// if true, orders gets printed in each step in StdOut


		//Parameter for life market data from yahoo finance
		fromDate = "2014-02-01";  				//First date of life data
		toDate = "2015-02-01";					//Last Date of life data
		tickerSymbol = "MSFT";					//ticker getting life data from (here Microsoft)

		//Parameter for Market Maker behaviour
		dragVolume = 10000;						//Volume determine how much influence the Market Maker has to drag the artificial price to the life price
		//if high, price follows the life price, if low price is more independend		
		volumeFactor = 0.1;						// How many of the current market volume should be placed in the market to create volume around the current price
		// if low, price is more volatile and driven by randomness from trades

		//Parameters for all Traders	
		maxBuyRandom = 1000;    				//maximum of shares a trader can buy per order
		maxSellRandom = 1000; 					//maximum of shares a trader can sell per order
		comFee = 0.005;							//Commission Fee of 0.5%

		// set up default values for random traders
		numRandomTrader = 200; 					//number of Traders in Model
		riskFactorAverse = 1; 					//Factor that represent a riskaverse trader; those set prices closer around last price
		riskFactorAffin = 5; 					//Factor that represnet more agressive traders; those set prices in a bigger range about last price
		riskDistribution = 0.7; 				//determines how many of the traders are risk averse (here 70%)
		initialSharesRandom = 1000;   			//1000 shares for each random trader
		initialMoneyRandom = 10000;			    //100000$ for each trader		

		// set up default values for intelligent traders
		initSharesInteligent = 0;				//each trader starts with no shares usually
		initMoneyInteligent = 1000000;			//but with some money for buying stocks
		maxBuyIntelligent = Integer.MAX_VALUE;	//No (practical) limitation for intelligent traders in number of buying 
		maxSellIntelligent = Integer.MAX_VALUE; //and selling

		//pouyan trading model parameter
		pouyanRisk = 0.2;                     // max risk for pouyan trading model

		//SMA trader variables
		smaLongPeriod = 300;
		smaShortPeriod = 200;
			
		//Bollinger Band trader variables
		bollingerPeriod = 50;
		bollingerUp = 15;
		bollingerDown = 10;
		
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

		/*
		 * Set up Markets
		 * 		
		 */
		//Load artificial marker
		market = new ArtificialMarket( initPrices, initVolumes, printOrderBook, printOrders);
		statistics = new Statistics() ;
		statistics.setMarket(market);

		//load Market maker
		marketMaker = new MarketMaker("MarketMaker", lifeMarket, market, dragVolume,  volumeFactor, c);


		/*
		 * Set Up Agents
		 * 
		 */
		// setup random traders	
		randomTraderList = new ArrayList<RandomTrader>(); 	
		for(int i = 0; i < numRandomTrader; i++){

			double riskF = riskFactorAverse; // usually the agents is risk averse

			//Determine if affin or averse agent
			if(Sim.getRnd().getDblFromTo(0, 1) > riskDistribution)
			{
				//in X percent the trader is risk affine and gambles more
				riskF = riskFactorAffin;
			}

			RandomTrader rt = new RandomTrader("Random"+ i, this.market, 
					new Portfolio(this.initialSharesRandom, this.initialMoneyRandom), this.maxBuyRandom,
					this.maxSellRandom, riskF);
			rt.setCommissionFee(comFee);

			randomTraderList.add(rt);
		}


		//set up intelligent trader
		this.smaTrader = new HighFrequenceSMATrader("SMA Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, smaLongPeriod, smaShortPeriod);
		this.smaTrader.setCommissionFee(comFee);

		this.bbTrader = new BollingerBandTrader("BB Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, bollingerPeriod, bollingerUp, bollingerDown);
		this.bbTrader.setCommissionFee(comFee);

		this.hpTrader = new HighestPriceTraderTian("HP Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent);
		this.hpTrader.setCommissionFee(comFee);
        
		//set up pouyan trading model
		this.pouyanTrader = new PouyanTradingStrategy("Pouyan Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, this.pouyanRisk);
		this.pouyanTrader.setCommissionFee(comFee);

		scheduleEvents();
	}

	public void scheduleEvents() {

		//Add Events
		//Every Timestep
		eventList.scheduleCollection(0, 1, this.randomTraderList, getObjectClass("org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader"), "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.smaTrader, "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.bbTrader, "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.hpTrader, "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.pouyanTrader, "sendFinalOrderToMarket");


		eventList.scheduleSimple(0, 1, this.marketMaker, "runMarketMakerStrategy");
		eventList.scheduleSimple(0, 1, this.market, "clearMarket");
		eventList.scheduleSimple(0, 1, this.statistics, "updatePrice");

		//Just end of day
		eventList.scheduleSimple(0, stepsADay, this.marketMaker, "nextDay");
		eventList.scheduleSimple(0, stepsADay, this.market, "saveClosePrice");
		

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



}
