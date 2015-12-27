package org.com.essex.ec910.artificialstockmarket.market;

import org.com.essex.ec910.artificialstockmarket.datasource.DatabaseConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

	private ArrayList<Order> sellOrderBook;
	private ArrayList<Order> buyOrderBook;

	private ArrayList<Integer> priceHistory;
	private ArrayList<Integer> volumeHistory;

	/**
	 * if true, orderbook is printed every step
	 */
	private boolean showOrderBook;

	// For debuging onlu
	private FileWriter fw;


	/**
	 * if true, orders are printed every step
	 */
	private boolean printOrders;

	/**
	 * Constructor
	 * 
	 * @param initialPrice
	 */
	public ArtificialMarket(int[] initPrices, int[] initVolumes, boolean printOrderBook, boolean printOrders) {
		this.showOrderBook = printOrderBook;
		this.printOrders = printOrders;

		sellOrderBook = new ArrayList<Order>();
		buyOrderBook = new ArrayList<Order>();
		priceHistory = new ArrayList<Integer>();
		volumeHistory = new ArrayList<Integer>();

		try {
			fw = new FileWriter(new File("output.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Copy initial values in list
		for (int i = 0; i < initPrices.length; i++) {
			priceHistory.add(initPrices[i]);
			volumeHistory.add(initVolumes[i]);
		}

	}

	/**
	 * order driven clearing mechanism new price is found by
	 * "largest best execution" principal Compare ...
	 */
	public void clearMarket() {

		// if orderbooks empty, do nothing
		if (sellOrderBook.isEmpty() && buyOrderBook.isEmpty()) {
			return;
		}

		// Otherwise: find new price by volume maximization
		// Find Orders with same price and cumulate the Volume
		Collections.sort(sellOrderBook);

		//holds prices for each level
		ArrayList<Double> sellPriceLevels = new ArrayList<Double>(); 
	
		// Hold cumulated volume for each price level
		ArrayList<Integer> sellVolumePerLevelList = new ArrayList<Integer>(); 

		// holds list of orders for each price level
		ArrayList<ArrayList<Order>> samesellOrders = new ArrayList<ArrayList<Order>>();

		Double lastLevel = (double) -1;
		int sellVolumePerLevel = 0;

		Order curOrder = null;
		ArrayList<Order> curLevelList = new ArrayList<Order>(); // temporary
																// list on each
																// level

		double minsellLimitPrice = 0;
		double maxsellLimitPrice = 0;

		for (int i = 0; i < sellOrderBook.size(); i++) {
			curOrder = sellOrderBook.get(i);
			// System.out.println(curOrder.toString());
			// If Market Order, by definition at the beginning of the list
			if (curOrder.getType2() == Order.MARKET) {
				if (lastLevel < 0) {
					// Create new Market Level first Time
					sellPriceLevels.add(0.0);
					lastLevel = 0.0;
					sellVolumePerLevel = 0;
				}
				// Add Order and Volume to acutely Level
				curLevelList.add(curOrder);
				sellVolumePerLevel += curOrder.getVolume();
			} else
			// Limit Orders
			{

				if (curOrder.getLimitprice() > lastLevel) {
					if (curLevelList.size() > 0)
					// Add only if they were Orders at last Level
					{
						samesellOrders.add(curLevelList);
						sellVolumePerLevelList.add(sellVolumePerLevel);
					}

					// add new Price Level
					if (minsellLimitPrice > 0) {
						minsellLimitPrice = Math.min(minsellLimitPrice, curOrder.getLimitprice());
					} else {
						minsellLimitPrice = curOrder.getLimitprice();
					}

					sellPriceLevels.add((double) curOrder.getLimitprice());
					lastLevel = (double) curOrder.getLimitprice();
					minsellLimitPrice = Math.min(minsellLimitPrice, lastLevel);
					// Reset parameter
					curLevelList = new ArrayList<Order>();
					sellVolumePerLevel = 0;
				}

				curLevelList.add(curOrder);
				sellVolumePerLevel += curOrder.getVolume();
			}
		}

		// Add last level
		if (curLevelList.size() > 0)
		// Add only if they were Orders at last Level
		{
			samesellOrders.add(curLevelList);
			sellVolumePerLevelList.add(sellVolumePerLevel);
		}

		maxsellLimitPrice = lastLevel;

		Collections.sort(buyOrderBook);

		ArrayList<Double> buyPriceLevels = new ArrayList<Double>();
		ArrayList<Integer> buyVolumePerLevelList = new ArrayList<Integer>();
		ArrayList<ArrayList<Order>> samebuyOrders = new ArrayList<ArrayList<Order>>();

		lastLevel = (double) -1;
		int buyVolumePerLevel = 0;

		curOrder = null;
		curLevelList = new ArrayList<Order>();

		double maxbuyLimitPrice = 0;
		int minbuyLimitPrice = 0;

		for (int i = 0; i < buyOrderBook.size(); i++) {
			curOrder = buyOrderBook.get(i);
			
			
			// If Market Order, by definition at the beginning of the list
			if (curOrder.getType2() == Order.MARKET) {
				if (lastLevel < 0) // Initiation
				{
					// Create new Market Level first Time
					buyPriceLevels.add(0.0);
					lastLevel = 0.0;
					buyVolumePerLevel = 0;
				}
				// Add Order and Volume to acutely Level
				curLevelList.add(curOrder);
				buyVolumePerLevel += curOrder.getVolume();

			} else
			// Limit Orders
			{

				if (curOrder.getLimitprice() > lastLevel) {
					if (curLevelList.size() > 0)
					// Add only if they were Orders at last Level
					{
						samebuyOrders.add(curLevelList);
						buyVolumePerLevelList.add(buyVolumePerLevel);
					}
					// add new Price Level
					if (minbuyLimitPrice > 0) {
						minbuyLimitPrice = Math.min(minbuyLimitPrice, curOrder.getLimitprice());
					} else {
						minbuyLimitPrice = curOrder.getLimitprice();
					}

					// Create new Market Level first Time
					buyPriceLevels.add((double) curOrder.getLimitprice());
					lastLevel = (double) curOrder.getLimitprice();
					// Reset parameter
					curLevelList = new ArrayList<Order>();
					buyVolumePerLevel = 0;
				}

				curLevelList.add(curOrder);
				buyVolumePerLevel += curOrder.getVolume();
			}
		}

		if (curLevelList.size() > 0)
		// Add only if they were Orders at last Level
		{
			samebuyOrders.add(curLevelList);
			buyVolumePerLevelList.add(buyVolumePerLevel);
		}

		maxbuyLimitPrice = lastLevel;

		// Orderbook Statistics

		// Step 2: Find new Price

		int newPrice = getSpotPrice();
		int transition = 0;
		int nShares = 0;

		int[] buyCumVolPerLevelList = new int[buyVolumePerLevelList.size()];

		if (true) {
			// Price is where execution volume gets maximized
			// Match Price Levels of sell and buy

			// Cumulatet Voulmes
			int[] sellCumVolPerLevelList = new int[sellVolumePerLevelList.size()];

			int vol = 0;
			// Easy for sell Orders
			for (int i = 0; i < sellVolumePerLevelList.size(); i++) {
				vol += sellVolumePerLevelList.get(i);
				sellCumVolPerLevelList[i] = vol;
			}

			// More Complex for buy Orders
			// Exception for Market Orders
			if (buyPriceLevels.get(0) == 0.0) { // there Are Market Orders

				vol = buyVolumePerLevelList.get(0);

				for (int i = (buyVolumePerLevelList.size() - 1); i >= 1; i--) {
					vol += buyVolumePerLevelList.get(i);
					buyCumVolPerLevelList[i] = vol;					
				}

				/*
				 * //Cumulate over all volume int oabuyVolume =0; for(int
				 * i=0;i<buyVolumePerLevelList.size();i++) { oabuyVolume +=
				 * buyVolumePerLevelList.get(i);}
				 * 
				 * vol = oabuyVolume; buyCumVolPerLevelList[0] = vol;
				 * 
				 * for(int i=1;i<buyVolumePerLevelList.size();i++) { vol -=
				 * buyVolumePerLevelList.get(i-1); buyCumVolPerLevelList[i] =
				 * vol; }
				 */
			}

			else
			// No Market Orders
			{
				vol = 0;
				for (int i = (buyVolumePerLevelList.size() - 1); i >= 0; i--) {
					vol += buyVolumePerLevelList.get(i);
					buyCumVolPerLevelList[i] = vol;
				}
				/*
				 * //there are not Market Orders
				 * 
				 * //Cumulate over all int oabuyVolume =0; for(int
				 * i=0;i<buyVolumePerLevelList.size();i++) { oabuyVolume +=
				 * buyVolumePerLevelList.get(i);}
				 * 
				 * //Descending Order vol = oabuyVolume; for(int
				 * i=1;i<buyVolumePerLevelList.size();i++) { vol -=
				 * buyVolumePerLevelList.get(i-1); buyCumVolPerLevelList[i] =
				 * vol; }
				 */
			}

			// Matching price Vectors and find new Price
			int maxPrice = Math.max((int) maxbuyLimitPrice, (int) maxsellLimitPrice);
			int minPrice = Math.min((int) minbuyLimitPrice, (int) minsellLimitPrice);

			//
			int countbuy = 0;
			int marketOrderOffset = 0;
			if (buyPriceLevels.get(0) == 0.0) { // there Are Market Orders
				marketOrderOffset = 0;
				countbuy = 1;
			}

			int buyLevelCumVol = buyCumVolPerLevelList[countbuy];
			;

			int countsell = 0;
			int sellLevelCumVol = 0;
			if (sellPriceLevels.get(0) == 0.0) { // there Are Market Orders
				sellLevelCumVol = sellCumVolPerLevelList[0];
				countsell = 1;
			}

			int sellVol = 0, buyVol = 0;

			int maxVolume = buyLevelCumVol;

			if (showOrderBook) {
				//
				// try {
				// fw.write(String.format("Price \t sellVol \t buyVol \t sellCum
				// \t buyCum \t
				// Transition\n---------------------------------------------------------------\n"));
				// fw.write(System.lineSeparator());
				// fw.flush();
				//
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				System.out.printf(
						"Price \t sellVol \t buyVol \t sellCum \t buyCum \t Transition\n---------------------------------------------------------------\n");
			}

			boolean lastLevelMatch = false;

			// go over whole order book (level by level) and set price there
			// where transition is minimized
			for (int i = minPrice; i <= maxPrice; i++) {

				if ((buyPriceLevels.get(countbuy)) == i) // price match
				{
					lastLevelMatch = true;
					buyVol = buyVolumePerLevelList.get(countbuy);
					buyLevelCumVol = buyCumVolPerLevelList[countbuy];
					if (countbuy < buyCumVolPerLevelList.length - 1) {
						countbuy++;
					}
				} else // no price match
				{
					// But last was a price match
					if (lastLevelMatch) {
						if (buyPriceLevels.get(buyPriceLevels.size() - 1) < i)
						{// if no more price matches in this step
							
							if (buyPriceLevels.get(0) == 0.0) {
								// last CumVolume is MarketOrderVolume
								// market order size
								buyLevelCumVol = buyVolumePerLevelList.get(0); 
							} else {
								// No Buy Volume
								buyLevelCumVol = 0;
							}
						} else {
							// if more price matches will be happen
							// then go one Volume Ahead
							buyLevelCumVol = buyCumVolPerLevelList[countbuy];
						}
					}

					buyVol = 0;
					lastLevelMatch = false;
				}

				if (sellPriceLevels.get(countsell) == i) {
					sellVol = sellVolumePerLevelList.get(countsell);
					sellLevelCumVol = sellCumVolPerLevelList[countsell];
					if (countsell < sellCumVolPerLevelList.length - 1) {
						countsell++;
					}
					;
				} else {
					sellVol = 0;
				}

				int h = Math.abs(sellLevelCumVol - buyLevelCumVol);

				// Maximise execution volume (-> minimize transition)
				if (maxVolume > h && h != 0) {
					maxVolume = h;
					transition = h;
					newPrice = i;
					nShares = buyLevelCumVol;
				}

				if (showOrderBook)
				// Print Orderbook
				{
					// if(sellVol != 0 || buyVol != 0)
					// {
					// try {
					//
					// fw.write(String.format("%d \t\t %d \t\t %d \t\t %d \t\t
					// %d \t\t %d \n ", i, sellVol, buyVol, sellLevelCumVol,
					// buyLevelCumVol, h));
					// fw.write(System.lineSeparator());
					// fw.flush();
					//
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					System.out.printf("%d \t\t %d \t\t %d \t\t %d \t\t %d \t\t %d \n  ", i, sellVol, buyVol,
							sellLevelCumVol, buyLevelCumVol, h);
				}
				// }

			}
			System.out.println();
			System.out.println("Match: Price: " + newPrice + " with Volume " + nShares);

		} else {

			// Market Orders Only
			// old price is new price
			// can never happen because of market maker

			newPrice = getSpotPrice();
			// nShares = sellVolumePerLevelList
		}

// Step 3. Execute all Orders that fits price by
		// first: buy all needed shares from seller
		// second: sell all needed shares to buyer
		
		
		// List of all Orders for price p
		int leftShares = nShares;

		int sellIndex = 0;
		ArrayList<Order> sellOrders = new ArrayList<Order>();
		ArrayList<Integer> orderSize = new ArrayList<Integer>();

		// Execute Sell Orders (buy from seller)
		for (int i = 0; i < sellOrderBook.size(); i++) {

			int shares = 0;
			if (sellOrderBook.get(i).getVolume() < leftShares) {
				shares = sellOrderBook.get(i).getVolume();
			} else {
				// transitions
				shares = leftShares;
			}

			int money = shares * newPrice;
			sellOrderBook.get(i).getOwner().buyShareFromTrader(money, shares);
			leftShares -= shares;

			if (leftShares == 0) {
				break;
			}
		}

		// Execute Buy Orders (sell to buyer)
		// if there are market orders, execute first
		if (buyPriceLevels.get(0) == 0.0) {
			Iterator<Order> orders = samebuyOrders.get(0).iterator();
			while (orders.hasNext()) {
				// Execute Order
				Order o = orders.next();

				int shares = o.getVolume();
				int money = shares * newPrice;

				o.getOwner().sellShareToTrader(money, shares);
			}
		}

		// Execute Buy Orders Level by Level
		for (int i = buyPriceLevels.size() - 1; i > 0; i--) {
			Iterator<Order> orders = samebuyOrders.get(i).iterator();
			while (orders.hasNext()) {
				// Execute Order
				Order o = orders.next();

				int shares = o.getVolume();
				int money = shares * newPrice;

				o.getOwner().sellShareToTrader(money, shares);
			}

			if (newPrice == buyPriceLevels.get(i)) {
				break;
			}
		}

		// Store Price and clear order books
		priceHistory.add(newPrice);
		volumeHistory.add(nShares);
		buyOrderBook.clear();
		sellOrderBook.clear();

	}

	/**
	 * adds an Order to the order book
	 * 
	 * @param order
	 */
	public void reciveOrder(Order order) {

		if (printOrders) {
			System.out.println(order.toString());
		}

		try {
			fw.write(order.toString());
			fw.write(System.lineSeparator());
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (order.getType1() == Order.BUY) {
			buyOrderBook.add(order);
		}

		else if (order.getType1() == Order.SELL) {
			sellOrderBook.add(order);
		}

		else {
		} // do nothing, Order isn't allowed
	}

	/**
	 * Returns the last n prices as an Array of integer in order is [t, t-1,
	 * t-2, .. , t-n]. If n is grater then the price list, the whole price list
	 * will be returned.
	 * 
	 * @param n
	 *            - number of prices
	 * @return array of integer with last n prices or null if list is empty
	 */
	public Integer[] getLastNPrice(int n) {
		if (priceHistory.isEmpty()) {
			return null;
		}

		if (n > priceHistory.size()) {
			n = priceHistory.size();
		}

		Integer[] lastPrices = new Integer[n];

		for (int i = 0; i < n; i++) {
			lastPrices[i] = priceHistory.get(priceHistory.size() - (i + 1));
		}

		return lastPrices;

	}

	/**
	 * Returns the last n volumes as an Array of integer in order is [t, t-1,
	 * t-2, .. , t-n]. If n is grater then the price list, the whole volume list
	 * will be returned.
	 *
	 * @param n
	 *            - number of volumes
	 * @return array of integer with last n volume values or null if list is
	 *         empty
	 */
	public Integer[] getLastNVolume(int n) {
		if (volumeHistory.isEmpty()) {
			return null;
		}

		if (n > volumeHistory.size()) {
			n = volumeHistory.size();
		}

		Integer[] lastVolume = new Integer[n];

		for (int i = 0; i < n; i++) {
			lastVolume[i] = volumeHistory.get(volumeHistory.size() - (i + 1));
		}

		return lastVolume;

	}

	/**
	 * returns last founded price in market
	 * 
	 * @return spot Price
	 */
	public int getSpotPrice() {
		Integer[] lastPrice = getLastNPrice(1);

		if (lastPrice == null) {
			return -1;
		} else {
			return lastPrice[0];
		}
	}
	
	/**
	 * returns last founded price in market as double (for time series in observer only)
	 * by simple casting
	 * @return spot Price
	 */
	public double getSpotPriceAsDouble() {
		return (double) getSpotPrice();
	}

	/**
	 * returns last executed Volume in Market
	 * 
	 * @return
	 */
	public int getSpotVolume() {
		Integer[] lastVol = getLastNVolume(1);

		if (lastVol == null) {
			return -1;
		} else {
			return lastVol[0];
		}
	}

	/**
	 * returns last executed volume in market as double (for time series in observer only)
	 * by simple casting
	 * @return spot Price
	 */
	public double getSpotVolumeAsDouble() {
		return (double) getSpotVolume();
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
