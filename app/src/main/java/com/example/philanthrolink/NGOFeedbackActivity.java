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

public class NGOFeedbackActivity extends AppCompatActivity {

    EditText volunteerID, volunteerReview;
    Button search, submit, NGOViewReviewButton;
    ImageView qrImage;
    TextView volunteerName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngofeedback);

        String value = getIntent().getStringExtra("NGOId");


        volunteerID = findViewById(R.id.volunteerIDSearch);
        volunteerReview = findViewById(R.id.volunteerReviewEditText);
        search = findViewById(R.id.volunteerSearchButton);
        submit = findViewById(R.id.volunteerReviewSubmitButton);
        qrImage = findViewById(R.id.volunteerQRView);
        volunteerName = findViewById(R.id.volunteerName);
        NGOViewReviewButton = findViewById(R.id.NGOViewReviewsButton);

        NGOViewReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NGOFeedbackActivity.this, ViewNGOReviewActivity.class);
                intent.putExtra("userID", value);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idVolunteer = volunteerID.getText().toString();
                String VID = volunteerID.getText().toString().trim();
                if(VID.isEmpty()){
                    volunteerID.setError("Volunteer ID cannot be empty!");
                }else{
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Volunteers");
                    Query checkVolunteerDatabase = reference.orderByChild("idnumber").equalTo(VID);
                    checkVolunteerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String nameFromDB = snapshot.child(VID).child("name").getValue(String.class);

                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReference().child("qrCodes");
                                StorageReference imageRef = storageReference.child("QR_"+VID+".jpg");
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

                                volunteerName.setText(nameFromDB);

                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String review = volunteerReview.getText().toString().trim();
                                        if(review.isEmpty()){
                                            volunteerReview.setError("Review Cannot be empty!");
                                        }else{
                                            NGOReviewHelper reviewHelper = new NGOReviewHelper(review, value);
                                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ReviewsByNGO");
                                            reference1.child(idVolunteer+"_by_"+value).setValue(reviewHelper);
                                            Toast.makeText(NGOFeedbackActivity.this, "Review Submitted Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(NGOFeedbackActivity.this, "Volunteer Does Not Exist! Try Again.", Toast.LENGTH_SHORT).show();
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