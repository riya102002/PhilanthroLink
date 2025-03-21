package com.example.philanthrolink;

public class NGOProfileHelper {
    String TypeOfDonation, Description, Quantity;
    public NGOProfileHelper(String TypeOfDonation, String Description, String Quantity){
        this.TypeOfDonation = TypeOfDonation;
        this.Description = Description;
        this.Quantity = Quantity;
    }

    public String getTypeOfDonation() {
        return TypeOfDonation;
    }

    public void setTypeOfDonation(String typeOfDonation) {
        TypeOfDonation = typeOfDonation;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public NGOProfileHelper(){

    }
}
