package com.account.SimplestCRUDExample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.SimplestCRUDExample.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
