package com.example.philanthrolink;

public class NGOHelperClass {
    String ngoName, ownerName, ownerID, email, phone, type, password;

    public NGOHelperClass(String ngoName, String ownerName, String ownerID, String email, String phone, String type, String password){
        this.ngoName = ngoName;
        this.ownerName = ownerName;
        this.ownerID = ownerID;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.password = password;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public NGOHelperClass(){

    }
}
