package com.bucks.banking.repositories;
import java.sql.*;
import java.util.*;
import java.util.Date;

import com.bucks.banking.model.TransactionDetail;
import com.bucks.banking.model.TransactionType;
import com.bucks.banking.services.DBUtil;

public class JdbcTransactionRepositoryImpl implements TransactionRepository {

    public Long addTransaction(TransactionDetail transactionDetail) {
        // SQL Query for inserting a new transaction
        String insertQuery = "INSERT INTO transactiondetail (accountnumber, transactiondate, amount, txtype) VALUES (?, ?, ?, ?) RETURNING transactionid";
        Connection connect = null;
        PreparedStatement insertStmt = null;
        ResultSet resultSet = null;
        Long transactionId = null;

        try {
            // Get connection
            connect = DBUtil.getConnection();
            insertStmt = connect.prepareStatement(insertQuery);
            insertStmt.setLong(1, transactionDetail.getAccountNumber());
            insertStmt.setTimestamp(2, new Timestamp(transactionDetail.getTransactionDate().getTime()));
            insertStmt.setInt(3, transactionDetail.getAmount());
            insertStmt.setString(4, transactionDetail.getTxType().name());  // Assuming txType is an enum

            // Execute insert and retrieve the generated transaction ID
            resultSet = insertStmt.executeQuery();
            if (resultSet.next()) {
                transactionId = resultSet.getLong("transactionid");
            }
            System.out.println("Transaction added successfully with ID: " + transactionId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (insertStmt != null) insertStmt.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return transactionId;
    }

    public List<TransactionDetail> getAllTransactionDetailsByAccountNumber(Long accountNumber) {
        // SQL Query for fetching all transactions for an account
        String query = "SELECT transactionid, accountnumber, transactiondate, amount, txtype FROM transactiondetail WHERE accountnumber = ?";
        Connection connect = null;
        PreparedStatement checkStmt = null;
        ResultSet resultSet = null;
        List<TransactionDetail> transactionDetails = new ArrayList<TransactionDetail>();

        try {
            // Get connection
            connect = DBUtil.getConnection();
            checkStmt = connect.prepareStatement(query);
            checkStmt.setLong(1, accountNumber);
            resultSet = checkStmt.executeQuery();

            // Process result set
            while (resultSet.next()) {
                long transactionId = resultSet.getLong("transactionid");
                Date transactionDate = resultSet.getTimestamp("transactiondate");
                int amount = resultSet.getInt("amount");
                String txTypeStr = resultSet.getString("txtype");
                TransactionType txType = TransactionType.valueOf(txTypeStr);  // Assuming TransactionType is an enum

                TransactionDetail transactionDetail = new TransactionDetail(transactionId, accountNumber, transactionDate, amount, txType);
                transactionDetails.add(transactionDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (checkStmt != null) checkStmt.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return transactionDetails;
    }
}
