//
//
//package org.com.essex.ec910.artificialstockmarket.trader.jonathan;
//
//// buy at new highest price
//// Tian
//
//import java.util.stream.Stream;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.stream.Stream;
//import org.com.essex.ec910.artificialstockmarket.market.ArtificialMarket;
//import org.com.essex.ec910.artificialstockmarket.market.Order;
//import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;
//import org.com.essex.ec910.artificialstockmarket.trader.Portfolio;
//import com.tictactec.ta.lib.Core;
//import com.tictactec.ta.lib.MAType;
//import com.tictactec.ta.lib.MInteger;
//import com.tictactec.ta.lib.RetCode;
//
//
//public class highest extends AbstractTrader {
//
//
//private ArrayList<Integer> priceHistory;	
//private int period;
//
//private double historyp[] = new double[10];
//private double money;
///**
// * Ta Lib Core Object
///**
// * 
// */
//private Core talib;
//private double closePerc;
//	
//public highest(final String name,final ArtificialMarket artificialMarket, final Portfolio portfolio, final int max_buy,final int max_sell,final int period, final double closePer, final double historyprice[], final double money) {
//super(name, artificialMarket, portfolio, max_buy, max_sell);		
//		
//this.period = period;
//
//this.closePerc = closePer;
//for ( int s =0;s < 10;s++){
//   this.historyp[s] = historyprice[s];
//}
//	talib = new Core();
//}
//
//
//
//@Override
//public Order runStrategy(double spotprice){
//
//	Order order = new Order(Order.BUY, Order.MARKET, 0, 0, this);
//
////use all money to buy shares if the price is the highest in last 10 days	, 
//
//double[] prices = artificialMarket.getLastNPriceAsDoubles(period);
//
//
//if(prices != null)
//{
//
//double spotprice = artificialMarket.getSpotPriceAsDouble();
//
//for(int w = 0;closePerc >=historyp[w]||w>=9;w++){
//
//MInteger begin = new MInteger();
//MInteger end = new MInteger();
//order = new Order(Order.BUY, Order.MARKET, max_buy, (int) spotprice, this);
//System.out.println("highrster Buy:" + order.toString());
//}
//
//
////sell all shares if the price keep dropping for 2 days 
//
//if( spotprice>= historyp[0] && historyp[0]>= historyp[1]){
//
//order = new Order(Order.SELL, Order.MARKET, portfolio.getShares(), (int) spotprice, this);
//System.out.println("highester Sell:" + order.toString());
//
//}
//
//}
//
//
//return order;
//}
//	
