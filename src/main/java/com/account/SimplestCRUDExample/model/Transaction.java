package com.account.SimplestCRUDExample.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Transactions")
public class Transaction {
	
	@Id
	@GeneratedValue
	private Long id;
	private static Long nextReferenceNo = 101L;
	private Long referenceNo;
	private LocalDateTime dateTime;
	private String type;
	private String subType;
	private int currentBalance;
	
	public Transaction() {
		super();
	}
	
	public Transaction(LocalDateTime dateTime, String type, String subType, int currentBalance) {
		super();
		this.referenceNo = this.nextReferenceNo;
		this.nextReferenceNo++;
		this.dateTime = dateTime;
		this.type = type;
		this.subType = subType;
		this.currentBalance = currentBalance;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getReferenceNo() {
		return referenceNo;
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSubType() {
		return subType;
	}
	
	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	public int getCurrentBalance() {
		return currentBalance;
	}
	
	public void setCurrentBalance(int currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", referenceNo=" + referenceNo + ", dateTime=" + dateTime + ", type=" + type
				+ ", subType=" + subType + ", currentBalance=" + currentBalance + "]";
	}
	
}
