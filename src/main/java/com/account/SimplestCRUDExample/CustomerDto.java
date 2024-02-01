package com.account.SimplestCRUDExample;

import java.sql.Date;
import java.time.LocalDate;

public class CustomerDto {
	private Long panCardNumber;
    private Long nationalInsuranceNumber;
    private String firstName;
    private String lastName;
    private String postalAddress;
    private String emailAddress;
    private Date dateOfBirth;
    
    public Long getPanCardNumber() {
        return panCardNumber;
    }

    public void setPanCardNumber(Long panCardNumber) {
        this.panCardNumber = panCardNumber;
    }

    public Long getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    public void setNationalInsuranceNumber(Long nationalInsuranceNumber) {
        this.nationalInsuranceNumber = nationalInsuranceNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
