package com.example.philanthrolink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VolunteerForgotPasswordActivity extends AppCompatActivity {

    EditText queryIdNum;
    Button submitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngoforgot_password);

        queryIdNum = findViewById(R.id.ngo_forgot_password_idnum);
        submitButton = findViewById(R.id.ngo_forgot_password_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Volunteers");
                Query checkUserDatabase = reference.orderByChild("idnumber").equalTo(queryIdNum.getText().toString().trim());
                String idNum = queryIdNum.getText().toString().trim();
                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
//                            Toast.makeText(ContactActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                            queryIdNum.setError(null);
                            String passwordFromDB = snapshot.child(idNum).child("password").getValue(String.class);
                            String emailFromDB = snapshot.child(idNum).child("email").getValue(String.class);

                            try {
                                String senderEmail = "teamphilanthrolink@gmail.com";
                                String stringReceiverEmail = emailFromDB;
                                String stringPasswordSenderEmail = "nnlqgfolyzutuaqp";

                                String stringHost = "smtp.gmail.com";

                                Properties properties = System.getProperties();

                                properties.put("mail.smtp.host", stringHost);
                                properties.put("mail.smtp.port", "465");
                                properties.put("mail.smtp.ssl.enable", "true");
                                properties.put("mail.smtp.auth", "true");

                                javax.mail.Session session = Session.getInstance(properties, new Authenticator(){
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication(){
                                        return new PasswordAuthentication(senderEmail, stringPasswordSenderEmail);
                                    }
                                });

                                MimeMessage mimeMessage = new MimeMessage(session);

                                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                                mimeMessage.setSubject("Message from PhilanthroLink Team");
                                mimeMessage.setText("Don't Worry! \nPhilanthroVolunteer got you covered!\n\nFor the ID Number : "+idNum+"\nHere's your password : "+passwordFromDB+"\n\nTeam PhilanthroLink");

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Transport.send(mimeMessage);
                                        } catch (MessagingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                thread.start();
                                Toast.makeText(VolunteerForgotPasswordActivity.this, "Recovery Mail with your Password sent to your Registered Email ID", Toast.LENGTH_SHORT).show();

                            } catch (AddressException e){
                                e.printStackTrace();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(VolunteerForgotPasswordActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}