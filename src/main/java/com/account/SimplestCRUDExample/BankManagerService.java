package com.account.SimplestCRUDExample;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.account.SimplestCRUDExample.model.Account;
import com.account.SimplestCRUDExample.model.Customer;
import com.account.SimplestCRUDExample.model.User;
import com.account.SimplestCRUDExample.repository.AccountRepository;
import com.account.SimplestCRUDExample.repository.CustomerRepository;
import com.account.SimplestCRUDExample.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class BankManagerService {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void login(AccountManagementSystemApplication app) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bank Manager Login");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Validate the username and password
        if (validateCredentials(username, password)) {
            System.out.println("Login successful. Welcome, Bank Manager!");
            // Perform further actions after successful login
            displayMenu(app);
            
        } else {
            System.out.println("Invalid username or password. Login failed.");
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Implement the logic to validate the username and password
        // This might involve checking against a database or an authentication provider
        // Return true if the credentials are valid, false otherwise
        return true;  // Example: Always return true for demonstration purposes
    }
    
    public void displayMenu(AccountManagementSystemApplication app) {
    	Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        Long customerId;
        Customer customer;
        boolean result;

        do {
            System.out.println("Bank Manager Options:");
            System.out.println("1. Get all customers");
            System.out.println("2. Check if customer account exists");
            System.out.println("3. Get customer by id");
            System.out.println("4. Create account for a new customer");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline
            switch (choice) {
            case 1:
                // Logic for checking customer account
            	getAllCustomers();
                break;
            case 2:
                // Logic for creating account for an existing customer
            	System.out.print("Enter Customer Id: ");
            	customerId = Long.valueOf(scanner.next());
            	result = checkCustomerAccountExists(customerId);
            	if(result) {
            		System.out.println("Customer exists!");
            	} else {
            		System.out.println("Customer doesn't exist");
            	}
                break;
            case 3:
            	System.out.print("Enter Customer Id: ");
            	customerId = Long.valueOf(scanner.next());
            	if(checkCustomerAccountExists(customerId)) {
            		customer = getCustomerById(customerId);
            		System.out.println(customer);
            	} else {
            		System.out.println("Customer doesn't exist");
            	}
            	break;
            case 4:
                // Logic for creating account for an existing customer
            	System.out.println("Enter following customer details");
            	System.out.print("PanCard Number: ");
            	Long panCardNumber = Long.valueOf(scanner.next());
            	System.out.print("National Insurance Number: ");
            	Long nationalInsuranceNumber = Long.valueOf(scanner.next());
            	System.out.print("First Name: ");
            	String firstName = scanner.next();
            	System.out.print("Last Name: ");
            	String lastName = scanner.next();
            	scanner.nextLine();
            	System.out.print("Address: ");
            	String address = scanner.nextLine();
            	System.out.print("Email: ");
            	String email = scanner.next();
            	System.out.print("Date of Birth: ");
            	String dateInput = scanner.next();
            	Date dateOfBirth = Date.valueOf(LocalDate.of(2000, 10, 26));
                CustomerDto customerDto = new CustomerDto();
                customerDto.setPanCardNumber(panCardNumber);
                customerDto.setNationalInsuranceNumber(nationalInsuranceNumber);
                customerDto.setFirstName(firstName);
                customerDto.setLastName(lastName);
                customerDto.setPostalAddress(address);
                customerDto.setEmailAddress(email);
                customerDto.setDateOfBirth(dateOfBirth);
                Customer newCustomer = createCustomer(customerDto);
                // Create an instance of the Random class
                Random random = new Random();

                // Generate a random 4-digit code
                int passcode = random.nextInt(10000);  
                // Generates a random integer between 0 (inclusive) and 10000 (exclusive)
                User user = new User(newCustomer.getId(), passcode);
                userRepository.save(user);
                emailService.sendEmail(customerDto.getEmailAddress(), "Login Password", "Your password is " + user.getPassword());
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
     app.loginDisplay();
    
    }
    
    public void getAllCustomers() {
    	List<Customer> customers = customerRepository.findAll();

        if (!customers.isEmpty()) {
            for (Customer customer : customers) {
                System.out.println(customer.toString());
            }
        } else {
            System.out.println("No customers found.");
        }
    }
    
    public Customer getCustomerById(Long customer_id) {
    	Optional<Customer> customer = customerRepository.findById(customer_id);
    	if(customer.isPresent()) {
    		return customer.get();
    	}
	    return null;
    }
	
	public boolean checkCustomerAccountExists(Long customer_id) {
		Optional<Customer> customerOptional = customerRepository.findById(customer_id);
	    return customerOptional.isPresent();
	}
	
	/**
	 * Create account for an already existing customer
	 */
	public String createAccount(Long customer_id) {
		// Create account
	    Account account = new Account(customer_id, 0);
	    
	    // Save account to the account repository
	    accountRepository.save(account);
		return "Account Created";
	}

	/**
	 * Create account the initial account for a new customer
	 */
	@Transactional
	public Customer createCustomer(@RequestBody CustomerDto customerDTO) {
		Customer customer = new Customer();
        customer.setPanCardNumber(customerDTO.getPanCardNumber());
        // Set other customer attributes
        customer.setAadharNumber(customerDTO.getNationalInsuranceNumber());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPostalAddress(customerDTO.getPostalAddress());
        customer.setEmailAddress(customerDTO.getEmailAddress());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());

        // Save the customer to the database
        Customer savedCustomer = customerRepository.save(customer);
        
        Account accountObj = new Account(savedCustomer.getId(), 0);
    	accountRepository.save(accountObj);
        
        customerRepository.flush();
        
        accountRepository.flush();
        
        if (savedCustomer != null) {
            // Log a message to indicate successful data persistence
            System.out.println("Customer data saved successfully: " + savedCustomer.getId());
        } else {
            // Log a message to indicate that the data was not saved
        	System.out.println("Failed to save customer data");
        }
        return savedCustomer;
	}

}
