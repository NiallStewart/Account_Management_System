package com.account.SimplestCRUDExample.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Users")
public class User {
	
	@Id
	private Long id;
	private int password;
	
	public User() {
		super();
	}
	
	public User(Long id, int password) {
		super();
		this.id = id;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getPassword() {
		return password;
	}
	
	public void setPassword(int password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", password=" + password + "]";
	}

}
