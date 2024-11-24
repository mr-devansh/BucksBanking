package com.bucks.banking.repositories;

import java.sql.*;
import java.util.*;
import com.bucks.banking.model.Account;
import com.bucks.banking.model.Reward;
import com.bucks.banking.services.DBUtil;

public class JdbcRewardRepositoryImpl implements RewardRepository{

	public void addReward(Reward reward) {
		// TODO Auto-generated method stub
		   // SQL Queries
        String insertQuery = "INSERT INTO reward (accountnumber, rewardamount) VALUES (?, ?)";
        Connection connect = null;
		PreparedStatement checkStmt = null;
		Account account = null;
        try {
        	connect = DBUtil.getConnection();
            PreparedStatement insertStmt = connect.prepareStatement(insertQuery);
            insertStmt.setLong(1, reward.getAccountNumber());
            insertStmt.setInt(2, reward.getRewardAmount());
            insertStmt.executeUpdate();
            System.out.println("Reward inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection issues or query execution errors here
        }
    }
	

	public int getTotalRewardAmount(Long accountNumber) {
	    String query = "SELECT SUM(rewardamount) FROM reward WHERE accountnumber = ?";
	    Connection connect = null;
	    int sum = 0;
		PreparedStatement checkStmt = null;
		Account account = null;
		
		try {
			connect = DBUtil.getConnection();
			checkStmt = connect.prepareStatement(query); 
	        checkStmt.setLong(1, accountNumber);
	        ResultSet resultSet = checkStmt.executeQuery();
		    if (resultSet.next()) {
		    	sum = resultSet.getInt(1); // Return the sum of reward amounts
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sum;
	}


	public List<Reward> getAllRewardsForAccount(Long accountNumber) {
		// TODO Auto-generated method stub
		String query = "SELECT rewardconfirmationnumber, rewardamount FROM reward WHERE accountnumber = ?";
	    Connection connect = null;
	    List<Reward> ans = new ArrayList<Reward>();
		PreparedStatement checkStmt = null;
		Account account = null;
		
		try {
			connect = DBUtil.getConnection();
			checkStmt = connect.prepareStatement(query); 
	        checkStmt.setLong(1, accountNumber);
	        ResultSet resultSet = checkStmt.executeQuery();
		    while(resultSet.next()) {
		    	ans.add(new Reward(resultSet.getLong("rewardconfirmationnumber"), resultSet.getInt("rewardamount"), accountNumber)); // Return the sum of reward amounts
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
	
}
