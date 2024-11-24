package com.bucks.banking.repositories;

import java.math.*;
import java.sql.*;
import java.util.*;

import com.bucks.banking.model.Account;
import com.bucks.banking.model.Address;
import com.bucks.banking.model.Beneficiary;
import com.bucks.banking.services.DBUtil;

public class JdbcAccountRepositoryImpl implements AccountRepository{

	public Account findAccountByNumber(Long accountNumber) {
		// TODO Auto-generated method stub
		Connection connect = null;
		PreparedStatement statement = null;
		Account account = null;
		try {
			connect = DBUtil.getConnection();
			String findAccountQuery = "SELECT * from account where accountnumber=?";
			statement = connect.prepareStatement(findAccountQuery);
			statement.setLong(1, accountNumber);
			ResultSet ans = statement.executeQuery();
			if(ans.next()) {
				account =  mapToAccount(ans);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return account;
	}

	public List<Account> findAllAccounts() {
		// TODO Auto-generated method stub
		Connection connect = null;
		PreparedStatement statement = null;
		List<Account> accounts = new ArrayList<Account>();
		try {
			connect = DBUtil.getConnection();
			String findAccountQuery = "SELECT * from account";
			statement = connect.prepareStatement(findAccountQuery);
			ResultSet ans = statement.executeQuery();
			while(ans.next()) {
				accounts.add(mapToAccount(ans));
			}		

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accounts;
	}

	public long save(Account account){
		Connection connect = null;
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;
		try {


			Account account1 = new Account();
			Address address = new Address();
			Beneficiary beneficiary = new Beneficiary();
			connect = DBUtil.getConnection();
			String insertAccountQuery = "INSERT into account(name,isactive,city,country, balance, emailaddress) values(?,?,?,?,?,?)";
			String insertBeneficiaryQuery = "INSERT into Beneficiaries(name, accountnumber) values(?,?)";
			statement = connect.prepareStatement(insertAccountQuery);
			statement2 = connect.prepareStatement(insertBeneficiaryQuery);

			//statement.setLong(1, account.getAccountNumber());
			statement.setString(1, account.getName());
			statement.setBoolean(2, account.isActive());
			statement.setString(3, account.getAddress().getCity());
			statement.setString(4, account.getAddress().getCountry());
			statement.setInt(5, account.getBalance());
			statement.setString(6, account.getEmailAddress());

			int resultSet = statement.executeUpdate();

			for(Beneficiary e : account.getBeneficiaries()) {
				//statement2.setLong(1, e.getSsn());
				statement2.setString(1, e.getName());
				statement2.setLong(2, account.getAccountNumber());
				statement2.executeUpdate();
			}
//			System.out.println(resultSet.getLong("accountnumber"));
				return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error occured while creating new account");
		}
		return 0;
	}

	public void update(Account account, Connection connection) {
		PreparedStatement statement = null;
		try {
			String updateAccountQuery = "UPDATE account set name=?, isactive=?, city=?, country=?, balance=?, emailaddress=? where accountnumber=?";
			statement = connection.prepareStatement(updateAccountQuery);

			statement.setString(1, account.getName());
			statement.setBoolean(2, account.isActive());
			statement.setString(3, account.getAddress().getCity());
			statement.setString(4, account.getAddress().getCountry());
			statement.setInt(5, account.getBalance());
			statement.setString(6, account.getEmailAddress());
			statement.setLong(7, account.getAccountNumber());

			int ans = statement.executeUpdate();  // Execute the update

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public void delete(Account account) {
		Connection connect = null;
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;

		try {
			connect = DBUtil.getConnection();

			// First, delete the beneficiaries for the account
			String deleteBeneficiaryQuery = "DELETE FROM beneficiaries WHERE accountnumber = ?";
			statement = connect.prepareStatement(deleteBeneficiaryQuery);
			statement.setLong(1, account.getAccountNumber());
			statement.executeUpdate();

			// Now delete the account
			String deleteAccountQuery = "DELETE FROM account WHERE accountnumber = ?";
			statement2 = connect.prepareStatement(deleteAccountQuery);
			statement2.setLong(1, account.getAccountNumber());
			int ans = statement2.executeUpdate();
			if(ans!=0) {
				System.out.println(account.getName()+" deleted!");
			}else {
				System.out.println("Some problem occured while deleting the account");
			}

		} catch (SQLException e) {
			// Handle the exception (you can log it or rethrow a custom exception)
			e.printStackTrace();
		} finally {
			// Close resources (like connection and statements) in the finally block
			try {
				if (statement != null) {
					statement.close();
				}
				if (statement2 != null) {
					statement2.close();
				}
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private Account mapToAccount(ResultSet resultSet) throws SQLException {
		Connection connect = null;
		PreparedStatement statement = null;
		Account account = new Account();
		Address address = new Address();
		Beneficiary beneficiary = new Beneficiary();
		account.setAccountNumber(resultSet.getInt("accountNumber"));
		account.setName(resultSet.getString("name"));
		account.setActive(resultSet.getBoolean("isactive"));
		address.setCity(resultSet.getString("city"));
		address.setCountry(resultSet.getString("country"));
		account.setBalance(resultSet.getInt("balance"));
		account.setEmailAddress(resultSet.getString("emailaddress"));
		connect = DBUtil.getConnection();
		String findAccountQuery = "SELECT * from beneficiaries where accountnumber=?";
		statement = connect.prepareStatement(findAccountQuery);
		statement.setInt(1, resultSet.getInt("accountnumber"));
		ResultSet ans = statement.executeQuery();
		Set<Beneficiary> beneficiaries = new HashSet<Beneficiary>();
		while (ans.next()) {
			beneficiaries.add(new Beneficiary(ans.getLong("ssn"), ans.getString("name")));
		}
		account.setAddress(address);
		account.setBeneficiaries(beneficiaries);
		return account;
	}
}
