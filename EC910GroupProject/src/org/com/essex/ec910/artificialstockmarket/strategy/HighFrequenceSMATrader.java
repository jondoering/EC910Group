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
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * simple moving average strategy compare  
 * without explicit money management
 * @author Jonathan
 *
 */
public class HighFrequenceSMATrader extends AbstractTrader {

	
	private int longPeriod;
	private int shortPeriod;
	
	/**
	 * Ta Lib Core Object
	 */
	private Core talib;
	
	public HighFrequenceSMATrader(final String name,final ArtificialMarket artificialMarket, final Portfolio portfolio, final int max_buy,
			final int max_sell,final int longPeriod, final int shortPeriod) {
		super(name, artificialMarket, portfolio, max_buy, max_sell);		
		
		this.longPeriod = longPeriod;
		this.shortPeriod = shortPeriod;
		
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
		
		double[] prices = artificialMarket.getLastNPriceAsDoubles(longPeriod);
		
		
		if(prices != null)
		{
			
			double spotprice = artificialMarket.getSpotPriceAsDouble();
			
			if(prices.length >= longPeriod)
			{
				double[] outLong = new double[prices.length];
				double[] outShort = new double[prices.length];
				
				MInteger beginLong = new MInteger();
				MInteger beginShort = new MInteger();
				
				MInteger lengthLong = new MInteger();
				MInteger lengthShort = new MInteger();
				
				//Calculate SMAs
				RetCode retCodeLong = talib.sma(0, prices.length-1, prices, longPeriod, beginLong, lengthLong, outLong);				
				//Calculate SMAs
				RetCode retCodeShort = talib.sma(0, prices.length-1, prices, shortPeriod, beginShort, lengthShort, outShort);
				
				if(retCodeLong == RetCode.Success && retCodeShort == RetCode.Success)
				{
					if((outShort[0] > outLong[0]) && portfolio.getShares() == 0)
					{
						//Buy as much as possible if short crosses long bottom up 
						long vol = (long) Math.floor(this.getInvestableMoney()/spotprice);

						if(vol > max_buy)
						{	vol = max_buy;}

						order = new Order(Order.BUY, Order.MARKET, vol, (int) spotprice, this);
						//System.out.println("SMA Buy:" + order.toString());
					}
					
					if((outShort[0] < outLong[0]) && portfolio.getShares() > 0)
					{
						//Sell all shares if short crosses long top down and agent owns shares 
						order = new Order(Order.SELL, Order.MARKET, portfolio.getShares(), (int) spotprice, this);
						//System.out.println("SMA Sell:" + order.toString());
						
					}
					
				}
				
			}

		}
		return order;
	}
	
	



	
	
}
