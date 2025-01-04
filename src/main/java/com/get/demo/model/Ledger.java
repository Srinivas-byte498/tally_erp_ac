package com.get.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ledger")
public class Ledger {
    @Id
    private String id; // Auto-generated ID for MongoDB
    private String company;
    private String name;
    private String cr;
    private String dr;

    // Constructors
    public Ledger() {}

    public Ledger(String company,String name,  String cr, String dr) {
        this.company = company;
        this.name = name;
        this.cr = cr;
        this.dr = dr;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCR() {
        return cr;
    }

    public void setCR(String cr) {
        this.cr = cr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDR() {
        return dr;
    }

    public void setDR(String dr) {
        this.dr = dr;
    }
}
