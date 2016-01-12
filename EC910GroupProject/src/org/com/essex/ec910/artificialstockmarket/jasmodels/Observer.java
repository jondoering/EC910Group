package org.com.essex.ec910.artificialstockmarket.jasmodels;



import jas.engine.Sim;
import jas.engine.SimModel;
import jas.graphics.plot.IndividualBarPlotter;
import jas.graphics.plot.TimeSeriesPlotter;

/**
 * 
 * Graphical JAS observer class. 
 * Manages all of the graphical output. 
 * 
 * @author Pouyan Dinarvand
 *
 */

public class Observer extends SimModel {

	public Model model;
	
	private TimeSeriesPlotter pricePlot;
	private TimeSeriesPlotter volumePlot;
	private TimeSeriesPlotter kurtPlot;
	private TimeSeriesPlotter skewPlot;
	private TimeSeriesPlotter varPlot;
	private TimeSeriesPlotter traderValuePlot;
	private IndividualBarPlotter transCounterPlot;
	private IndividualBarPlotter profitLossPlot;
	private IndividualBarPlotter winningRatePlot;


	/* (non-Javadoc)
	 * @see jas.engine.SimModel#setParameters()
	 */
	@Override
	public void setParameters() {
		// TODO Auto-generated method stub

		model = (Model) Sim.engine.getModelWithID("org.com.essex.ec910.artificialstockmarket.jasmodels.Model");

		// open a probe to allow the user to modify default values
		Sim.openProbe(this, "Parameters observer");

	}


	/* (non-Javadoc)
	 * @see jas.engine.SimModel#buildModel()
	 */
	@Override
	public void buildModel() {

		//set up time series and plots
		pricePlot = new TimeSeriesPlotter("Price");
		volumePlot = new TimeSeriesPlotter("Volume");
		kurtPlot = new TimeSeriesPlotter("Kurtosis");
		skewPlot = new TimeSeriesPlotter("Skewness");
		varPlot = new TimeSeriesPlotter("Variance");
		traderValuePlot = new TimeSeriesPlotter("Strategy Portfolio Value");
		transCounterPlot = new IndividualBarPlotter("Transactions of Traders");
		profitLossPlot = new IndividualBarPlotter(" P & L");
		winningRatePlot = new IndividualBarPlotter("Winning Rates of Traders");
			
		pricePlot.addSeries("PriceSeries", model.market, "getSpotPrice", true);	
		pricePlot.addSeries("priceLife", model.marketMaker, "getLastLifePrice", true);		
		volumePlot.addSeries("Volume", model.market, "getSpotVolume", true);
		varPlot.addSeries("Variance", model.statistics, "getVariance", true);		
		kurtPlot.addSeries("Kurtosis", model.statistics, "getKurtosis", true);
		skewPlot.addSeries("Skewness", model.statistics, "getSkewness", true);
				
		//timeseries showing portfolio value of trader
		traderValuePlot.addSeries("Simple Moving Average", model.smaTrader, "getPortfolioValue", true);
		traderValuePlot.addSeries("Bollinger Band", model.bbTrader, "getPortfolioValue", true);
		traderValuePlot.addSeries("Highest Price", model.hpTrader, "getPortfolioValue", true);
		traderValuePlot.addSeries("Pouyan Trader", model.pouyanTrader, "getPortfolioValue", true);
		traderValuePlot.addSeries("william Trader", model.williamTrader, "getPortfolioValue", true);

		//Barplot showing transaction of trader
		transCounterPlot.addSource("Simple Moving Average", model.smaTrader, "getTransactionCount", true);		
		transCounterPlot.addSource("Bollinger Band", model.bbTrader, "getTransactionCount", true);
		transCounterPlot.addSource("Highest Price", model.hpTrader, "getTransactionCount", true);		
		transCounterPlot.addSource("Pouyan Trader", model.pouyanTrader, "getTransactionCount", true);
		transCounterPlot.addSource("william Trader", model.williamTrader, "getTransactionCount", true);
		
		//P&L
		profitLossPlot.addSource("Simple Moving Average", model.smaTrader, "getProfit_loss", true);		
		profitLossPlot.addSource("Bollinger Band", model.bbTrader, "getProfit_loss", true);
		profitLossPlot.addSource("Highest Price", model.hpTrader, "getProfit_loss", true);		
		profitLossPlot.addSource("Pouyan Trader", model.pouyanTrader, "getProfit_loss", true);
		profitLossPlot.addSource("william Trader", model.williamTrader, "getProfit_loss", true);
		
		
		//winningRatePlot
		winningRatePlot.addSource("Simple Moving Average", model.smaTrader, "getWinningRate", true);		
		winningRatePlot.addSource("Bollinger Band", model.bbTrader, "getWinningRate", true);
		winningRatePlot.addSource("Highest Price", model.hpTrader, "getWinningRate", true);		
		winningRatePlot.addSource("Pouyan Trader", model.pouyanTrader, "getWinningRate", true);
		winningRatePlot.addSource("william Trader", model.williamTrader, "getWinningRate", true);



		addSimWindow(pricePlot);
		addSimWindow(volumePlot);
		//addSimWindow(traderValuePlot);
		//addSimWindow(transCounterPlot);
		//addSimWindow(profitLossPlot);
		//addSimWindow(winningRatePlot);

		if(model.showStatistics)
		{
			addSimWindow(varPlot);
		addSimWindow(kurtPlot);
		addSimWindow(skewPlot);
		eventList.scheduleSimple(2, 1, varPlot, Sim.EVENT_UPDATE);
	    eventList.scheduleSimple(4, 1, skewPlot, Sim.EVENT_UPDATE);	
	    eventList.scheduleSimple(5, model.stepsADay, kurtPlot, Sim.EVENT_UPDATE);}
					
		eventList.scheduleSimple(0, 1, pricePlot, Sim.EVENT_UPDATE);		
		eventList.scheduleSimple(0, 1, transCounterPlot, Sim.EVENT_UPDATE);		
		eventList.scheduleSimple(0, 1, volumePlot, Sim.EVENT_UPDATE);		
		eventList.scheduleSimple(1,1,traderValuePlot, Sim.EVENT_UPDATE);
		eventList.scheduleSimple(1,1,profitLossPlot, Sim.EVENT_UPDATE);
		eventList.scheduleSimple(1,1,winningRatePlot, Sim.EVENT_UPDATE);


	}

	

}

