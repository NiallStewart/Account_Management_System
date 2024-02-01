package com.account.SimplestCRUDExample.controller;

import com.account.SimplestCRUDExample.EmailService;
import com.account.SimplestCRUDExample.model.Account;
import com.account.SimplestCRUDExample.model.Customer;
import com.account.SimplestCRUDExample.model.User;
import com.account.SimplestCRUDExample.repository.AccountRepository;
import com.account.SimplestCRUDExample.repository.CustomerRepository;
import com.account.SimplestCRUDExample.repository.UserRepository;

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
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customerList = new ArrayList<>();
            customerRepository.findAll().forEach(customerList::add);

            if (customerList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customerObj = customerRepository.findById(id);
        if (customerObj.isPresent()) {
            return new ResponseEntity<>(customerObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        try {
            Optional<Customer> bookData = customerRepository.findById(id);
            if (bookData.isPresent()) {
            	Customer updatedCustomerData = bookData.get();
                updatedCustomerData.setFirstName(customer.getFirstName());
                updatedCustomerData.setLastName(customer.getLastName());
                updatedCustomerData.setDateOfBirth(customer.getDateOfBirth());
                updatedCustomerData.setEmailAddress(customer.getEmailAddress());
                updatedCustomerData.setPostalAddress(customer.getPostalAddress());

                Customer bookObj = customerRepository.save(updatedCustomerData);
                return new ResponseEntity<>(bookObj, HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteCustomerById/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable Long id) {
        try {
        	customerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
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
    
    @PostMapping("/deposit")
    public ResponseEntity<HttpStatus> depositTransaction(@RequestParam int amount) {
    	
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdrawTransaction(@RequestParam int amount) {
    	
    }

}
