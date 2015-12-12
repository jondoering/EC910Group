package org.com.essex.ec910.artificialstockmarket.market;

import org.com.essex.ec910.artificialstockmarket.datasource.DatabaseConnector;
import org.com.essex.ec910.artificialstockmarket.trader.AbstractTrader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

/**
 * 
 * @author Jonathan
 *
 */
public class ArtificialMarket {
	
	
	private DatabaseConnector db;
	
	private ArrayList<Order> bidOrderBook;
	private ArrayList<Order> askOrderBook;
	
	private ArrayList<Integer> lastPrice;
	


	/**
	 * Constructor
	 * @param db
	 */
	public ArtificialMarket(DatabaseConnector db)
	{
		this.db = db;
		bidOrderBook = new ArrayList<Order>();
		askOrderBook = new ArrayList<Order>();
		lastPrice = new ArrayList<Integer>();
	}
	
	
	/**
	 * order driven clearing mechanism
	 * clearing price maximize the execution volume (Compare XYZ)
	 */
	public void clearMarket()
	{

		//Find Orders with same price and cumulate the Volume 
		Collections.sort(bidOrderBook);
		 
		ArrayList<Double> bidPriceLevels = new ArrayList<Double>(); //holds Prices for each Level
		ArrayList<Integer> bidVolumePerLevelList = new ArrayList<Integer>(); //holds cumulated volume for each price Level
		ArrayList<ArrayList<Order>> sameBidOrders = new ArrayList<ArrayList<Order>>(); // holds list of orders for each price level
		

		
		Double lastLevel = (double) -1; 
		int bidVolumePerLevel = 0;
	
		Order curOrder = null;
		ArrayList<Order> curLevelList = new ArrayList<Order>();		//temporary list on each level			

		double minBidLimitPrice = 0;

		for(int i=0;i<bidOrderBook.size();i++)
		{			
			curOrder = bidOrderBook.get(i);
			System.out.println(curOrder.toString());
			//If Market Order, by definition at the beginning of the list
			if(curOrder.getType2()==Order.MARKET)
			{
				if(lastLevel < 0 )
				{
					//Create new Market Level first Time 
					bidPriceLevels.add(0.0);
					lastLevel = 0.0;
					bidVolumePerLevel = 0;
				}
				//Add Order and Volume to acutely Level
				curLevelList.add(curOrder);
				bidVolumePerLevel += curOrder.getVolume();
			}
			else
			//Limit Orders
			{

				if(curOrder.getLimitprice() > lastLevel)
				{
					if(curLevelList.size() > 0)
					//Add only if they were Orders at last Level
					{	
						sameBidOrders.add(curLevelList);
						bidVolumePerLevelList.add(bidVolumePerLevel);
												
					}	
					
					//add new Price Level
					if(lastLevel>0)
					{	minBidLimitPrice = Math.min(minBidLimitPrice, curOrder.getLimitprice());}
					else
					{	minBidLimitPrice = curOrder.getLimitprice();}
					bidPriceLevels.add((double) curOrder.getLimitprice());
					lastLevel = (double) curOrder.getLimitprice();
					minBidLimitPrice = Math.min(minBidLimitPrice, lastLevel);
					//Reset parameter
					curLevelList = new ArrayList<Order>();			
					bidVolumePerLevel = 0;
				}
				
				curLevelList.add(curOrder);
				bidVolumePerLevel += curOrder.getVolume();
			}			
		}
		//Add
		if(curLevelList.size() > 0)
			//Add only if they were Orders at last Level
			{	
				sameBidOrders.add(curLevelList);
				bidVolumePerLevelList.add(bidVolumePerLevel);										
			}	
		
		
		Collections.sort(askOrderBook);

		ArrayList<Double> askPriceLevels = new ArrayList<Double>();
		ArrayList<Integer> askVolumePerLevelList = new ArrayList<Integer>();	
		ArrayList<ArrayList<Order>> sameAskOrders = new ArrayList<ArrayList<Order>>();
		
		lastLevel = (double) -1; 
		int askVolumePerLevel = 0;
		
		curOrder = null;
		curLevelList = new ArrayList<Order>();					

		
		for(int i=0;i<askOrderBook.size();i++)
		{			
			curOrder = askOrderBook.get(i);
			//If Market Order, by definition at the beginning of the list
			if(curOrder.getType2()==Order.MARKET)
			{
				if(lastLevel < 0 ) //Initiation
				{
					//Create new Market Level first Time 
					askPriceLevels.add(0.0);
					lastLevel = 0.0;
					askVolumePerLevel = 0;
				}
				//Add Order and Volume to acutely Level
				curLevelList.add(curOrder);
				askVolumePerLevel += curOrder.getVolume();
				
			}
			else
			//Limit Orders
			{

				if(curOrder.getLimitprice() > lastLevel)
				{
					if(curLevelList.size() > 0)
					//Add only if they were Orders at last Level
					{	
						sameAskOrders.add(curLevelList);
						askVolumePerLevelList.add(askVolumePerLevel);
					}	
					
					//Create new Market Level first Time 
					askPriceLevels.add((double) curOrder.getLimitprice());
					lastLevel = (double) curOrder.getLimitprice();
					//Reset parameter
					curLevelList = new ArrayList<Order>();			
					askVolumePerLevel = 0;
				}
				
				curLevelList.add(curOrder);
				askVolumePerLevel += curOrder.getVolume();
			}			
		}
		
		if(curLevelList.size() > 0)
		//Add only if they were Orders at last Level
		{	sameAskOrders.add(curLevelList);
				askVolumePerLevelList.add(askVolumePerLevel);
		}	
		
		double maxAskLimitPrice = lastLevel;
		
//Step 2: Find new Price if there are limit order
		
		int newPrice = 0;
		int transition = 0;
		int nShares = 0;
		
		if(true)
		{
			// Price is where execution volume gets maximized			
			//Match Price Levels of bid and ask		
			
			//Cumulatet Voulmes
			int[] bidCumVolPerLevelList = new int[bidVolumePerLevelList.size()];
			int[]  askCumVolPerLevelList = new int[askVolumePerLevelList.size()];
			
			int vol = 0;
		//Easy for Bid Orders
			for(int i=0;i<bidVolumePerLevelList.size();i++)
			{
				vol += bidVolumePerLevelList.get(i);
				bidCumVolPerLevelList[i] = vol;				
			}

			int lastElement = 0;
			//Exception for Market Orders
			if(askPriceLevels.get(0) == 0.0)
			{	//there Are Market Orders
				
				//Cumulate over all 
				int oaAskVolume =0;
				for(int i=0;i<askVolumePerLevelList.size();i++)
				{	oaAskVolume += askVolumePerLevelList.get(i);}
					
				vol = oaAskVolume;
				for(int i=1;i<askVolumePerLevelList.size();i++)
				{
					askCumVolPerLevelList[i] = vol;
					vol -= askVolumePerLevelList.get(i);				
				}
			}
			
			else 
			//No Market Orders
			{
				//there Are Market Orders
			
				//Cumulate over all 
				int oaAskVolume =0;
				for(int i=0;i<askVolumePerLevelList.size();i++)
				{	oaAskVolume += askVolumePerLevelList.get(i);}
					
				vol = oaAskVolume;
				for(int i=0;i<askVolumePerLevelList.size();i++)
				{
					askCumVolPerLevelList[i] = vol;
					vol -= askVolumePerLevelList.get(i);				
				}
				
			}
			
		//Matching price Vectors and find new Price
			
			
			int maxPrice = (int) maxAskLimitPrice;
			int minPrice = (int) minBidLimitPrice;
			
			//
			int countAsk = 0;
			if(askPriceLevels.get(0) == 0.0)
			{	//there Are Market Orders			
				 countAsk = 1;}
			
			int countBid = 0;
			if(bidPriceLevels.get(0) == 0.0)
			{	//there Are Market Orders			
				countBid = 1;}
							
			
			int bidLevelCumVol = 0;
			int askLevelCumVol = askCumVolPerLevelList[countAsk];;
			int maxVolume = askLevelCumVol;

			for(int i = minPrice; i<=maxPrice; i++)
			{
				
				
				if(askPriceLevels.get(countAsk) == i)
				{
					askLevelCumVol = askCumVolPerLevelList[countAsk];
					if(countAsk<askCumVolPerLevelList.length-1){countAsk++;};
				}				
				if(bidPriceLevels.get(countBid) == i)
				{
					bidLevelCumVol = bidCumVolPerLevelList[countBid];
					if(countBid<bidCumVolPerLevelList.length-1){countBid++;};
				}
				
				//Calc  cumulatedVolume Match
				int h = Math.abs(bidLevelCumVol - askLevelCumVol);
				System.out.println("Price: " + i + " Transition: " + h);
				//Maximise execution volume
				if(maxVolume > h)
				{	
					maxVolume = h;
					transition = h;
					newPrice = i;
					nShares = askLevelCumVol;
				}
				
				
				
			}
		
		}
		else
		{
			
			//Market Orders Only
			//
			// old price is new price
			
		}
		
		System.out.println("Match: Price: " + newPrice + " Transition: " + transition + " Need Shares: " + nShares);
		
		
	//Step 3. Execute all Orders that fits price  
		
	//List of all Orders for price p
		int leftShares = nShares;
		
		int bidIndex = 0;
		ArrayList<Order> sellOrders = new ArrayList<Order>();
		ArrayList<Integer> orderSize = new ArrayList<Integer>();
		
		
		//Execute Sell Orders
		
		for(int i=0;i<bidOrderBook.size();i++)
		{
		
			int shares = 0;
			if(bidOrderBook.get(i).getVolume() < leftShares)
			{
				 shares = bidOrderBook.get(i).getVolume();			
			}
			else
			{
				//transitions
				 shares = leftShares;				
			}
			
			int money =  shares * newPrice; 
			bidOrderBook.get(i).getOwner().buyShareFromTrader(money, shares);
			leftShares -= shares;
			
			if(leftShares == 0)
			{ break;}
		}
		
		
		//if there are market orders, exdcute first
		if(askPriceLevels.get(0) == 0.0)
		{
			Iterator<Order> orders = sameAskOrders.get(0).iterator();
			while(orders.hasNext())
			{
				//Execute Order
				Order o = orders.next();
				
				int shares = o.getVolume();
				int money = shares * newPrice;
				
				o.getOwner().sellShareToTrader(money, shares);
			}
		}
		 
		//Execute Buy Orders Level by Level
		for(int i=askPriceLevels.size()-1; i > 0; i--)
		{
			Iterator<Order> orders = sameAskOrders.get(i).iterator();
			while(orders.hasNext())
			{
				//Execute Order
				Order o = orders.next();
				
				int shares = o.getVolume();
				int money = shares * newPrice;
				
				o.getOwner().sellShareToTrader(money, shares);
			}
			
			if(newPrice == askPriceLevels.get(i))
			{
				break;
			}
		}
		//
		lastPrice.add(newPrice);
	
		
	}
	
	
	/**
	 * adds an Order to the order book
	 * @param order 
	 */
	public void reciveOrder(Order order)
	{
		if(order.getType1() == Order.BUY)
		{	askOrderBook.add(order);}
		
		else if(order.getType1() == Order.SELL)
		{	bidOrderBook.add(order);}
		
		else
		{	}//do nothing, Order isn't allowed 
	}
	
	
	/**
	 * Returns the last n prices as an Array of integer in order is [t, t-1, t-2, .. , t-n]. 
	 * If n is grater then the price list, the whole price list will be returned. 
	 * 
	 * @param n - number of prices to show
	 * @return array of integer with last n prices
	 */
	public int[] getLastNPrice(int n)
	{
		if(n>lastPrice.size())
		{	n = lastPrice.size();}
		
		int[] lastPrices = new int[n];
		
		for(int i=0; i<n; i++)
		{
			lastPrices[i] = lastPrice.get(lastPrice.size()-(i+1));
		}
		
		return lastPrices;
		
	}
	
	/**
	 * @param min
	 * @param max
	 * @return
	 */
	private int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}


}
