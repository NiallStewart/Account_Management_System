package com.account.SimplestCRUDExample.controller;

import com.account.SimplestCRUDExample.EmailService;
import com.account.SimplestCRUDExample.model.Account;
import com.account.SimplestCRUDExample.model.Customer;
import com.account.SimplestCRUDExample.model.User;
import com.account.SimplestCRUDExample.repository.AccountRepository;
import com.account.SimplestCRUDExample.repository.CustomerRepository;
import com.account.SimplestCRUDExample.repository.UserRepository;
import com.account.exceptions.CustomerNotFoundException;
import com.account.exceptions.InsufficientFundsException;
import com.account.exceptions.InvalidDepositException;
import com.account.exceptions.InvalidWithdrawalException;
import com.account.exceptions.NoCustomersException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private EmailService emailService;

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
	private UserRepository userRepository;

    @GetMapping("/getAllCustomers")
    public ResponseEntity<Object> getAllCustomers() {
        try {
            List<Customer> customerList = new ArrayList<>();
            customerRepository.findAll().forEach(customerList::add);

            if (customerList.isEmpty()) {
                throw new NoCustomersException("There are currently no customers available");
            }
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        }
        catch (NoCustomersException e) {
        	return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
        catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id) {
    	try {
    		Optional<Customer> customerObj = customerRepository.findById(id);
            if (customerObj.isPresent()) {
                return new ResponseEntity<>(customerObj.get(), HttpStatus.OK);
            } else {
            	throw new CustomerNotFoundException("Customer with id " + id + " does not exist");
            }	
    	}
         catch (CustomerNotFoundException e) {
        	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        try {
        	Customer customerObj = customerRepository.save(customer);
        	Account accountObj = new Account(customerObj.getId(), 0);
        	accountRepository.save(accountObj);
        	
        	// Create an instance of the Random class
            Random random = new Random();

            // Generate a random 4-digit code
            int passcode = random.nextInt(10000);  
            // Generates a random integer between 0 (inclusive) and 10000 (exclusive)
            User user = new User(customerObj.getId(), passcode);
            
            userRepository.save(user);
            
            emailService.sendEmail(customerObj.getEmailAddress(), "Login Password", "Your password is " + user.getPassword());
        	
            return new ResponseEntity<>(customerObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateCustomer/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        try {
        	Optional<Customer> customerData = customerRepository.findById(id);
            if (customerData.isPresent()) {
            	Customer updatedCustomerData = customerData.get();
                updatedCustomerData.setFirstName(customer.getFirstName());
                updatedCustomerData.setLastName(customer.getLastName());
                updatedCustomerData.setDateOfBirth(customer.getDateOfBirth());
                updatedCustomerData.setEmailAddress(customer.getEmailAddress());
                updatedCustomerData.setPostalAddress(customer.getPostalAddress());

                Customer CustomerObj = customerRepository.save(updatedCustomerData);
                return new ResponseEntity<>(CustomerObj, HttpStatus.CREATED);
            } else {
            	throw new CustomerNotFoundException("Customer with id " + id + " does not exist");
            }
        }
        catch (CustomerNotFoundException e) {
        	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteCustomerById/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        try {
        	Optional<Customer> customerData = customerRepository.findById(id);
        	if(customerData.isPresent()) {
        		customerRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
        	} else {
        		throw new CustomerNotFoundException("Customer with id " + id + " does not exist");
        	}
        }
        catch (CustomerNotFoundException e) {
        	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/deleteAllCustomers")
    public ResponseEntity<HttpStatus> deleteAllCustomers() {
        try {
        	customerRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/deposit")
    public ResponseEntity<Object> depositTransaction(@RequestParam Long accountId, @RequestParam int amount) {
    	try {
    		Optional<Account> accountData = accountRepository.findById(accountId);
    		if(accountData.isPresent()) {
    			Account updatedAccountData = accountData.get();
    			if(amount < 0) {
    				throw new InvalidDepositException("Cannot deposit a negative amount");
    			}
    			updatedAccountData.setCurrentBalance(updatedAccountData.getCurrentBalance() + amount);
				Account acountObj = accountRepository.save(updatedAccountData);
				return new ResponseEntity<>(acountObj, HttpStatus.CREATED);
    		}
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	catch (InvalidDepositException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @PutMapping("/withdraw")
    public ResponseEntity<Object> withdrawTransaction(@RequestParam Long accountId, @RequestParam int amount) {
    	try {
    		Optional<Account> accountData = accountRepository.findById(accountId);
    		if(accountData.isPresent()) {
    			Account updatedAccountData = accountData.get();
    			if(updatedAccountData.getCurrentBalance() >= amount) {
    				if(amount < 0) {
    					throw new InvalidWithdrawalException("Cannot withdraw a negative amount");
    				}
    				if(amount > 10000) {
    					throw new InvalidWithdrawalException("Withdrawal amount exceeds the limit (£10,000)");
    				}
    				updatedAccountData.setCurrentBalance(updatedAccountData.getCurrentBalance() - amount);
    				Account acountObj = accountRepository.save(updatedAccountData);
    				return new ResponseEntity<>(acountObj, HttpStatus.CREATED);
    			} else {
    				throw new InsufficientFundsException("Insufficient funds for withdrawal");
    			}
    		}
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	catch (InvalidWithdrawalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	catch (InsufficientFundsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

}
