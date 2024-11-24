package com.bucks.banking.services;

import java.sql.*;

public class DBUtil {
	private final static String dbUrl = "jdbc:postgresql://localhost:5432/BucksBanking";
	private final static String dbUser = "postgres";
	private final static String dbPass = "root";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dbUrl, dbUser, dbPass);
	}
 
}
