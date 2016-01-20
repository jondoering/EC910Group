package org.com.essex.ec910.artificialstockmarket.datasource;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *Object for wrapping a historical price inlcuding volume and date.
 *Usually used to wrap a life price from yahoo finance to provide it to the MarketMaker.
 *Price gets casted to an integer.
 * 
 * @author Jonathan Doering
 */
public class HistoricalPrice {

	private Date date;
	private int price;
	private int volume;
		
	
	/**
	 * 
	 * @param date - Date of the price in format "yyyy-MM-dd"
	 * @param price - price as double
	 * @param volume - volume as double
	 */
	public HistoricalPrice(String date, double price, double volume) {
		
		try {
				this.date = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
				e.printStackTrace();
		}
		
		this.price = (int) Math.round(price*100);
		this.volume = (int) volume;
	}


	public Date getDate() {
		return date;
	}


	public int getPrice() {
		return price;
	}


	public int getVolume() {
		return volume;
	}


	@Override
	public String toString() {
		return "HistoricalPrice [date=" + date + ", price=" + price + ", volume=" + volume + "]";
	}
	

}
