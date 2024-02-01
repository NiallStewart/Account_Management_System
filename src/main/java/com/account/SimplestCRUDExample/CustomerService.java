package com.account.SimplestCRUDExample;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.SimplestCRUDExample.model.Account;
import com.account.SimplestCRUDExample.model.Customer;
import com.account.SimplestCRUDExample.model.User;
import com.account.SimplestCRUDExample.repository.AccountRepository;
import com.account.SimplestCRUDExample.repository.CustomerRepository;
import com.account.SimplestCRUDExample.repository.UserRepository;

@Service
@Transactional
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private Long customerId;
	
	public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Customer Login");
        System.out.print("Customer Id: ");
        Long customerid = Long.valueOf(scanner.next());
        System.out.print("Password: ");
        int password = scanner.nextInt();

        // Validate the username and password
        if (validateCredentials(customerid, password)) {
        	String customerName = customerRepository.findById(customerid).get().getFirstName() + " " + customerRepository.findById(customerid).get().getLastName();
            System.out.println("Login successful. Welcome, " + customerName);
            this.customerId = customerid;
            // Perform further actions after successful login
            displayMenu();
            
        } else {
            System.out.println("Invalid username or password. Login failed.");
        }
    }

    private boolean validateCredentials(Long customer_id, int password) {
    	Optional<User> user = userRepository.findById(customer_id);
    	if(user.isPresent()) {
    		if(user.get().getPassword() == password) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void displayMenu() {
    	Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        Long customerId;
        Customer customer;
        boolean result;
        int amount;

        do {
            System.out.println("Customer Options:");
            System.out.println("1. View Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. View Accounts");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline
            switch (choice) {
            case 1:
                // Logic for checking customer account
            	viewBalance();
                break;
            case 2:
                // Logic for creating account for an existing customer
            	System.out.print("How much would you like to withdraw: ");
            	amount = scanner.nextInt();
            	withdraw(amount);
                break;
            case 3:
            	System.out.print("How much would you like to deposit: ");
            	amount = scanner.nextInt();
            	deposit(amount);
            	break;
            case 4:
                // Logic for creating account for an existing customer
            	System.out.println("Viewing Accounts...");
                break;
            case 5:
                // Exiting options
            	System.out.println("Exiting...");
                exit = true;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    } while (!exit);
     // After the loop, show the login screen again
     login();
    
    }
    
    public void viewBalance() {
    	List<Account> accounts = accountRepository.findAll();
        boolean accountFound = false;
        for (Account account : accounts) {
            if (account.getCustomerId().equals(this.customerId)) {
                System.out.println("Balance for customer " + customerId + ": £" + account.getCurrentBalance());
                accountFound = true;
                break;  // Once the account is found, exit the loop
            }
        }
        if (!accountFound) {
            System.out.println("No account found for customer " + customerId);
        }
    }
    
    public void deposit(int amount) {
    	List<Account> accounts = accountRepository.findAll();
        boolean accountFound = false;
        for (Account account : accounts) {
        	if (account.getCustomerId().equals(this.customerId)) {
        		if(amount >= 0) {
        			account.setCurrentBalance(account.getCurrentBalance() + amount);
                	accountRepository.save(account);
                	accountFound = true;	
        		} else {
        			System.out.println("Can't deposit a negative value!");
        		}
        	}
        }
        if (!accountFound) {
            System.out.println("No account found for customer " + customerId);
        }
    }
    
    public void withdraw(int amount) {
    	List<Account> accounts = accountRepository.findAll();
        boolean accountFound = false;
        for (Account account : accounts) {
        	if (account.getCustomerId().equals(this.customerId)) {
        		if(amount <= 10000) {
        			if(account.getCurrentBalance() >= amount) {
            			if(amount >= 0) {
            				account.setCurrentBalance(account.getCurrentBalance() - amount);
                        	accountRepository.save(account);
                        	accountFound = true;
            			} else {
            				System.out.println("Cannot withdraw a negative number");
            			}
            		} else {
            			System.out.println("Insufficent funds");
            		}	
        		} else {
        			System.out.println("Cannot withdraw more than £10,000");
        		}
        	}
        }
        if (!accountFound) {
            System.out.println("No account found for customer " + customerId);
        }
    }
}
