package com.example.philanthrolink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class VolunteerLoginActivity extends AppCompatActivity {

    EditText volunteerLoginIdNumber, voluneerLoginPassword;
    Button volunteerLoginButton;
    TextView volunteerSignupRedirectText, volunteerForgotPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_login);

        volunteerLoginIdNumber = findViewById(R.id.volunteer_login_idnum);
        voluneerLoginPassword = findViewById(R.id.volunteer_login_pass);
        volunteerLoginButton = findViewById(R.id.volunteer_login_button);
        volunteerForgotPasswordText = findViewById(R.id.volunteer_forgotPasswordRedirectText);
        volunteerSignupRedirectText = findViewById(R.id.volunteer_signupRedirectText);

        volunteerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateIdNumber() | !validatePassword()){

                }else{
                    checkUser();
                }
            }
        });

        volunteerSignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerLoginActivity.this, VolunteerSignupActivity.class);
                startActivity(intent);
            }
        });

        volunteerForgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerLoginActivity.this, VolunteerForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateIdNumber(){
        String val = volunteerLoginIdNumber.getText().toString().trim();
        if(val.isEmpty()){
            volunteerLoginIdNumber.setError("Username cannot be empty");
            return false;
        }
        else {
            volunteerLoginIdNumber.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = voluneerLoginPassword.getText().toString().trim();
        if(val.isEmpty()){
            voluneerLoginPassword.setError("Password cannot be empty");
            return false;
        }
        else {
            voluneerLoginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userIdnumber = volunteerLoginIdNumber.getText().toString().trim();
        String userPassword = voluneerLoginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Volunteers");
        Query checkUserDatabase = reference.orderByChild("idnumber").equalTo(userIdnumber);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    volunteerLoginIdNumber.setError(null);
                    String passwordFromDB = snapshot.child(userIdnumber).child("password").getValue(String.class);
//
                    if(passwordFromDB.equals(userPassword)){
                        String nameFromDB = snapshot.child(userIdnumber).child("name").getValue(String.class);
                        volunteerLoginIdNumber.setError(null);

                        //Pass the data using intent

//                        String nameFromDB = snapshot.child(userIdnumber).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userIdnumber).child("email").getValue(String.class);
                        String idtypeFromDB = snapshot.child(userIdnumber).child("idtype").getValue(String.class);
                        String idnumFromDB = snapshot.child(userIdnumber).child("idnumber").getValue(String.class);
                        String occupationFromDB = snapshot.child(userIdnumber).child("occupation").getValue(String.class);

                        Intent intent = new Intent(VolunteerLoginActivity.this, VolunteerProfileActivity.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("occupation", occupationFromDB);
                        intent.putExtra("idtype", idtypeFromDB);
                        intent.putExtra("idnum", idnumFromDB);
                        intent.putExtra("pass", passwordFromDB);

                        startActivity(intent);
                    }else {
                        volunteerLoginIdNumber.setError("Invalid Credentials");
                        volunteerLoginIdNumber.requestFocus();
                    }
                }else {
                    volunteerLoginIdNumber.setError("User does not exist");
                    volunteerLoginIdNumber.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}