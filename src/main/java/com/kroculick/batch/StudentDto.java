package com.kroculick.batch;

public class StudentDto {

    private String emailAddress;
    private String name;
    private String purchasePackage;

    public StudentDto() {}

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getName() {
        return name;
    }

    public String getPurchasePackage() {
        return purchasePackage;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPurchasePackage(String purchasePackage) {
        this.purchasePackage = purchasePackage;
    }
}
