package com.get.erpintegration.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companies")
public class Company {
    @Id
    private String id; // Auto-generated ID for MongoDB
    private String name;
    private String phoneNumber;
    private String email;
    private String website;

    // Constructors
    public Company() {}

    public Company(String name, String phoneNumber, String email, String website) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
