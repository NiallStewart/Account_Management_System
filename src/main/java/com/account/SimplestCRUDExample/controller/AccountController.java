package com.account.SimplestCRUDExample.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.SimplestCRUDExample.model.Account;
import com.account.SimplestCRUDExample.repository.AccountRepository;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	AccountRepository accountRepository;

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            List<Account> accountList = new ArrayList<>();
            accountRepository.findAll().forEach(accountList::add);

            if (accountList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(accountList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAccountById/{id}")
    public ResponseEntity<Account> getCustomerById(@PathVariable Long id) {
        Optional<Account> accountObj = accountRepository.findById(id);
        if (accountObj.isPresent()) {
            return new ResponseEntity<>(accountObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addAccount")
    public ResponseEntity<Account> addCustomer(@RequestBody Account account) {
        try {
        	Account accountObj = accountRepository.save(account);
            return new ResponseEntity<>(accountObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateAccount/{id}")
    public ResponseEntity<Account> updateCustomer(@PathVariable Long id, @RequestBody Account account) {
        try {
            Optional<Account> accountData = accountRepository.findById(id);
            if (accountData.isPresent()) {
            	Account updatedAccountData = accountData.get();
                updatedAccountData.setCurrentBalance(account.getCurrentBalance());
                updatedAccountData.setCustomerId(account.getCustomerId());

                Account accountObj = accountRepository.save(updatedAccountData);
                return new ResponseEntity<>(accountObj, HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAccountById/{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable Long id) {
        try {
        	accountRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/deleteAllAccounts")
    public ResponseEntity<HttpStatus> deleteAllAccounts() {
        try {
        	accountRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
