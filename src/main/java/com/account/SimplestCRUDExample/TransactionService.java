package com.account.SimplestCRUDExample;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.SimplestCRUDExample.model.Account;
import com.account.SimplestCRUDExample.model.Transaction;
import com.account.SimplestCRUDExample.repository.AccountRepository;
import com.account.SimplestCRUDExample.repository.CustomerRepository;
import com.account.SimplestCRUDExample.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	public String withdrawTransaction(Long accountNumber, int amount, String type, String subtype) {
		Account customerAccount = accountRepository.findById(accountNumber).get();
		if(!customerAccount.equals(null)) {
			int currentBalance = customerAccount.getCurrentBalance();
			if(currentBalance - amount >= 0) {
				customerAccount.setCurrentBalance(customerAccount.getCurrentBalance() - amount);
				
				// Save the updated account information
				accountRepository.save(customerAccount);
				
				// Create a record of the transaction
				Transaction transaction = new Transaction();
				transaction.setCurrentBalance(currentBalance);
				transaction.setDateTime(LocalDateTime.now());
				transaction.setType(type);
				transaction.setSubType(subtype);
				
				// Save the transaction information
				transactionRepository.save(transaction);
				
				return "Withdrawal Transaction Successful!";
			} else {
				return "Insufficient Funds";
			}
		} else {
			return "Invalid Account Number!";
		}
	}
	
	public String depositTransaction(Long accountNumber, int amount, String type, String subtype) {
		Account customerAccount = accountRepository.findById(accountNumber).get();
		if(!customerAccount.equals(null)) {
			customerAccount.setCurrentBalance(customerAccount.getCurrentBalance() + amount);
			
			// Save the updated account information
			accountRepository.save(customerAccount);
			
			// Create a record of the transaction
			Transaction transaction = new Transaction();
			transaction.setCurrentBalance(customerAccount.getCurrentBalance());
			transaction.setDateTime(LocalDateTime.now());
			transaction.setType(type);
			transaction.setSubType(subtype);
			
			// Save the transaction information
			transactionRepository.save(transaction);
			
			return "Deposit Transaction Successful!";
		} else {
			return "Invalid Account Number!";
		}
	}
	
	public String tranferTransaction(Long sender, Long reciever, int amount, String type, String subtype) {
		Account senderAccount = accountRepository.findById(sender).get();
		Account recieverAccount = accountRepository.findById(reciever).get();
		
		if(!senderAccount.equals(null) && !recieverAccount.equals(null)) {
			withdrawTransaction(sender, amount, type, subtype);
			depositTransaction(reciever, amount, type, subtype);
			
			return "Transfer Transaction Successful!";
		} else {
			return "Invalid Account Number(s)";
		}
	}

}
