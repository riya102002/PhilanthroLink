package com.example.philanthrolink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VolunteerReviewActivity extends AppCompatActivity {

    EditText ngoID, ngoReview;
    Button search, submit, viewReviews;
    ImageView qrImage;
    TextView ngoName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_review);

        String value = getIntent().getStringExtra("ID");

        ngoID = findViewById(R.id.ngoIDSearch);
        ngoReview = findViewById(R.id.ngoReviewEditText);
        search = findViewById(R.id.ngoSearchButton);
        submit = findViewById(R.id.ngoReviewSubmitButton);
        qrImage = findViewById(R.id.ngoQRView);
        ngoName = findViewById(R.id.ngoName);
        viewReviews = findViewById(R.id.volunteerViewReviewsButton);

        viewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerReviewActivity.this, ViewVolunteerReviewActivity.class);
                intent.putExtra("userID", value);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idNGO = ngoID.getText().toString();
                String NID = ngoID.getText().toString().trim();
                if(NID.isEmpty()){
                    ngoID.setError("Volunteer ID cannot be empty!");
                }else{
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NGOs");
                    Query checkVolunteerDatabase = reference.orderByChild("ownerID").equalTo(NID);
                    checkVolunteerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String nameFromDB = snapshot.child(NID).child("ngoName").getValue(String.class);

                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReference().child("NGOQrCodes");
                                StorageReference imageRef = storageReference.child("QR_"+NID+".jpg");
                                imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        // Convert the byte array to a Bitmap object
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                        // Use the Bitmap object however you need to (e.g. display it in an ImageView)
                                        qrImage.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors that occur while downloading the image
                                    }
                                });

                                ngoName.setText(nameFromDB);

                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String review = ngoReview.getText().toString().trim();
                                        if(review.isEmpty()){
                                            ngoReview.setError("Review Cannot be empty!");
                                        }else{
                                            VolunteerReviewHelper reviewHelper = new VolunteerReviewHelper(review, value);
                                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ReviewsByVolunteer");
                                            reference1.child(idNGO+"_by_"+value).setValue(reviewHelper);
                                            Toast.makeText(VolunteerReviewActivity.this, "Review Submitted Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(VolunteerReviewActivity.this, "NGO Does Not Exist! Try Again.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}