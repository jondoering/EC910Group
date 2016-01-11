/**
 * 
 */
package org.com.essex.ec910.artificialstockmarket.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;

/**
 * Statistics calculates and provides main statistical moments of stock prices.
 * This includes mu and standard deviation (of Gausian normal distribution), skewness and kurtosis.
 * Statistical methdos coming form cern.colt.list @see https://dst.lbl.gov/ACSSoftware/colt/api/cern/colt/list/package-use.html
 *   
 * @author Jonathan Doering
 *
 */
public class Statistics {
	
	private ArrayList<Integer> prices;
	private ArrayList<Integer> volume;

	private ArrayList<Double> returns;
	private ArtificialMarket market;
	private String filename;
	private PrintStream ps;
	//private	int obv = 0;
	
	public Statistics(String runningPath)
	{
		filename = runningPath +"dataSeries_" + new SimpleDateFormat("ddMMyy_hh_mm_ss").format(new Date()) + ".csv";
		
		openStream();
		
		prices = new ArrayList<Integer>();
		volume = new ArrayList<Integer>();
		returns = new ArrayList<Double>();
	}
	
	/**
	 * Finalizer closes output stream
	 */
	public void finalize()
	{
		ps.close();
	}
	
	/**
	 * sets artificial stock market object
	 * @param market
	 */
	public void setMarket(ArtificialMarket market)
	{
		this.market = market;
	}
	
	/**
	 * adds last market spot price and volume to statistics object
	 */
	public void updatePrice()
	{
		this.addPrice(market.getSpotPrice());
		volume.add(market.getSpotVolume());
		
		this.writeLine();
	}

	/**
	 * Adds a price to the time series and calculates the new return
	 * @param price - new integer price
	 */
	private void addPrice(final int price)
	{
		if(prices.isEmpty())
		{	returns.add(0.0);}
		else
		{	returns.add(calcReturn(prices.get(prices.size()-1), price));}
		
		prices.add((Integer) price);
		
	}
	
		
	/**
	 * Calculates a return between two prices
	 * @param p1 - price 1
	 * @param p2 - price 2
	 * @return return of price 1 and price 2
	 */
	private Double calcReturn(final int p1, final int p2)
	{
		double a = (double) p1;
		double b = (double) p2;
		
		return ((b-a)/b);
	}
	
	/**
	 * @return - mu of current price curve
	 */
	public double getMu()
	{
		DoubleArrayList d = new DoubleArrayList();
		d.addAllOf(returns);
		
		return Descriptive.mean(d);
	}
	
	/**
	 * calculates variance of returns vector by standard variance formular sum((returns(i)-average(returns)^2)/(size(returns)-1)
	 * @return 
	 * @return variance
	 */
	public double getVariance()
	{
		
		double avg = returns.stream().mapToDouble(i -> i).average().getAsDouble();				
		return	returns.stream().mapToDouble(i -> Math.pow((i-avg), 2)).sum() /(returns.size()-1);		
	}
	
	/**
	 * @return standard deviation as sqrt of variance
	 */
	public double getStddev()
	{
		return Math.sqrt(getVariance());
	}
	
	/**
	 * Calculates skewness of returns vector as sample skewness of all elements of vector
	 * @return skewness of returns
	 */
	public double getSkewness()
	{
		DoubleArrayList d = new DoubleArrayList();
		d.addAllOf(returns);		
		
		return Descriptive.sampleSkew(d, Descriptive.mean(d), this.getVariance());	
	}
	
	
	/**
	 * Calculates kurtosis of returns vector as sample kurtosis of all elements of vector
	 * @return kurtosis of returns
	 */
	public double getKurtosis()
	{
		DoubleArrayList d = new DoubleArrayList();
		d.addAllOf(returns);
		
		return Descriptive.sampleKurtosis(d, Descriptive.mean(d), this.getVariance());
	}

	
	/**
	 *Oopens Stream for file writing in current directory; filthy solution due to the fact, that stream never gets closed;
	 *but it is the only way, because of a really weird behavior of JAS using modelEnd() method.
	 */
	private void openStream() 
	{
		
		ps = null;
		FileOutputStream fop;
		try {
			fop = new FileOutputStream(new File(filename));
			ps = new PrintStream(fop);			
			ps.println	("time, price, volume");		
			ps.flush();
			
			System.out.println("Writing output to " + filename);
			
		} catch (FileNotFoundException e) {
			System.out.println("Failure to load output stream  - no output will be written.");
		}		
		
		
	}
	
	/**
	 * writes a line in statistical output file in current running directory
	 */
	private void writeLine()
	{
		if(ps != null)
		{
			int i = prices.size()-1;
			
			StringBuilder sb = new StringBuilder();
			sb.append(i);
			sb.append(", ");
			sb.append(prices.get(i));
			sb.append(", ");
			sb.append(volume.get(i));	
			
			ps.println(sb.toString());
			ps.flush();
		}
	}
}
