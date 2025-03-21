package com.example.philanthrolink;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewNGOReviewActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> reviewsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ngoreview);

        listView = findViewById(R.id.listView_reviews);
        reviewsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reviewsList);
        listView.setAdapter(adapter);

        // Get the volunteer ID from the intent
        String NGOID = getIntent().getStringExtra("userID");

        // Firebase database reference
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("ReviewsByVolunteer");

        // Child event listener to retrieve reviews
        reviewsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                String reviewKey = dataSnapshot.getKey();
                if (reviewKey.contains(NGOID + "_by_")) {
                    String review = dataSnapshot.child("review").getValue(String.class);
                    String volunteerID = dataSnapshot.child("volunteerID").getValue(String.class);
                    reviewsList.add("Volunteer ID : "+volunteerID+"\nReview : "+review);
                    adapter.notifyDataSetChanged();
                }
            }

            // Other child event listener methods (onChildChanged, onChildRemoved, onChildMoved, onCancelled)
            // can be implemented similarly if required.

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                // Handle changed data
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Handle removed data
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
                // Handle moved data
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
