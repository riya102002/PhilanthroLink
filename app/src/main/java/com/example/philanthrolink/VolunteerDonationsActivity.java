package com.example.philanthrolink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VolunteerDonationsActivity extends AppCompatActivity {

    private ListView donationsListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> donationsList;
    private String userID; // User ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_donations);

        donationsListView = findViewById(R.id.donationsListView);
        donationsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, donationsList);
        donationsListView.setAdapter(adapter);

        Intent intent = getIntent();
        userID = intent.getStringExtra("IDUser");

//        userID = "your_user_id"; // Replace with the actual user ID

        retrieveUserDonations();
    }

    private void retrieveUserDonations() {
        DatabaseReference userDonationsRef = FirebaseDatabase.getInstance()
                .getReference("User_Donations")
                .child("DonationBy:" + userID);

        userDonationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donationsList.clear();

                for (DataSnapshot donationSnapshot : dataSnapshot.getChildren()) {
                    String donationId = donationSnapshot.getKey();
                    String requestID = donationSnapshot.child("requestID").getValue(String.class);
                    String ngoID = donationSnapshot.child("ngoID").getValue(String.class);
                    String typeOfDonation = donationSnapshot.child("typeOfDonation").getValue(String.class);
                    int quantity = donationSnapshot.child("quantity").getValue(Integer.class);
                    String remarks = donationSnapshot.child("remarks").getValue(String.class);

                    String donation = "Donation ID: " + donationId
                            + "\nRequest ID: " + requestID
                            + "\nNGO ID: " + ngoID
                            + "\nType of Donation: " + typeOfDonation
                            + "\nQuantity: " + quantity
                            + "\nRemarks: " + remarks;

                    donationsList.add(donation);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }
}
