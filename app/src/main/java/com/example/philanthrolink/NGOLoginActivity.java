package com.example.philanthrolink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NGOLoginActivity extends AppCompatActivity {


    EditText NGOLoginIDNumber, NGOLoginPassword;
    Button NGOLoginButton;
    TextView NGOSignupRedirectText, NGOForgotPasswordText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngologin);

        NGOLoginIDNumber = findViewById(R.id.ngo_login_idnum);
        NGOLoginPassword = findViewById(R.id.ngo_login_pass);
        NGOLoginButton = findViewById(R.id.ngo_login_button);
        NGOSignupRedirectText = findViewById(R.id.ngo_signupRedirectText);
        NGOForgotPasswordText = findViewById(R.id.ngo_ForgotPasswordRedirectText);

        NGOLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateIdnumber() | !validatePassword()){

                }else{
                    checkUser();
                }
            }
        });

        NGOForgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NGOLoginActivity.this, NGOForgotPassword.class);
                startActivity(intent);
            }
        });

        NGOSignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NGOLoginActivity.this, NGOSignupActivity.class);
                startActivity(intent);
            }
        });

        NGOSignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NGOLoginActivity.this, NGOForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateIdnumber(){
        String val = NGOLoginIDNumber.getText().toString().trim();
        if(val.isEmpty()){
            NGOLoginIDNumber.setError("Username cannot be empty");
            return false;
        }
        else {
            NGOLoginIDNumber.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = NGOLoginPassword.getText().toString().trim();
        if(val.isEmpty()){
            NGOLoginPassword.setError("Password cannot be empty");
            return false;
        }
        else {
            NGOLoginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String ngoUserIdNumber = NGOLoginIDNumber.getText().toString().trim();
        String ngoUserPassword = NGOLoginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NGOs");
        Query checkUserDatabase = reference.orderByChild("ownerID").equalTo(ngoUserIdNumber);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    NGOLoginIDNumber.setError(null);
                    String passwordFromDB = snapshot.child(ngoUserIdNumber).child("password").getValue(String.class);
//
                    if (passwordFromDB.equals(ngoUserPassword)) {
                        String ownerNameFromDB = snapshot.child(ngoUserIdNumber).child("ownerID").getValue(String.class);
                        NGOLoginIDNumber.setError(null);

                        //Pass the data using intent

//                        String nameFromDB = snapshot.child(userIdnumber).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(ngoUserIdNumber).child("email").getValue(String.class);
                        String ngoTypeFromDB = snapshot.child(ngoUserIdNumber).child("type").getValue(String.class);
                        String phoneFromDB = snapshot.child(ngoUserIdNumber).child("phone").getValue(String.class);
                        String ngoNameFromDB = snapshot.child(ngoUserIdNumber).child("ngoName").getValue(String.class);
                        String idnumFromDB = snapshot.child(ngoUserIdNumber).child("ownerID").getValue(String.class);


                        Intent intent = new Intent(NGOLoginActivity.this, NGOProfileActivity.class);

                        intent.putExtra("id", idnumFromDB);
                        intent.putExtra("name", ownerNameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("ngoType", ngoTypeFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        intent.putExtra("pass", passwordFromDB);
                        intent.putExtra("ngoName", ngoNameFromDB);

                        startActivity(intent);
                    } else {
                        NGOLoginPassword.setError("Invalid Credentials");
                        NGOLoginPassword.requestFocus();
                    }
                } else {
                    NGOLoginIDNumber.setError("User does not exist");
                    NGOLoginIDNumber.requestFocus();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}