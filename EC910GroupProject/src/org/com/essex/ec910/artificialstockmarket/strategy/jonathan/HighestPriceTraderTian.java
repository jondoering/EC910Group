package org.com.essex.ec910.artificialstockmarket.strategy.jonathan;

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
public class HighestPriceTraderTian extends AbstractTrader {


private ArrayList<Integer> priceHistory;	
private int period;

private double closePerc;
	
public HighestPriceTraderTian(final String name,final ArtificialMarket artificialMarket, final Portfolio portfolio, final int max_buy,final int max_sell) {

super(name, artificialMarket, portfolio, max_buy, max_sell);		

}



@Override
public Order runStrategy(){

Order order = new Order(Order.BUY, Order.MARKET, 0, 0, this);

double[] prices = artificialMarket.getLastNPriceAsDoubles(11);


if(prices != null)
{

double spotprice = artificialMarket.getSpotPriceAsDouble();


double maxPrice = 0;

for(int w = 1; w>prices.length; w++)
{
	maxPrice = Math.max(prices[w], maxPrice); 
}

//use all money to buy shares if the price is the highest in last 10 steps	, 
if(spotprice>maxPrice && this.portfolio.getShares()== 0){
	
	
	long vol = (long) Math.ceil(this.getInvestableMoney()/spotprice); 
	
	if(vol > max_buy)
	{	vol = max_buy;}
	
	order = new Order(Order.BUY, Order.MARKET, vol , (int) spotprice, this);

}
else if(( spotprice<= prices[1] && spotprice <= prices[2]) && (this.portfolio.getShares() > 0)){
//sell all shares if the price keep dropping for 2 steps 

order = new Order(Order.SELL, Order.MARKET, portfolio.getShares(), (int) spotprice, this);

}

}


return order;
}


}
	
