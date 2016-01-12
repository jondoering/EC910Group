package org.com.essex.ec910.artificialstockmarket.datasource;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *Class used to fetch financial data from an Internet data base (Yahoo finance). It used
 *Yahoo Query Language to get historical price data.
 *For Further information see: https://developer.yahoo.com/java/, https://developer.yahoo.com/yql/guide/yql-builder.html
 * 
 * @author Jonathan Doering
 */
public class YQLConnector {


	private String symbol;
	private String fields  = "Date,Volume,Adj_Close ";
	private ArrayList<HistoricalPrice> histPrices;

	/**
	 * Constructor
	 * @param yahoo_ticker - stock ticker used for fetching data
	 */
	public YQLConnector(String yahoo_ticker) {
				
		this.symbol = yahoo_ticker;	
		histPrices = new ArrayList<HistoricalPrice>();
		
	}
	
	/**
	 * @return ArrayList with historical prices
	 */
	public ArrayList<HistoricalPrice> getAllPrices()
	{
		return histPrices;
	}
	
	/**
	 * @return length of ArrayList with historical prices
	 */
	public int getNumOfPrices()
	{
		return histPrices.size();		
	}
	
	/**
	 * 
	 * @param n the  n-th historical price from ArrayList
	 * @return - Historical Price Object
	 */
	public HistoricalPrice getPrice(int n)
	{
		if(histPrices.isEmpty())
		{	return null;}
		else	
		{
			if(n>histPrices.size())		
			{	n = histPrices.size();}
		
			return histPrices.get(n);}
		
	}
	/**
	 * Fetchs data between specific dates and adds them to the queue.
	 * Mainly an adapted and tailored version of tutorial form yahoo finance. 
	 * See: https://developer.yahoo.com/java/howto-parseRestJava.html
	 * @param startDate  - start date of data
	 * @param endDate    - end date of data
	 * @throws Exception
	 */
	public void addData( String startDate, String endDate) throws Exception
	{
		
		 String querry = "http://query.yahooapis.com/v1/public/yql?"
		 			   + "q=select%20" + fields + "%20from%20yahoo.finance.historicaldata%20where%20symbol%20in%20"
		 			   + "(%22"+symbol+"%22)%20and%20startDate%3D%22"+ startDate + "%22%20and%20endDate%3D%22" + endDate
		 			   + "%22%0A%09%09&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env";
		 		 
		//Example taken and adjusted from: https://developer.yahoo.com/java/howto-parseRestJava.html
		Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(querry);				
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList)xPath.evaluate("/query/results/quote", response, XPathConstants.NODESET);

		//iterate over search Result nodes, Results in descending order
		for (int i = nodes.getLength()-1;i>=0; i--) {

		  //Get each xpath expression as a string
		  String date = (String)xPath.evaluate("Date", nodes.item(i), XPathConstants.STRING);
		  double volume = (double)xPath.evaluate("Volume", nodes.item(i), XPathConstants.NUMBER);
		  double price = (double)xPath.evaluate("Adj_Close", nodes.item(i), XPathConstants.NUMBER);

		  //Add price to list
		  histPrices.add(new HistoricalPrice(date, price, volume));
		  
		}
		
	}
	
	
}
