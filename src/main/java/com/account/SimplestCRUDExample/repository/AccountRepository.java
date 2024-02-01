package com.account.SimplestCRUDExample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.SimplestCRUDExample.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
