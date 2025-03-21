package com.example.philanthrolink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class NGOProfileActivity extends AppCompatActivity {

    Spinner TypeOfDonation;
    EditText Description, Quantity;
    Button saveDetails, logout;
    ImageView qrImage, feedback, stats;
    TextView titleIdNumView, titleNGONameView, feedbackText, statsText;
    FirebaseDatabase database;
    DatabaseReference reference;

    static String nameOwner, ngoPhone, nameNGO, ngoIdNum, ngoEmail, donationType, donationDescription, donationQuantity;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngoprofile);

        TypeOfDonation = findViewById(R.id.ngo_type_of_donation);
        Description = findViewById(R.id.ngo_description);
        Quantity = findViewById(R.id.ngo_quantity);
        qrImage = findViewById(R.id.qrimage);
        logout = findViewById(R.id.ProfileBack);
        saveDetails = findViewById(R.id.save_details_button);
        feedback = findViewById(R.id.FeedbackPhoto);
        feedbackText = findViewById(R.id.FeedbackText);
        stats = findViewById(R.id.StatsPhoto);
        statsText = findViewById(R.id.StatsText);
        TypeOfDonation = (Spinner) findViewById(R.id.ngo_type_of_donation);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(NGOProfileActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.donation));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypeOfDonation.setAdapter(myAdapter);

        Intent intent = getIntent();

        nameNGO = intent.getStringExtra("ngoName");
        ngoEmail = intent.getStringExtra("email");
        nameOwner = intent.getStringExtra("name");
        ngoIdNum = intent.getStringExtra("id");
        ngoPhone = intent.getStringExtra("phone");

        titleNGONameView = findViewById(R.id.titleStoreName);
        titleIdNumView = findViewById(R.id.titleIDNum);

        String allData = "NGOId:"+ngoIdNum+" NGOName:"+nameNGO+" NGOOwner:"+nameOwner+" NGOEmail:"+ngoEmail+
                " NGOPhone: "+ngoPhone;
        QRGEncoder qrgEncoder = new QRGEncoder(allData, null, QRGContents.Type.TEXT, 1000);
        Bitmap qrBits = qrgEncoder.getBitmap();
        qrImage.setImageBitmap(qrBits);
        uploadImage(qrBits);
        titleIdNumView.setText(ngoIdNum);
        titleNGONameView.setText(nameNGO);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NGOProfileActivity.this, NGOLoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NGOProfileActivity.this, ngoIdNum, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(NGOProfileActivity.this, NGOFeedbackActivity.class);
                intent1.putExtra("NGOId", ngoIdNum);
                startActivity(intent1);
            }
        });

        feedbackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NGOProfileActivity.this, ngoIdNum, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(NGOProfileActivity.this, NGOFeedbackActivity.class);
                intent1.putExtra("NGOId", ngoIdNum);
                startActivity(intent1);
            }
        });

        statsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NGOProfileActivity.this, NGOStatsActivity.class);
                intent1.putExtra("NGOId", ngoIdNum);
                startActivity(intent1);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NGOProfileActivity.this, NGOStatsActivity.class);
                intent1.putExtra("NGOId", ngoIdNum);
                startActivity(intent1);
            }
        });

        final int[] c = {1};
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donationType = TypeOfDonation.getSelectedItem().toString().trim();
                donationDescription = Description.getText().toString().trim();
                donationQuantity = Quantity.getText().toString().trim();

                if(donationType.equals("SELECT DONATION TYPE")){
//                    TypeOfDonation.setError("Donation Type cannot be Empty!");
                    Toast.makeText(NGOProfileActivity.this, "Donation type not selected!", Toast.LENGTH_SHORT).show();
                }else if(donationDescription.isEmpty()){
                    Description.setError("Donation Description cannot be Empty!");
                }else if(donationQuantity.isEmpty()){
                    Quantity.setError("Donation Quantity cannot be Empty!");
                }else{
                    Random rand = new Random();
                    int n = rand.nextInt(100);
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("NGO_Donation_Requests");
                    NGOProfileHelper ngoProfileHelper = new NGOProfileHelper(donationType, donationDescription, donationQuantity);
                    reference.child("NGO_ID_"+ngoIdNum).child("requestID:"+n).setValue(ngoProfileHelper);
                    Toast.makeText(NGOProfileActivity.this, "Request saved Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void uploadImage(Bitmap bitmap){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String filename = "QR_" + ngoIdNum + ".jpg";
        StorageReference storageRef = storage.getReference().child("NGOQrCodes/" + filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Save the image URL to Firebase Database
//                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//                        String key = database.child("images").push().getKey();
//                        database.child("images").child(key).setValue(uri.toString());
                    }
                });
            }
        });
    }
}