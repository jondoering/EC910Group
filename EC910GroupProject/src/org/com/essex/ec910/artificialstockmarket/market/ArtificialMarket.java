package org.com.essex.ec910.artificialstockmarket.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Main Class of the model. Describes and implements an artificial stock market where
 * agents can trade on. Therefore a version of the auction based trading system used by XETRA
 * and SETS was implemented. The price finding mechanism is execution volume maximizing. 
 * Overall two different Order types (market and limit order) are supported. 
 * For further information please see related course work.
 *  
 * @author Jonathan Doering
 *
 */
public class ArtificialMarket {


	private ArrayList<Order> sellOrderBook;
	private ArrayList<Order> buyOrderBook;

	private ArrayList<Integer> priceHistory;
	private ArrayList<Integer> volumeHistory;

	private ArrayList<Integer> closePriceHistory;
	
	private boolean showOrderBook; //if true, orderbook is printed every step
	private boolean printOrders; //if true, orders are printed every step

	/**
	 * Constructor
	 *
	 * @param initPrices - array of int as initial prices
	 * @param initVolumes - array of int as initial volume values
	 * @param printOrderBook - boolean indicates if order book for evaluation purposes should be printed
	 * @param printOrders - boolean indicates if order book for evaluation purposes should be printed
	 */
	public ArtificialMarket(int[] initPrices, int[] initVolumes, boolean printOrderBook, boolean printOrders) {
		this.showOrderBook = printOrderBook;
		this.printOrders = printOrders;

		sellOrderBook = new ArrayList<Order>();
		buyOrderBook = new ArrayList<Order>();
		priceHistory = new ArrayList<Integer>();
		volumeHistory = new ArrayList<Integer>();
		closePriceHistory = new ArrayList<Integer>();
		
		// Copy initial values in list
		for (int i = 0; i < initPrices.length; i++) {
			priceHistory.add(initPrices[i]);
			volumeHistory.add(initVolumes[i]);
		}

	}

	/**
	 * Order driven clearing mechanism. The new price is found by
	 * largest best execution principal.
	 */
	public void clearMarket()
	{
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
		ArrayList<Order> curLevelList = new ArrayList<Order>(); // temporary list on eachlevel
		
		double minsellLimitPrice = 0;
		double maxsellLimitPrice = 0;

		//cumulate sell orders 
		for (int i = 0; i < sellOrderBook.size(); i++) {
			curOrder = sellOrderBook.get(i);

			// If Market Order; by definition at the beginning of the list
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

		//same for buy orderbook again
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
						minbuyLimitPrice = Math.min(minbuyLimitPrice, (int) curOrder.getLimitprice());
					} else {
						minbuyLimitPrice = (int) curOrder.getLimitprice();
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

			
			}

			else
			// No Market Orders
			{
				vol = 0;
				for (int i = (buyVolumePerLevelList.size() - 1); i >= 0; i--) {
					vol += buyVolumePerLevelList.get(i);
					buyCumVolPerLevelList[i] = vol;
				}
				
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

				// Maximize execution volume (-> minimize transition)
				if (maxVolume > h && h != 0) {
					maxVolume = h;
					transition = h;
					newPrice = i;
					nShares = buyLevelCumVol;
				}

				if (showOrderBook)
				// print order book
				{
					
					System.out.printf("%d \t\t %d \t\t %d \t\t %d \t\t %d \t\t %d \n  ", i, sellVol, buyVol,
							sellLevelCumVol, buyLevelCumVol, h);
				}
				// }

			}
			if (showOrderBook)
			// print order book
			{			
				System.out.println();
				System.out.println("Match: Price: " + newPrice + " with Volume " + nShares);
			}

		} 
		else 
		{

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
	
		// Execute Sell Orders (buy from seller)
		for (int i = 0; i < sellOrderBook.size(); i++) {

			long shares = 0;
			if (sellOrderBook.get(i).getVolume() < leftShares) {
				shares = sellOrderBook.get(i).getVolume();
			} else {
				// transitions
				shares = leftShares;
			}

			long money = shares * newPrice;
			sellOrderBook.get(i).getOwner().buyShareFromTrader(money, shares);
			leftShares -= shares;

			if (leftShares == 0) {
				break;
			}
		}

		// Execute buy orders (sell to buyer)
		// if there are market orders, execute first
		if (buyPriceLevels.get(0) == 0.0) {
			Iterator<Order> orders = samebuyOrders.get(0).iterator();
			while (orders.hasNext()) {
				// Execute order
				Order o = orders.next();

				long shares = o.getVolume();
				long money = shares * newPrice;

				o.getOwner().sellShareToTrader(money, shares);
			}
		}

		// Execute buy orders level by level
		for (int i = buyPriceLevels.size() - 1; i > 0; i--) {
			Iterator<Order> orders = samebuyOrders.get(i).iterator();
			while (orders.hasNext()) {
				// Execute order
				Order o = orders.next();

				long shares = o.getVolume();
				long money = shares * newPrice;

				o.getOwner().sellShareToTrader(money, shares);
			}

			if (newPrice == buyPriceLevels.get(i)) {
				break;
			}
		}

		
		//save price and clear order books
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

		if (order.getType1() == Order.BUY) {
			buyOrderBook.add(order);
		}

		else if (order.getType1() == Order.SELL) {
			sellOrderBook.add(order);
		}

		else {
		} // do nothing, order isn't allowed
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
	 * Returns the last n prices as an Array of Doubles in order is [t, t-1,
	 * t-2, .. , t-n]. If n is greater then actual price list, the whole price list
	 * will be returned.
	 * 
	 * @param n - number of prices
	 * @return array of Doubles with last n prices or null if list is empty
	 */
	public double[] getLastNPriceAsDoubles(int n) {
		if (priceHistory.isEmpty()) {
			return null;
		}

		if (n > priceHistory.size()) {
			n = priceHistory.size();
		}

		double[] lastPrices = new double[n];

		for (int i = 0; i < n; i++) {
			lastPrices[i] = (double) priceHistory.get(priceHistory.size() - (i + 1));
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
	public int[] getLastNVolume(int n) {
		if (volumeHistory.isEmpty()) {
			return null;
		}

		if (n > volumeHistory.size()) {
			n = volumeHistory.size();
		}

		int[] lastVolume = new int[n];

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
		int[] lastVol = getLastNVolume(1);

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
	 *  saves current spot price in close price history 
	 */
	public void saveClosePrice()
	{
		this.closePriceHistory.add(this.getSpotPrice());
	}

	/**
	 * Returns the last n close prices as an Array of Doubles in order [t, t-1,
	 * t-2, .. , t-n]. If n is greater then actual price list, the whole price list
	 * will be returned.
	 * 
	 * @param n - number of prices
	 * @return array of Doubles with last n prices or null if list is empty
	 */
	public double[] getLastNCloseAsDoubles(int n) {
		if (closePriceHistory.isEmpty()) {
			return null;
		}

		if (n > closePriceHistory.size()) {
			n = closePriceHistory.size();
		}

		double[] lastPrices = new double[n];

		for (int i = 0; i < n; i++) {
			lastPrices[i] = (double) closePriceHistory.get(closePriceHistory.size() - (i + 1));
		}

		return lastPrices;

	}

}
