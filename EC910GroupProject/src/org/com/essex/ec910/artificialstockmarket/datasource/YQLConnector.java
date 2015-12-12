package org.com.essex.ec910.artificialstockmarket.datasource;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Jonathan
 *
 *Using Yahoo Query Language to get historical price data from Yahoo database
 *Further information see: https://developer.yahoo.com/java/, https://developer.yahoo.com/yql/guide/yql-builder.html
 */
public class YQLConnector {


	private String symbol;
	private String fields  = "Date,Volume,Adj_Close ";
	
	private ArrayList<HistoricalPrice> histPrices;

	public YQLConnector(String symbol) {
				
		this.symbol = symbol;	
		histPrices = new ArrayList<HistoricalPrice>();
		
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
	 * @return
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
	public void addData( String startDate, String endDate) throws Exception
	{

		 String querry = "http://query.yahooapis.com/v1/public/yql?"
		 			   + "q=select%20" + fields + "%20from%20yahoo.finance.historicaldata%20where%20symbol%20in%20"
		 			   + "(%22"+symbol+"%22)%20and%20startDate%3D%22"+ startDate + "%22%20and%20endDate%3D%22" + endDate
		 			   + "%22%0A%09%09&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env";
		 
		//get data as XML
		//Example taken and adjusted from: https://developer.yahoo.com/java/howto-parseRestJava.html
		
		Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(querry);				
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		//Get all search Result nodes
		NodeList nodes = (NodeList)xPath.evaluate("/query/results/quote", response, XPathConstants.NODESET);

		//iterate over search Result nodes, Results in descending order
		for (int i = nodes.getLength()-1;i>=0; i--) {

		  //Get each xpath expression as a string
		  String date = (String)xPath.evaluate("Date", nodes.item(i), XPathConstants.STRING);
		  double volume = (double)xPath.evaluate("Volume", nodes.item(i), XPathConstants.NUMBER);
		  double price = (double)xPath.evaluate("Adj_Close", nodes.item(i), XPathConstants.NUMBER);

		  //Add price to list
		  histPrices.add(new HistoricalPrice(date, price, volume));
		  
		  //System.out.println(new HistoricalPrice(date, price, volume));
		}
		
	}
	
	
}
