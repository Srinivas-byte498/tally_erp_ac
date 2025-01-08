package com.get.erpintegration.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "requests")
public class Request {

    @Id
    private String id;

    private String reqdata;
    private String companyid;  
    private LocalDateTime createddt; 
    private String status;

    // Constructors
    public Request() {
    }

    public Request(String status, String reqdata, String companyid, LocalDateTime createddt) {
        this.status = status;
        this.reqdata = reqdata;
        this.companyid = companyid;
        this.createddt = createddt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReqData() {
        return reqdata;
    }

    public void setReqData(String reqdata) {
        this.reqdata = reqdata;
    }

    public String getCompanyId() {
        return companyid;
    }

    public void setCompanyId(String companyid) {
        this.companyid = companyid;
    }

    public LocalDateTime getReqDtt() {
        return createddt;
    }

    public void setReqDtt(LocalDateTime createddt) {
        this.createddt = createddt;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", reqdata='" + reqdata + '\'' +
                ", companyid='" + companyid + '\'' +
                ", createddt=" + createddt +
                '}';
    }
}