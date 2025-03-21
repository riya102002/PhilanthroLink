package com.example.philanthrolink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class VolunteerProfileActivity extends AppCompatActivity {

    static String idnumUser, nameUser, occupationUser, emailUser, idtypeUser, passUser;
    TextView profileOccupation, profilePass, profileIdtype, profileIdnum;
    TextView titleName, titleEmail, reviewText, donationsText;
    ImageView review, donations;
    Button ProfileBack, viewNGO, scanNGO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        profileOccupation = findViewById(R.id.profileOccupation);
        profilePass = findViewById(R.id.profilePass);
        profileIdnum = findViewById(R.id.profileIdNum);
        profileIdtype = findViewById(R.id.profileIdType);
        titleName = findViewById(R.id.titleName);
        titleEmail = findViewById(R.id.titleEmail);
        ProfileBack = findViewById(R.id.ProfileBack);
        review = findViewById(R.id.ReviewPhoto);
        reviewText = findViewById(R.id.ReviewText);
        donations = findViewById(R.id.DonationsPhoto);
        donationsText = findViewById(R.id.DonationsText);
        viewNGO = findViewById(R.id.view_NGO_Button);
        scanNGO = findViewById(R.id.scan_NGO_Button);

        Intent intent = getIntent();
        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        occupationUser = intent.getStringExtra("occupation");
        idtypeUser = intent.getStringExtra("idtype");
        passUser = intent.getStringExtra("pass");
        idnumUser = intent.getStringExtra("idnum");

        String allData = "VolunteerID:"+idnumUser+" VolunteerName:"+nameUser+" VolunteerEmail:"+emailUser+" VolunteerOccupation:"+occupationUser+
                " VolunteerIDType: "+idtypeUser;
        QRGEncoder qrgEncoder = new QRGEncoder(allData, null, QRGContents.Type.TEXT, 1000);
        Bitmap qrBits = qrgEncoder.getBitmap();
        uploadImage(qrBits);

        showUserData();

        ProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerProfileActivity.this, VolunteerLoginActivity.class);
                Toast.makeText(VolunteerProfileActivity.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finishAffinity();
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerProfileActivity.this, VolunteerReviewActivity.class);
                intent.putExtra("ID", idnumUser);
                startActivity(intent);
            }
        });

        reviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerProfileActivity.this, VolunteerReviewActivity.class);
                intent.putExtra("ID", idnumUser);
                startActivity(intent);
            }
        });

        donations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerProfileActivity.this, VolunteerDonationsActivity.class);
                intent.putExtra("IDUser", idnumUser);
                startActivity(intent);
            }
        });

        donationsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerProfileActivity.this, VolunteerDonationsActivity.class);
                intent.putExtra("IDUser", idnumUser);
                startActivity(intent);
            }
        });

        scanNGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerProfileActivity.this, NGOScanActivity.class);
                intent.putExtra("IDUser", idnumUser);
                startActivity(intent);
            }
        });

        viewNGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerProfileActivity.this, AvailableNGOActivity.class);
                intent.putExtra("IDUser", idnumUser);
                startActivity(intent);
            }
        });
    }

    public void showUserData(){

        String greet="Welcome "+nameUser;
        Toast.makeText(VolunteerProfileActivity.this, greet, Toast.LENGTH_SHORT).show();

        titleName.setText(nameUser);
        titleEmail.setText(emailUser);
        profileOccupation.setText(occupationUser);
        profileIdnum.setText(idnumUser);
        profileIdtype.setText(idtypeUser);
        profilePass.setText(passUser);

    }

    void uploadImage(Bitmap bitmap){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String filename = "QR_" + idnumUser + ".jpg";
        StorageReference storageRef = storage.getReference().child("qrCodes/" + filename);
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