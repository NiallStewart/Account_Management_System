package com.account.SimplestCRUDExample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.SimplestCRUDExample.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
