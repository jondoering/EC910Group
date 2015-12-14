package org.com.essex.ec910.artificialstockmarket.market;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.com.essex.ec910.artificialstockmarket.datasource.HistoricalPrice;
import org.com.essex.ec910.artificialstockmarket.datasource.YQLConnector;

/**
 * @author Jonathan
 *
 */
public class LifeMarket {

	private int lastPrice;
	
	private YQLConnector ydb;
	
	private SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	
	private ArrayList<HistoricalPrice> histPrices;
 
	
	/**
	 * @param db
	 */
	public LifeMarket(String yahoo_ticker, String from, String to) throws Exception
	{
		this.ydb =new YQLConnector(yahoo_ticker);
		
		//Calulate days between dates
		//int days = (int) TimeUnit.DAYS.convert(dformat.parse(to).getTime() - dformat.parse(from).getTime(), TimeUnit.MILLISECONDS);
		
		System.out.println("Load data from Yahoo Finance ...");
		fetchPricesFromTo(from, to);
		System.out.println("Loaded " + ydb.getAllPrices().size() + " data points.");
		
		
		histPrices = ydb.getAllPrices(); 
				
	}
	
	
	
	/**
	 *  fetchs prices from Yahoo Database in 250day steps
	 * @param from -start day as string
	 * @param to - end date as string
	 * @throws Exception
	 */
	private void fetchPricesFromTo(String from, String to) throws Exception
	{
		//Problem if between start and endDate is more then one year	
		//therefore fetch Data in 250days steps		
				int year_size = 250;
				
				
				int offset = 0;
					Calendar calendar = new GregorianCalendar();			
					
					
					boolean runFlag = true; 
					while(runFlag)
					{
					
					   
						calendar.setTime(dformat.parse(from));
						calendar.add(Calendar.DAY_OF_MONTH, offset);
						Date f = calendar.getTime();
						
						calendar.add(Calendar.DAY_OF_MONTH, year_size);
						Date t = calendar.getTime();
						
						if(t.after(dformat.parse(to)))
						{
							//stop loop if date is after then to
							t = dformat.parse(to);
							runFlag = false;					
						}
						
						
						ydb.addData(dformat.format(f), dformat.format(t));
						 
						offset += year_size;
					}			
	}
	

	/**
	 * Returns price n in historical price array
	 * @param n
	 * @return historical price
	 */
	public HistoricalPrice getPrice(int n)
	{
		return histPrices.get(n);
	}
	
	
}
