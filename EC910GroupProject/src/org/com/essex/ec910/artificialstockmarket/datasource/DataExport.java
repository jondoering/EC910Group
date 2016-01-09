package org.com.essex.ec910.artificialstockmarket.datasource;
import java.sql.*;

import jas.statistics.db.Database;
import jas.statistics.db.Table;

/**
 * @author MAO WEIGUANG
 *
 * Using SQL Query Language to  store all outcomes of the simulation data
 */
public class DataExport {
	
	//public int order;
	//public int price;


	public DataExport()  {
		
		
	}
	
	public void storeData()
	{
	
		try {
			Class.forName("com.mysql.jdbc.Driver");     
			//Class.forName("org.gjt.mm.mysql.Driver");
			System.out.println("Success loading Mysql Driver!");
		}
		catch (Exception e) {
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
		try {
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/EC910data","root","EC910");// username:root  password:EC910 
			System.out.println("Success connect Mysql server!");

			Statement stmt = connect.createStatement();
			String sql ="insert into user "   // insert value in the database
					+ "value('234', '123')";
			int m =stmt.executeUpdate(sql);


		}
		
		catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();

		}
	
	}

}




