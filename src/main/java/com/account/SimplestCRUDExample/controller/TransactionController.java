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

import com.account.SimplestCRUDExample.model.Transaction;
import com.account.SimplestCRUDExample.repository.TransactionRepository;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	@Autowired
	TransactionRepository transactionRepository;

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try {
            List<Transaction> transactionList = new ArrayList<>();
            transactionRepository.findAll().forEach(transactionList::add);

            if (transactionList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(transactionList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTransactionById/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transactionObj = transactionRepository.findById(id);
        if (transactionObj.isPresent()) {
            return new ResponseEntity<>(transactionObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addTransaction")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        try {
        	Transaction transactionObj = transactionRepository.save(transaction);
            return new ResponseEntity<>(transactionObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateTransaction/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        try {
            Optional<Transaction> transactionData = transactionRepository.findById(id);
            if (transactionData.isPresent()) {
            	Transaction updatedTransactionData = transactionData.get();
            	updatedTransactionData.setDateTime(transaction.getDateTime());
                updatedTransactionData.setType(transaction.getType());
                updatedTransactionData.setSubType(transaction.getSubType());
                updatedTransactionData.setCurrentBalance(transaction.getCurrentBalance());

                Transaction transactionObj = transactionRepository.save(updatedTransactionData);
                return new ResponseEntity<>(transactionObj, HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteTransactionById/{id}")
    public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable Long id) {
        try {
        	transactionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/deleteAllTransactions")
    public ResponseEntity<HttpStatus> deleteAllTransactions() {
        try {
        	transactionRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
}
