package org.com.essex.ec910.artificialstockmarket.jasmodels;

import jas.engine.Sim;
import jas.engine.SimModel;
import jas.graphics.plot.TimeSeriesPlotter;

/**
 * @author Pouyan
 *
 */

public class Observer extends SimModel {

	public Model model;
	private TimeSeriesPlotter pricePlot;
	private TimeSeriesPlotter volumePlot;

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
		// TODO Auto-generated method stub
		pricePlot = new TimeSeriesPlotter("Price");
		volumePlot = new TimeSeriesPlotter("Volume");
		
		pricePlot.addSeries("priceArtificial", model.getArtificialMarket(), "getSpotPrice", true);
		pricePlot.addSeries("priceLife", model.getMarketMaker(), "getLastLifePrice", true);

		volumePlot.addSeries("Volume", model.getArtificialMarket(), "getSpotVolume", true);
		
		addSimWindow(pricePlot);
		addSimWindow(volumePlot);
		
//		ordersPlot = new CollectionBarPlotter("Orders");
//		ordersPlot.addSeries("orders", model.market.getParticipants(), getObjectClass("RandomAgent"), "getOrder", true);
//		addSimWindow(ordersPlot);
		
		
		
		eventList.scheduleSimple(0, 1, pricePlot, Sim.EVENT_UPDATE);
		eventList.scheduleSimple(0, 1, volumePlot, Sim.EVENT_UPDATE);

		//		eventList.scheduleSimple(0, 1, ordersPlot, Sim.EVENT_UPDATE);

	}


}

