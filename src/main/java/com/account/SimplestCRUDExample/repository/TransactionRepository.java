package com.account.SimplestCRUDExample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.SimplestCRUDExample.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
