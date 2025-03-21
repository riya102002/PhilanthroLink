package com.example.philanthrolink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView ngo, volunteer;
    TextView ngotext, volunteertext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ngo = findViewById(R.id.startupNGOImage);
        volunteer = findViewById(R.id.startupVolunteerImage);
        ngotext = findViewById(R.id.startupNGOText);
        volunteertext = findViewById(R.id.startupVolunterText);

        ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NGOSignupActivity.class);
                startActivity(intent);
            }
        });

        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VolunteerSignupActivity.class);
                startActivity(intent);
            }
        });

        ngotext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NGOSignupActivity.class);
                startActivity(intent);
            }
        });

        volunteertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VolunteerSignupActivity.class);
                startActivity(intent);
            }
        });

    }
}