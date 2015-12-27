package org.com.essex.ec910.artificialstockmarket.jasmodels;

import java.text.SimpleDateFormat;
import java.util.Date;

import jas.engine.Sim;
import jas.engine.SimModel;
import jas.graphics.plot.TimeSeriesPlotter;
import jas.stats.TimeSeries;

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
		
		pricePlot.addSeries("PriceSeries", model.getArtificialMarket(), "getSpotPrice", true);	
		pricePlot.addSeries("priceLife", model.getMarketMaker(), "getLastLifePrice", true);		
		volumePlot.addSeries("Volume", model.getArtificialMarket(), "getSpotVolume", true);
		varPlot.addSeries("Variance", model.getStatistics(), "getVariance", true);		
		kurtPlot.addSeries("Kurtosis", model.getStatistics(), "getKurtosis", true);
		skewPlot.addSeries("Skewness", model.getStatistics(), "getSkewness", true);
				
		addSimWindow(pricePlot);
		addSimWindow(volumePlot);
		addSimWindow(varPlot);
		addSimWindow(kurtPlot);
		addSimWindow(skewPlot);
		
		
		
		eventList.scheduleSimple(0, 1, pricePlot, Sim.EVENT_UPDATE);		
		eventList.scheduleSimple(0, 1, volumePlot, Sim.EVENT_UPDATE);		
		eventList.scheduleSimple(2, 1, varPlot, Sim.EVENT_UPDATE);
		eventList.scheduleSimple(4, 1, skewPlot, Sim.EVENT_UPDATE);
		
		eventList.scheduleSimple(5, model.getStepsADay(), kurtPlot, Sim.EVENT_UPDATE);

	}

	

}

