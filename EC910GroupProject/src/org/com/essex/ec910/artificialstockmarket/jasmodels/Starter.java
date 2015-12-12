package org.com.essex.ec910.artificialstockmarket.jasmodels;

import java.util.ArrayList;

import org.com.essex.ec910.artificialstockmarket.datasource.YQLConnector;
import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;

import jas.engine.Sim;
import jas.engine.SimEngine;
import jas.engine.SimModel;
import jas.engine.gui.JAS;

public class Starter extends SimModel {


public int numberOfTrader;
	public double initialPrice;
	public ArtificialMarket market, market2;
	private ArrayList<AbstractTrader> AT_list;
	private ArrayList<ArtificialMarket> AM_list;
	
	@Override
	public void setParameters() {
		// TODO Auto-generated method stub
		// set up default values for model parameters
				numberOfTrader = 20;
				initialPrice = 50;
		// open a probe to allow the user to modify default values
		Sim.openProbe(this, "Parameters model");
	}
	
	
	@Override
	public void buildModel() {
		// TODO Auto-generated method stub
		AM_list = new ArrayList<ArtificialMarket>();
		
		
		
		scheduleEvents();
	}

public void scheduleEvents() {

}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SimEngine eng = new SimEngine();
		JAS jas = new JAS(eng);
		jas.setVisible(true);
		
		Starter m = new Starter();
		eng.addModel(m);
		m.setParameters();
	
		System.out.println("CE910 Group Project - Implementing a artificial stock market \n\nProject Empty");
		
		
	}

}
