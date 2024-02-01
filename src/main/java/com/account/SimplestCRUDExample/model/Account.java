package com.account.SimplestCRUDExample.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="Accounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ACCOUNT_NUMBER_GENERATOR")
	@SequenceGenerator(name = "ACCOUNT_NUMBER_GENERATOR", allocationSize = 1, initialValue = 1000000000)
	private Long accountNumber;
	private Long customerId;
	private int currentBalance;
	
	public Account() {
		super();
	}
	
	public Account(Long customerId, int currentBalance) {
		super();
		this.customerId = customerId;
		this.currentBalance = currentBalance;
	}
	
	public Long getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public Long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public int getCurrentBalance() {
		return currentBalance;
	}
	
	public void setCurrentBalance(int currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", customerId=" + customerId + ", currentBalance="
				+ currentBalance + "]";
	}
	
	

}
