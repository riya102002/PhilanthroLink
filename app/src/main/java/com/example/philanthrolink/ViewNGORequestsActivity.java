package com.example.philanthrolink;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewNGORequestsActivity extends AppCompatActivity {

    DatabaseReference donationRef;
    DatabaseReference extraDonationRef;
    DatabaseReference reviewRef;
    ListView donationListView;
    List<String> donationList;
    ArrayAdapter<String> donationAdapter;
    String userID; // User ID
    int donationID = 1; // Sequential donation ID
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ngorequests);

        String ngoID = getIntent().getStringExtra("NGOId");
        userID = getIntent().getStringExtra("VolunteerID");

        donationRef = FirebaseDatabase.getInstance().getReference("NGO_Donation_Requests").child("NGO_ID_" + ngoID);
        extraDonationRef = FirebaseDatabase.getInstance().getReference("NGO_Donation_Requests").child("ExtraDonations_" + ngoID);
        reviewRef = FirebaseDatabase.getInstance().getReference("Donation_Received").child("NGO_ID_" + ngoID);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        donationID = sharedPreferences.getInt("donationID", 1);

        TextView headingTextView = findViewById(R.id.headingTextView);
        headingTextView.setText("Pending Requests from NGO " + ngoID);
        donationListView = findViewById(R.id.donationListView);
        donationList = new ArrayList<>();
        donationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, donationList);
        donationListView.setAdapter(donationAdapter);


        donationListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedItem = donationList.get(position);
            String[] parts = selectedItem.split("\n");
            String requestID = parts[0].replace("Request ID : ", "");
            String typeOfDonation = parts[1].replace("Type of Donation : ", "");
            String quantity = parts[2].replace("Quantity : ", "");
            String description = parts[3].replace("Description : ", "");

            AlertDialog.Builder builder = new AlertDialog.Builder(ViewNGORequestsActivity.this);
            builder.setTitle("Donation Details");
            builder.setMessage("Request ID: " + requestID + "\nType of Donation: " + typeOfDonation
                    + "\nQuantity: " + quantity + "\nDescription: " + description);

            LinearLayout layout = new LinearLayout(ViewNGORequestsActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText numberEditText = new EditText(ViewNGORequestsActivity.this);
            numberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            numberEditText.setHint("Enter quantity");
            layout.addView(numberEditText);

            final EditText remarksEditText = new EditText(ViewNGORequestsActivity.this);
            remarksEditText.setHint("Enter remarks");
            layout.addView(remarksEditText);

            builder.setView(layout);

            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                String enteredNumber = numberEditText.getText().toString().trim();
                String remarks = remarksEditText.getText().toString().trim();
                if (enteredNumber.isEmpty()) {
                    Toast.makeText(this, "Donation Quantity Empty!", Toast.LENGTH_SHORT).show();
                } else {
                    int enteredQuantity = Integer.parseInt(enteredNumber);

                    if (enteredQuantity > 0) {
                        int currentQuantity = Integer.parseInt(quantity);
                        int updatedQuantity = currentQuantity - enteredQuantity;

                        if (enteredQuantity < currentQuantity) {
                            DatabaseReference quantityRef = donationRef.child("requestID:" + requestID).child("quantity");
                            quantityRef.setValue(updatedQuantity + "");

                            DatabaseReference userDonationRef = FirebaseDatabase.getInstance().getReference("User_Donations").child("DonationBy:" + userID);
                            String donationId = String.valueOf(donationID++);
                            DatabaseReference newDonationRef = userDonationRef.child(donationId);
                            newDonationRef.child("requestID").setValue(requestID);
                            newDonationRef.child("ngoID").setValue(ngoID);
                            newDonationRef.child("typeOfDonation").setValue(typeOfDonation);
                            newDonationRef.child("quantity").setValue(enteredQuantity);
                            newDonationRef.child("remarks").setValue(remarks);
                            newDonationRef.child("donationByVolunteerID").setValue(userID);

                            String reviewId = reviewRef.push().getKey();
                            DatabaseReference newReviewRef = reviewRef.child(reviewId);
                            newReviewRef.child("requestID").setValue(requestID);
                            newReviewRef.child("typeOfDonation").setValue(typeOfDonation);
                            newReviewRef.child("quantity").setValue(enteredQuantity);
                            newReviewRef.child("remarks").setValue(remarks);
                            newReviewRef.child("donationByVolunteerID").setValue(userID);

                            Toast.makeText(ViewNGORequestsActivity.this, ("Thank you for your Donation!\nUpdated Quantity: " + updatedQuantity), Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference quantityRef = donationRef.child("requestID:" + requestID).child("quantity");
                            quantityRef.setValue("0");

                            DatabaseReference userDonationRef = FirebaseDatabase.getInstance().getReference("User_Donations").child("DonationBy:" + userID);
                            String donationId = String.valueOf(donationID++);
                            DatabaseReference newDonationRef = userDonationRef.child(donationId);
                            newDonationRef.child("requestID").setValue(requestID);
                            newDonationRef.child("ngoID").setValue(ngoID);
                            newDonationRef.child("typeOfDonation").setValue(typeOfDonation);
                            newDonationRef.child("quantity").setValue(enteredQuantity);
                            newDonationRef.child("remarks").setValue(remarks);

                            String reviewId = reviewRef.push().getKey();
                            DatabaseReference newReviewRef = reviewRef.child(reviewId);
                            newReviewRef.child("requestID").setValue(requestID);
                            newReviewRef.child("typeOfDonation").setValue(typeOfDonation);
                            newReviewRef.child("quantity").setValue(enteredQuantity);
                            newReviewRef.child("remarks").setValue(remarks);

                            DatabaseReference extraDonationIdRef = extraDonationRef.child("requestID:" + requestID);
                            extraDonationIdRef.child("ngoID").setValue(ngoID);
                            extraDonationIdRef.child("typeOfDonation").setValue(typeOfDonation);
                            extraDonationIdRef.child("quantity").setValue(enteredQuantity);
                            extraDonationIdRef.child("extraQuantity").setValue(enteredQuantity - currentQuantity);
                            extraDonationIdRef.child("remarks").setValue(remarks);
                            extraDonationIdRef.child("donationByVolunteerID").setValue(userID);

                            Toast.makeText(ViewNGORequestsActivity.this, "Thank you for the extra donation! Be assured your contribution will be used for a good cause!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ViewNGORequestsActivity.this, "Invalid quantity entered.", Toast.LENGTH_SHORT).show();
                    }
                }

                refreshScreen();
            });

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                refreshScreen();
            });

            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            dialog.show();
        });

        loadDonationRequests();
    }

    private void loadDonationRequests() {
        for (int i = 1; i <= 100; i++) {
            String requestId = "requestID:" + i;
            String reqID = "Request ID : " + i;
            DatabaseReference childRef = donationRef.child(requestId);

            childRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    if (dataSnapshot.getKey().equals("quantity")) {
                        int q = Integer.parseInt(dataSnapshot.getValue(String.class));
                        if (q > 0) {
                            DatabaseReference typeRef = childRef.child("typeOfDonation");
                            typeRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String type = snapshot.getValue(String.class);

                                    DatabaseReference descriptionRef = childRef.child("description");
                                    descriptionRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String desc = snapshot.getValue(String.class);

                                            String donationRequest = reqID + "\nType of Donation : " + type
                                                    + "\nQuantity : " + q + "\nDescription : " + desc;

                                            donationList.add(donationRequest);
                                            donationAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    // Handle child data changes if needed
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String donationRequest = dataSnapshot.getValue(String.class);
                    donationList.remove(donationRequest);
                    donationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Handle child moved if needed
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error if needed
                }
            });
        }
    }

    private void refreshScreen() {
        donationList.clear();
        donationAdapter.notifyDataSetChanged();
        loadDonationRequests();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("donationID", donationID);
        editor.apply();
    }
}
