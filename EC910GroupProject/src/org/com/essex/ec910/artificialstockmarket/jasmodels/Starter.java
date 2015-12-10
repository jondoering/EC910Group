package org.com.essex.ec910.artificialstockmarket.jasmodels;

import org.com.essex.ec910.artificialstockmarket.datasource.YQLConnector;
import jas.engine.Sim;
import jas.engine.SimEngine;
import jas.engine.SimModel;
import jas.engine.gui.JAS;

public class Starter extends SimModel {

@Override
	public void setParameters() {
		// TODO Auto-generated method stub
		
		// open a probe to allow the user to modify default values
		Sim.openProbe(this, "Parameters model");
	}
	
	
	@Override
	public void buildModel() {
		// TODO Auto-generated method stub

		
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
