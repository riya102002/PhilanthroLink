package com.example.philanthrolink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AvailableNGOActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LinearLayout buttonContainer;
    final String[] ngoID = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_ngoactivity);
        databaseReference = FirebaseDatabase.getInstance().getReference("NGOs");
        buttonContainer = findViewById(R.id.buttonContainer);

        TextView headingTextView = new TextView(this);
        headingTextView.setText("Available NGO List");
        headingTextView.setTextSize(20);
        headingTextView.setGravity(Gravity.CENTER);
        headingTextView.setTypeface(null, Typeface.BOLD);
        headingTextView.setTextColor(Color.BLACK);
        int marginInPx = getResources().getDimensionPixelSize(R.dimen.text_margin);
        headingTextView.setPadding(50, 50, 50, 50);
        headingTextView.setBackgroundResource(R.drawable.white_background);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, marginInPx, 0, marginInPx);
        layoutParams.gravity = Gravity.CENTER;
        headingTextView.setLayoutParams(layoutParams);
        buttonContainer.addView(headingTextView);

        // Listen for changes in the database
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ngoID[0] = dataSnapshot.getKey(); // Assuming NGO names are stored as keys
                addButton(ngoID[0]);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Handle changes to the data if needed
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String ngoName = dataSnapshot.getKey();
                removeButton(ngoName);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Handle when a child node changes position if needed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    private void addButton(final String ngoId) {
        Button button = new Button(this);
        button.setText(ngoId);
        button.setTextSize(20);
        button.setTextColor(Color.BLACK);
        button.setBackgroundResource(R.drawable.white_background);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0, 20, 0, 20);
        int paddingInPx = getResources().getDimensionPixelSize(R.dimen.button_padding);
        button.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
        button.setLayoutParams(layoutParams);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(ngoId);
            }
        });

        buttonContainer.addView(button);
    }


    private void removeButton(String ngoName) {
        for (int i = 0; i < buttonContainer.getChildCount(); i++) {
            View child = buttonContainer.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                if (button.getText().toString().equals(ngoName)) {
                    buttonContainer.removeViewAt(i);
                    break;
                }
            }
        }
    }

    private void showPopup(final String ngoId) {
        DatabaseReference ngoReference = databaseReference.child(ngoId);
        ngoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String ngoName = dataSnapshot.child("ngoName").getValue(String.class);
                    String ownerID = dataSnapshot.child("ownerID").getValue(String.class);
                    String ownerName = dataSnapshot.child("ownerName").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String type = dataSnapshot.child("type").getValue(String.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(AvailableNGOActivity.this);
                    builder.setTitle(ngoId);
                    builder.setMessage("NGO ID: " + ngoId + "\n"
                            + "NGO Name: " + ngoName + "\n"
                            + "Owner ID: " + ownerID + "\n"
                            + "Owner Name: " + ownerName + "\n"
                            + "Email: " + email + "\n"
                            + "Phone: " + phone + "\n"
                            + "Type: " + type);
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Handle case when the NGO with the given ID does not exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

}
