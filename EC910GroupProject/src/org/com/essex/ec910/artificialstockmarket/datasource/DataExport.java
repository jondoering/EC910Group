package org.com.essex.ec910.artificialstockmarket.datasource;
import java.sql.*;

public class DataExport {
	public static void main(String args[]) {
	    try {
	      Class.forName("com.mysql.jdbc.Driver");     
	      //Class.forName("org.gjt.mm.mysql.Driver");
	     System.out.println("Success loading Mysql Driver!");
	    }
	    catch (Exception e) {
	      System.out.print("Error loading Mysql Driver!");
	      e.printStackTrace();
	    }
//	    try {
//	      Connection connect = DriverManager.getConnection(
//	          "jdbc:mysql://localhost:3306/EC910Data","root","EC910");
//	           
//
//	      System.out.println("Success connect Mysql server!");
//	      Statement stmt = connect.createStatement();
//	      ResultSet rs = stmt.executeQuery("select * from user");
//	                                                              //user 为你表的名称
//	while (rs.next()) {
//	        System.out.println(rs.getString("name"));
//	      }
//	    }
//	    catch (Exception e) {
//	      System.out.print("get data error!");
//	      e.printStackTrace();
//	    }
	  }
	}


