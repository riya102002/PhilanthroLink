package com.example.philanthrolink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class NGOSignupActivity extends AppCompatActivity {

    EditText signupName, signupOwnerName, signupOwnerID, signupEmail, signupPhone, signupType, signupPassword;
    TextView ngoLoginRedirectText;
    Button ngoSignupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    String name, ownerName, ownerID, email, phone, type, password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngosignup);

        signupName = findViewById(R.id.ngo_signup_name);
        signupOwnerID = findViewById(R.id.ngo_signup_Id_Num);
        signupOwnerName = findViewById(R.id.ngo_signup_owner_name);
        signupEmail = findViewById(R.id.ngo_signup_email);
        signupPhone = findViewById(R.id.ngo_signup_phone);
        signupType = findViewById(R.id.ngo_signup_ngotype);
        signupPassword = findViewById(R.id.ngo_signup_pass);
        ngoLoginRedirectText = findViewById(R.id.ngo_loginRedirectText);
        ngoSignupButton = findViewById(R.id.ngo_signup_button);

        ngoSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validNgoName() | !validName() | !validIdnum() | !validEmail()| !validPhone() | !validType() | !validPassword()){

                }else{
                    setData();
                }
            }
        });

        ngoLoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NGOSignupActivity.this, NGOLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setData(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("NGOs");

        name = signupName.getText().toString().trim();
        ownerName = signupOwnerName.getText().toString().trim();
        ownerID = signupOwnerID.getText().toString().trim();
        email = signupEmail.getText().toString().trim();
        phone = signupPhone.getText().toString().trim();
        password = signupPassword.getText().toString().trim();
        type = signupType.getText().toString().trim();

        String allData = "NGOName:"+name+" OwnerName:"+ownerName+
                " OwnerID:"+ownerID+" Email:"+email+" Phone:"+phone+
                " NGOType:"+type+" Password:"+password;

        QRGEncoder qrgEncoder = new QRGEncoder(allData, null, QRGContents.Type.TEXT, 1000);
        Bitmap qrBits = qrgEncoder.getBitmap();

        NGOHelperClass ngoHelperClass = new NGOHelperClass(name, ownerName, ownerID,
                email, phone, type, password);
        reference.child(ownerID).setValue(ngoHelperClass);

        Toast.makeText(this, "Welcome to (PhilanthroNGO), PhilanthroLink!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NGOSignupActivity.this, NGOLoginActivity.class);
        startActivity(intent);
    }

    public boolean validNgoName(){
        String val = signupName.getText().toString().trim();
        if(val.isEmpty()){
            signupName.setError("Name Cannot be Empty");
            return false;
        }
        else {
            boolean x = ((val != null) && (!val.equals(""))
                    && (val.matches("^[a-zA-Z ]*$")));
            if(x==false){
                signupName.setError("NGO Name can only have Alphabets");
                return false;
            }
            signupName.setError(null);
            return true;
        }
    }

    public boolean validName(){
        String val = signupOwnerName.getText().toString().trim();
        if(val.isEmpty()){
            signupOwnerName.setError("Name Cannot be Empty");
            return false;
        }
        else {
            boolean x = ((val != null) && (!val.equals(""))
                    && (val.matches("^[a-zA-Z ]*$")));
            if(x==false){
                signupOwnerName.setError("Owner Name can only have Alphabets");
                return false;
            }
            signupOwnerName.setError(null);
            return true;
        }
    }

    public boolean validEmail(){
        String val = signupEmail.getText().toString().trim();
        if(val.isEmpty()){
            signupEmail.setError("Email Cannot be Empty");
            return false;
        }
        else {
            if(val.contains("@")) {
                signupEmail.setError(null);
                return true;
            }
            signupEmail.setError("Email does not contain '@' symbol");
            return false;
        }
    }

    public boolean validIdnum(){
        String val = signupOwnerID.getText().toString().trim();
        if(val.isEmpty()){
            signupOwnerID.setError("ID Number Cannot be Empty");
            return false;
        }
        else {
            boolean x = ((val != null) && (!val.equals(""))
                    && (val.matches("^[a-zA-Z0-9]*$")));
            if(x==false){
                signupOwnerID.setError("NGO ID Number can only have Alphanumeric");
                return false;
            }
            signupOwnerID.setError(null);
            return true;
        }
    }

    public boolean validPassword(){
        String val = signupPassword.getText().toString().trim();
        if(val.isEmpty()){
            signupPassword.setError("Password Cannot be Empty");
            return false;
        }
        else {
            if(val.length()>=8) {
                signupPassword.setError(null);
                return true;
            }
            signupPassword.setError("Password should be more than or equal to 8 characters long");
            return false;
        }
    }

    public boolean validPhone(){
        String val = signupPhone.getText().toString().trim();
        if(val.isEmpty()){
            signupPhone.setError("Phone Number Cannot be Empty");
            return false;
        }
        else {
            if(val.length()==10) {
                signupPhone.setError(null);
                return true;
            }
            signupPhone.setError("Invalid Phone Number");
            return false;
        }
    }

    public boolean validType(){
        String val = signupType.getText().toString().trim();
        if(val.isEmpty()){
            signupType.setError("Business Type Cannot be Empty");
            return false;
        }
        else {
            signupType.setError(null);
            return true;
        }
    }
}