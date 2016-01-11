/**
 * 
 */
package org.com.essex.ec910.artificialstockmarket.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
import org.com.essex.ec910.artificialstockmarket.market.Order;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * simple moving average strategy compare  
 * without explicit money management
 * @author Jonathan
 *
 */
public class BollingerBandTrader extends AbstractTrader {

	
	private int period;
	private double nbdevup;
	private double nbdevdn;
	private double closePerc;
	
	/**
	 * Ta Lib Core Object
	 */
	private Core talib;
	
	public BollingerBandTrader(final String name,final ArtificialMarket artificialMarket, final Portfolio portfolio, final int max_buy,
			final int max_sell,final int period, final double nbdevup, final double nbdevdn) {
		super(name, artificialMarket, portfolio, max_buy, max_sell);		
		
		this.period = period;
		this.nbdevup = nbdevup;
		this.nbdevdn = nbdevdn;
		this.closePerc = 0.002;
		
		talib = new Core();
	}

	
	/* 
	 * Simple SMA Strategy
	 * (non-Javadoc)
	 * @see org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader#runStrategy()
	 */
	@Override
	public Order runStrategy(){
			 
		Order order = new Order(Order.BUY, Order.MARKET, 0, 0, this);// default order which will not be sent to the market (because volume = 0)

		// get Prices	
		
		double[] prices = artificialMarket.getLastNPriceAsDoubles(period);
		
		
		if(prices != null)
		{
			
			double spotprice = artificialMarket.getSpotPriceAsDouble();
			
			if(prices.length >= period)
			{
				double[] upperband = new double[prices.length];
				double[] middleband = new double[prices.length];
				double[] lowerband = new double[prices.length];
				
				MInteger begin = new MInteger();
				MInteger end = new MInteger();
				
				
				//Calculate SMAs
				RetCode retCode = talib.bbands(0, prices.length-1, prices, period, nbdevup, nbdevdn, MAType.Sma, begin, end, upperband, middleband, lowerband);				
				
				if(retCode == RetCode.Success)
				{
					if(isClose(spotprice, lowerband[0]) && portfolio.getShares() == 0)
					{
						//Buy as much as possible if short crosses long bottom up 
						long vol = (long) Math.floor(this.getInvestableMoney()/spotprice);
						
						if(vol > max_buy)
						{	vol = max_buy;}

						order = new Order(Order.BUY, Order.MARKET, vol, (int) spotprice, this);
						//System.out.println("Bollinger Buy:" + order.toString());
					}
					
					if(isClose(spotprice, upperband[0]) && portfolio.getShares() > 0)
					{
						//Sell all shares if short crosses long top down and agent owns shares 
						order = new Order(Order.SELL, Order.MARKET, portfolio.getShares(), (int) spotprice, this);
						//System.out.println("Bollinger Sell:" + order.toString());
						
					}
					
				}
				
			}

		}
		return order;
	}
	
	/**
	 * decider if two prices are close by a percentage value 
	 * @param a - price a
	 * @param b - price b
	 * @return true, if a and b close, false otherwise
	 */
	private boolean isClose(double a, double b)
	{
		return ((a>(1-closePerc)*b) && (a<(1+closePerc)*b)) ? true : false;
	}
	



	
	
}
