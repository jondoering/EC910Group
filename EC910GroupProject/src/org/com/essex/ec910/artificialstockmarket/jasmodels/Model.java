package org.com.essex.ec910.artificialstockmarket.jasmodels;

import jas.engine.Sim;
import jas.engine.SimEngine;
import jas.engine.SimModel;
import jas.engine.gui.JAS;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.LifeMarket;
import org.com.essex.ec910.artificialstockmarket.statistics.Statistics;
import org.com.essex.ec910.artificialstockmarket.strategy.BollingerBandTrader;
import org.com.essex.ec910.artificialstockmarket.strategy.HighFrequenceSMATrader;
import org.com.essex.ec910.artificialstockmarket.strategy.MNHighestPriceTraderTian;
import org.com.essex.ec910.artificialstockmarket.strategy.PouyanTradingStrategy;
import org.com.essex.ec910.artificialstockmarket.strategy.WilliamTradingStrategy;
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
	MNHighestPriceTraderTian hpTrader;
    WilliamTradingStrategy williamTrader;
	PouyanTradingStrategy pouyanTrader;           // trading model of pouyan

	
	/**
	 * Periphery 
	 */
	Statistics statistics;


	/**
	 * Model Variables
	 * for a detailed description look at method setParameters;
	 */
	public int numRandomTrader;                         

	public int initMoneyRandom;                      
	public int initSharesRandom;                      
	int maxBuyRandom;
	int maxSellRandom;                           		 	

	public String toDate;
	public String tickerSymbol;
	public String fromDate;
	public int dragVolume;
	public double volumeFactor;

	public boolean printOrderBook; 
	public boolean printOrders; 
	public boolean showStatistics;

	int stepsADay; 					
	private double riskFactorAverse;
	private double riskFactorAffin;
	private double riskDistribution;


	private int initMoneyInteligent;
	private int initSharesInteligent;
	int maxBuyIntelligent;
	int maxSellIntelligent;                             
	
	private double comFee;

	private long pouyanVolume;                           
	private long pouyanTargetProfit;                  
	private long pouyanStopLoss;                         

	private int smaLongPeriod;
	private int smaShortPeriod;


	private int bollingerPeriod;
	private double bollingerUp;
	private double bollingerLow;


	private int tianM;
	private int tianN;


	private int williamPeriod;
	private double williamPercentage;




	@Override
	public void setParameters() {

		//Parameter for Model		
		stepsADay = 500; 						//How many Steps define a day (end of the day, Market Maker gets new price from life market) 		
		printOrderBook = false; 				// if true, ordthe whole order book (in a cumulated way) gets gets printed in each step in StdOut
		printOrders    = false;					//if true,  orders gets printed in each step in StdOut
		showStatistics = false;					//if true, extra market statistic gets plotted in JAS
		
			

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
		comFee = 0.005;							//Commission Fee of 0.5%

		// set up default values for random traders
		numRandomTrader = 200; 					//number of Traders in Model
		riskFactorAverse = 1; 					//Factor that represent a riskaverse trader; those set prices closer around last price
		riskFactorAffin = 5; 					//Factor that represnet more agressive traders; those set prices in a bigger range about last price
		riskDistribution = 0.7; 				//determines how many of the traders are risk averse (here 70%)
		initSharesRandom = 1000;   				//1000 shares for each random trader
		initMoneyRandom = 10000;			    //100000$ for each trader		
		maxBuyRandom = 1000;    				//maximum of shares a trader can buy per order
		maxSellRandom = 1000; 					//maximum of shares a trader can sell per order
	
		// set up default values for intelligent traders
		initSharesInteligent = 0;				//each trader starts with no shares usually
		initMoneyInteligent = 1000000;			//but with some money for buying stocks
		maxBuyIntelligent = Integer.MAX_VALUE;	//No (practical) limitation for intelligent traders in number of buying 
		maxSellIntelligent = Integer.MAX_VALUE; //and selling

		//pouyan trading model parameter
		pouyanVolume = 100;                     // volume (size) of trading
		pouyanTargetProfit = 100;               // target profit for pouyan trading strategy
		pouyanStopLoss = 100; 					// stop loss for pouyan trading strategy

		//SMA trader variables
		smaLongPeriod = 200;					//sma long period
		smaShortPeriod = 15;					//sma short period
			
		//Bollinger Band trader variables
		bollingerPeriod = 20;					//bollinger band period
		bollingerUp = 2;						//upper bollinger band multiple
		bollingerLow = 2;						//lower bollinger band multiple
		
		//Tian M-N high price strategy
		tianM = 10;								//number of days indicates a buy
		tianN = 2;								//number od days indicates a sell
			
		//William
		williamPeriod = 200;				    //period for counter strategy
		williamPercentage = 0.02;				//percentage for counter strategy 
		
		
		// open a probe to allow the user to modify default values
		Sim.openProbe(this, "Parameters model");
	}

	/* (non-Javadoc)
	 * @see jas.engine.SimModel#buildModel()
	 */
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
		
		//load statistic class; set path of running locatio as output path
		String path = Model.class.getProtectionDomain().getCodeSource().getLocation().getPath()
					.replace(new File(Model.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName(), "");
				
		String decodedPath = "";
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Failure to load output path  - no output will be written.");
		}

		statistics = new Statistics(decodedPath);
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
					new Portfolio(this.initSharesRandom, this.initMoneyRandom), this.maxBuyRandom,
					this.maxSellRandom, riskF);
			rt.setCommissionFee(comFee);

			randomTraderList.add(rt);
		}


		//set up intelligent trader
		this.smaTrader = new HighFrequenceSMATrader("SMA Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, smaLongPeriod, smaShortPeriod);
		this.smaTrader.setCommissionFee(comFee);

		this.bbTrader = new BollingerBandTrader("BB Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, bollingerPeriod, bollingerUp, bollingerLow);
		this.bbTrader.setCommissionFee(comFee);

		this.hpTrader = new MNHighestPriceTraderTian("HP Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, this.tianM, this.tianN);
		this.hpTrader.setCommissionFee(comFee);
        
		//set up pouyan trading model
		this.pouyanTrader = new PouyanTradingStrategy("Pouyan Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, this.pouyanVolume, this.pouyanTargetProfit,this.pouyanStopLoss);
		this.pouyanTrader.setCommissionFee(comFee);
         
		this.williamTrader = new WilliamTradingStrategy("William Trader", this.market, new Portfolio(this.initSharesInteligent,this.initMoneyInteligent), this.maxBuyIntelligent, this.maxSellIntelligent, this.williamPercentage, this.williamPeriod);
		this.williamTrader.setCommissionFee(comFee);
		
		
		scheduleEvents();
	}

	/**
	 * schedule all events for JAS model
	 */
	public void scheduleEvents() {

		//Add Events
		//Every Timestep
		eventList.scheduleCollection(0, 1, this.randomTraderList, getObjectClass("org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader"), "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.smaTrader, "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.bbTrader, "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.hpTrader, "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.pouyanTrader, "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.williamTrader, "sendFinalOrderToMarket");


		eventList.scheduleSimple(0, 1, this.marketMaker, "runMarketMakerStrategy");
		eventList.scheduleSimple(0, 1, this.market, "clearMarket");
		eventList.scheduleSimple(0, 1, this.statistics, "updatePrice");

		//Just end of day
		eventList.scheduleSimple(0, stepsADay, this.marketMaker, "nextDay");
		eventList.scheduleSimple(0, stepsADay, this.market, "saveClosePrice");
		

	}
	
	
	/**
	 * Starts whole model
	 * @param args
	 */
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
