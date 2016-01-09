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
 * @author MAO WEIGUANG
 *
 */
public class WilliamTradingStrategy extends AbstractTrader {

	
	private double sellPercent;
	
	
	/**
	 * Ta Lib Core Object
	 */
	private Core talib;
	private int period;
	
	
	public WilliamTradingStrategy(final String name,final ArtificialMarket artificialMarket, final Portfolio portfolio, final int max_buy,
			final int max_sell,final double buyPercentd, final int period) {
		super(name, artificialMarket, portfolio, max_buy, max_sell);		
		
		this.sellPercent = buyPercentd;
		this.period = period;
		
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
				double[] outLong = new double[prices.length];
				
				MInteger begin = new MInteger();				
				MInteger length = new MInteger();
				
				//Calculate SMAs
				RetCode retCode = talib.sma(0, prices.length-1, prices, period, begin, length, outLong);				
				
				
				if(retCode == RetCode.Success)
				{
					if(spotprice <= outLong[0]  && this.portfolio.getShares() == 0 ) 
					{
						//Buy as much as possible if price is close to moving averagh
						long vol = (long) Math.ceil(this.getInvestableMoney()/spotprice);

						if(vol > max_buy)
						{	vol = max_buy;}

						order = new Order(Order.BUY, Order.MARKET, vol, (int) spotprice, this);
					}
					
					if(spotprice >= outLong[0]*(1+sellPercent) && this.portfolio.getShares() > 0)
					{
						//Sell all shares if current price is sellPercent above the average 
						order = new Order(Order.SELL, Order.MARKET, portfolio.getShares(), (int) spotprice, this);
						
					}
					
				}
				
			}

		}
		return order;
	}
	
	



	
	
}
