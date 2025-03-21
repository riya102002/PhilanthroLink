package com.example.philanthrolink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NGOStatsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> requests;
    private List<String> extraDonations;

    private String ngoId;
    private DatabaseReference requestsRef;
    private DatabaseReference extraDonationsRef;

    private Map<String, Integer> requestQuantities;
    private boolean sortByQuantityAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngostats);

        // Get NGO ID from the intent
        ngoId = getIntent().getStringExtra("NGOId");

        requestsRef = FirebaseDatabase.getInstance().getReference("NGO_Donation_Requests")
                .child("NGO_ID_" + ngoId);

        extraDonationsRef = FirebaseDatabase.getInstance().getReference("NGO_Donation_Requests")
                .child("ExtraDonations_" + ngoId);

        listView = findViewById(R.id.listView);
        requests = new ArrayList<>();
        extraDonations = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requests);
        listView.setAdapter(adapter);

        requestQuantities = new HashMap<>();

        // Listen for new requests
        requestsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                String requestKey = dataSnapshot.getKey();
                requests.add(requestKey);
                updateRequestQuantities(requestKey);
                sortRequestsByQuantity();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            // Implement other ChildEventListener methods if needed (onChildChanged, onChildRemoved, onChildMoved)

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });

        // Listen for new extra donations
        extraDonationsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                String extraDonationKey = dataSnapshot.getKey();
                extraDonations.add(extraDonationKey);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            // Implement other ChildEventListener methods if needed (onChildChanged, onChildRemoved, onChildMoved)

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });

        // Handle item click on ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String requestKey = requests.get(position);
                showRequestDetailsDialog(requestKey);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ngostats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_sort) {
            sortByQuantityAscending = !sortByQuantityAscending;
            sortRequestsByQuantity();
            adapter.notifyDataSetChanged();
            return true;
        } else if (itemId == R.id.menu_extra_donations) {
            showExtraDonationsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateRequestQuantities(String requestKey) {
        DatabaseReference requestRef = requestsRef.child(requestKey);
        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer quantity = Integer.parseInt(dataSnapshot.child("quantity").getValue(String.class));
                if (quantity != null) {
                    requestQuantities.put(requestKey, quantity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void sortRequestsByQuantity() {
        Collections.sort(requests, new Comparator<String>() {
            @Override
            public int compare(String requestKey1, String requestKey2) {
                Integer quantity1 = requestQuantities.get(requestKey1);
                Integer quantity2 = requestQuantities.get(requestKey2);

                if (quantity1 != null && quantity2 != null) {
                    if (sortByQuantityAscending) {
                        return quantity1.compareTo(quantity2);
                    } else {
                        return quantity2.compareTo(quantity1);
                    }
                } else {
                    return 0;
                }
            }
        });
    }

    private void showRequestDetailsDialog(String requestKey) {
        DatabaseReference requestRef = requestsRef.child(requestKey);
        requestRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String quantity = dataSnapshot.child("quantity").getValue(String.class);
                    String typeOfDonation = dataSnapshot.child("typeOfDonation").getValue(String.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(NGOStatsActivity.this);
                    builder.setTitle("Request Details")
                            .setMessage("Description: " + description + "\nQuantity: " + quantity + "\nType of Donation: " + typeOfDonation)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            } else {
                // Handle error
            }
        });
    }

    private void showExtraDonationsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NGOStatsActivity.this);
        builder.setTitle("Extra Donations")
                .setAdapter(new ArrayAdapter<>(NGOStatsActivity.this, android.R.layout.simple_list_item_1, extraDonations), new DialogInterface.OnClickListener() {
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {
                        String extraDonationKey = extraDonations.get(which);
                        showExtraDonationDialog(extraDonationKey);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showExtraDonationDialog(String extraDonationKey) {
        DatabaseReference extraDonationRef = extraDonationsRef.child(extraDonationKey);
        extraDonationRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    String description = dataSnapshot.child("description").getValue(String.class);
                    long quantity = dataSnapshot.child("quantity").getValue(Long.class);
                    String typeOfDonation = dataSnapshot.child("typeOfDonation").getValue(String.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(NGOStatsActivity.this);
                    builder.setTitle("Extra Donation Details")
                            .setMessage("Description: " + description + "\nQuantity: " + quantity + "\nType of Donation: " + typeOfDonation)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            } else {
                // Handle error
            }
        });
    }
}
