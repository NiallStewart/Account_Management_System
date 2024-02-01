package com.account.SimplestCRUDExample;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import com.account.SimplestCRUDExample.model.Customer;
import com.account.SimplestCRUDExample.repository.CustomerRepository;

@SpringBootApplication
public class AccountManagementSystemApplication {
	
//	@Autowired
//	private EmailService emailService;
	
	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private BankManagerService bankManagerService;
	
	@Autowired
    private CustomerService customerService;

	public static void main(String[] args) {
		SpringApplication.run(AccountManagementSystemApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void sendMail() {
////		emailService.sendEmail("jess1736@hotmail.co.uk", "Test Subject", "This is a test email message");
//		emailService.sendEmail("stewartniall920@gmail.com", "Test Subject", "This is a test email message");
//	}
	
	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void loginDisplay() {
	   Scanner scanner = new Scanner(System.in);
	   System.out.println("Select a login option: ");
	   System.out.println("1 - Login as bank manager");
	   System.out.println("2 - Login as customer");
	   int choice = scanner.nextInt();
	   switch(choice) {
	   case 1:
		   performManagerLogin();
		   break;
	   case 2:
		   performCustomerLogin();
		   break;
	   default:
		   System.out.println("Invalid input! Enter a valid choice");
	   }
	}

	public void performManagerLogin() {
	   bankManagerService.login(this);
	}

	public void performCustomerLogin() {
	   customerService.login();
	}

}
