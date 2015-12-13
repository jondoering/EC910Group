package org.com.essex.ec910.artificialstockmarket.jasmodels;

import jas.engine.Sim;
import jas.engine.SimEngine;
import jas.engine.SimModel;
import jas.engine.gui.JAS;
import java.util.ArrayList;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;
import org.com.essex.ec910.artificialstockmarket.trader.RandomTrader;

/**
 * @author Pouyan
 *
 */
public class Model extends SimModel{
	
	public int numRandomTrader;                         // number of random traders
	public int initialMoney;                            // money of random traders at the beginning 
	public int initialShares;                           // shares of random traders at the beginning 
	int max_buy, max_sell;                              // trading limits for traders 

	//	simple random market for backtesting model and observer
	//public RandomMarket randomMarket;                  
	public  int initialPrice;
	public double landa;
	
	private ArtificialMarket market;
	public ArrayList<RandomTrader> randomTraderList;    // list of random traders
    
	
	@Override
	public void setParameters() {
		
		// set up default values for model parameters
		numRandomTrader = 100;
		initialMoney = 10000; //10000$ for each trader
		initialShares=1000;   //1000 shares for each trader 
		max_buy = 100;        
		max_sell = 100;
		
		// put DatabaseConnector here ???????????????????? 
		
		// open a probe to allow the user to modify default values
		Sim.openProbe(this, "Parameters model");
	}
	
	@Override
	public void buildModel() {
		
         market = new ArtificialMarket(null);// creating AM
		randomTraderList = new ArrayList<RandomTrader>();// creating random traders list 
		
        // setup random traders		
		for(int i = 0; i < numRandomTrader; i++){
			randomTraderList.add(new RandomTrader("R"+ i, this.market, 
					new Portfolio(this.initialMoney,this.initialShares), this.max_buy,
					this.max_sell));
		}
		
		scheduleEvents();
	}

	public void scheduleEvents() {
     
		//Add Events
		eventList.scheduleCollection(0, 1, this.randomTraderList, getObjectClass("org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader"), "sendFinalOrderToMarket");
		eventList.scheduleSimple(0, 1, this.market, "clearMarket");


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
		
		
		Observer o = new Observer();
		eng.addModel(o);
		o.setParameters();

	}

	public ArtificialMarket getMarket()
	{
		return market;
	}

}
