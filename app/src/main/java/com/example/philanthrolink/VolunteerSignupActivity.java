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

public class VolunteerSignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupIDType, signupIDNumber, signupOccupation, signupPhone, signupPassword;
    TextView loginRedirectText;
    Button signUpButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    String name, email, idtype, idnumber, occupation, phone, password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupIDType = findViewById(R.id.signup_idType);
        signupIDNumber = findViewById(R.id.signup_idnum);
        signupOccupation = findViewById(R.id.signup_occupation);
        signupPhone = findViewById(R.id.signup_phone);
        signupPassword = findViewById(R.id.signup_pass);
        signUpButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validEmail() | !validName() | !validIDType() | !validIDNumber() | !validOccupation() | !validPhone() | !validPassword()){

                }else{
                    setData();
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerSignupActivity.this, VolunteerLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setData(){
        database = database = FirebaseDatabase.getInstance();
        reference = database.getReference("Volunteers");

        name = signupName.getText().toString().trim();
        email = signupEmail.getText().toString().trim();
        idtype = signupIDType.getText().toString().trim();
        idnumber = signupIDNumber.getText().toString().trim();
        occupation = signupOccupation.getText().toString().trim();
        phone = signupPhone.getText().toString().trim();
        password = signupPassword.getText().toString().trim();

        try{
            String allData = "Name:"+name+" Email:"+email+" IDType:"+idtype
                    +" IDNumber:"+idnumber+" Occupation: "+occupation+" Phone:"+phone+" Pass:"+password;
            QRGEncoder qrgEncoder = new QRGEncoder(allData, null, QRGContents.Type.TEXT, 1000);
            Bitmap qrBits = qrgEncoder.getBitmap();

            VolunteerHelperClass volunteerHelperClass = new VolunteerHelperClass(name, email, idtype, idnumber, occupation, phone, password);
            reference.child(idnumber).setValue(volunteerHelperClass);

            Toast.makeText(this, "Welcome to (PhilanthroVolunteer), PhylanthroLink!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VolunteerSignupActivity.this, VolunteerLoginActivity.class);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validName(){
        String val = signupName.getText().toString().trim();
        if(val.isEmpty()){
            signupName.setError("Name Cannot be Empty");
            return false;
        }
        else {
            boolean x = ((val != null) && (!val.equals(""))
                    && (val.matches("^[a-zA-Z ]*$")));
            if(x==false){
                signupName.setError("Name can only have Alphabets");
                return false;
            }
            signupName.setError(null);
            return true;
        }
    }

    public boolean validEmail(){
        String val = signupEmail.getText().toString().trim();
        if(val.isEmpty()){
            signupEmail.setError("Email cannot be empty");
            return false;
        }
        else {
            if(val.endsWith("@gmail.com")) {
                signupEmail.setError(null);
                return true;
            }
            signupEmail.setError("Email does not end with '@gmail.com'");
            return false;
        }
    }

    public boolean validIDType(){
        String val = signupIDType.getText().toString().trim();
        if(val.isEmpty()){
            signupIDType.setError("ID Type cannot be empty");
            return false;
        }
        else {
            boolean x = ((val != null) && (!val.equals(""))
                    && (val.matches("^[a-zA-Z ]*$")));
            if(x==false){
                signupIDType.setError("ID Type can only have Alphabets");
                return false;
            }
            signupIDType.setError(null);
            return true;
        }
    }

    public boolean validIDNumber(){
        String val = signupIDNumber.getText().toString().trim();
        if(val.isEmpty()){
            signupIDNumber.setError("ID Number cannot be empty");
            return false;
        }
        else {
            boolean x = ((val != null) && (!val.equals(""))
                    && (val.matches("^[a-zA-Z0-9]*$")));
            if(x==false){
                signupIDNumber.setError("ID Number can only have Alphanumeric");
                return false;
            }
            signupIDNumber.setError(null);
            return true;
        }
    }

    public boolean validOccupation(){
        String val = signupOccupation.getText().toString().trim();
        if(val.isEmpty()){
            signupOccupation.setError("Occupation cannot be empty");
            return false;
        }
        else {
            boolean x = ((val != null) && (!val.equals(""))
                    && (val.matches("^[a-zA-Z]*$")));
            if(x==false){
                signupOccupation.setError("Occupation can only have Alphabets");
                return false;
            }
            signupOccupation.setError(null);
            return true;
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

    public boolean validPassword(){
        String val = signupPassword.getText().toString().trim();
        if(val.isEmpty()){
            signupPassword.setError("Password cannot be empty");
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
}