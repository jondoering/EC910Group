package org.com.essex.ec910.artificialstockmarket.jasmodels;



import jas.engine.Sim;
import jas.engine.SimModel;
import jas.graphics.plot.CollectionBarPlotter;
import jas.graphics.plot.IndividualBarPlotter;
import jas.graphics.plot.TimeSeriesPlotter;

/**
 * @author Pouyan
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

	//	private CollectionBarPlotter ordersPlot;


	@Override
	public void setParameters() {
		// TODO Auto-generated method stub

		model = (Model) Sim.engine.getModelWithID("org.com.essex.ec910.artificialstockmarket.jasmodels.Model");

		// open a probe to allow the user to modify default values
		Sim.openProbe(this, "Parameters observer");

	}


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

		//Barplot showing transaction of trader
		transCounterPlot.addSource("Simple Moving Average", model.smaTrader, "getTransactionCount", true);		
		transCounterPlot.addSource("Bollinger Band", model.bbTrader, "getTransactionCount", true);
		transCounterPlot.addSource("Highest Price", model.hpTrader, "getTransactionCount", true);		
		transCounterPlot.addSource("Pouyan Trader", model.pouyanTrader, "getTransactionCount", true);
		
		
		addSimWindow(pricePlot);
		addSimWindow(volumePlot);
		addSimWindow(varPlot);
		addSimWindow(kurtPlot);
		addSimWindow(skewPlot);
		addSimWindow(traderValuePlot);
		addSimWindow(transCounterPlot);
		
		
		eventList.scheduleSimple(0, 1, pricePlot, Sim.EVENT_UPDATE);		
		eventList.scheduleSimple(0, 1, transCounterPlot, Sim.EVENT_UPDATE);		
		eventList.scheduleSimple(0, 1, volumePlot, Sim.EVENT_UPDATE);		
		//eventList.scheduleSimple(2, 1, varPlot, Sim.EVENT_UPDATE);
		//eventList.scheduleSimple(4, 1, skewPlot, Sim.EVENT_UPDATE);	
		//eventList.scheduleSimple(5, model.stepsADay, kurtPlot, Sim.EVENT_UPDATE);
		eventList.scheduleSimple(0,1,traderValuePlot, Sim.EVENT_UPDATE);

	}

	

}

