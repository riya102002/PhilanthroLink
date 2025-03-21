package com.example.philanthrolink;

public class VolunteerHelperClass {
    String name, email, idtype, idnumber, occupation, phone, password;

    public VolunteerHelperClass(String name, String email, String idtype, String idnumber, String occupation, String phone, String password){
        this.name = name;
        this.email = email;
        this.idtype = idtype;
        this.idnumber = idnumber;
        this.occupation = occupation;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public VolunteerHelperClass(){

    }
}
