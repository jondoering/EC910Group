package org.com.essex.ec910.artificialstockmarket.strategy;

// buy at new highest price
// Tian

import java.util.stream.Stream;
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
 * @author Tian
 *
 */
public class MNHighestPriceTraderTian extends AbstractTrader {

private double lastBuyPrice = 0; 
private int m;
private int n;
	
public MNHighestPriceTraderTian(final String name,final ArtificialMarket artificialMarket, final Portfolio portfolio, final int max_buy,final int max_sell, final int m, final int n) {

super(name, artificialMarket, portfolio, max_buy, max_sell);

this.m = m;
this.n = n;

}



@Override
public Order runStrategy(){

Order order = new Order(Order.BUY, Order.MARKET, 0, 0, this);

double[] prices = artificialMarket.getLastNCloseAsDoubles(Math.max(m+1, n+1));


if(prices != null && prices.length==Math.max(m+1, n+1))
{

double lastClose = prices[0];


double maxPrice = 0;
boolean lower = false;

for(int w = 1; w>m; w++)
{
	maxPrice = Math.max(prices[w], maxPrice); 
}

int minPrice = Integer.MAX_VALUE;
for(int w = 1; w>n; w++)
{
	minPrice = (int) Math.min(prices[w], minPrice); 
}

//use all money to buy shares if the price is the highest in last 10 steps	, 
if(lastClose>maxPrice && this.portfolio.getShares()== 0 && lastClose!=lastBuyPrice){
	
	lastBuyPrice = lastClose;
	
	long vol = (long) Math.floor(this.getInvestableMoney()/lastClose); 
	
	if(vol > max_buy)
	{	vol = max_buy;}
	
	order = new Order(Order.BUY, Order.MARKET, vol , (int) lastClose, this);
	
}
else if(lastClose < minPrice && (this.portfolio.getShares() > 0)){
//sell all shares if the price keep dropping for 2 days 

	order = new Order(Order.SELL, Order.MARKET, portfolio.getShares(), (int) lastClose, this);

}

}


return order;
}


}
	
