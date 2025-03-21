package com.example.philanthrolink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class NGOScanActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    private CodeScannerView scannerView;
    private TextView codeData;
    private Button contactNGO;
//    private EditText custIdNumber;
    String idNumUser;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngoscan);

        scannerView = findViewById(R.id.scanner_view);
        codeData = findViewById(R.id.scanner_textView);
//        custIdNumber = findViewById(R.id.custNumText);
        contactNGO = findViewById(R.id.button_NGOContact);
        idNumUser = getIntent().getStringExtra("IDUser");

        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Camera permission already granted
            setupScanner();
        }
    }

    private void setupScanner(){
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = result.getText();
                        codeData.setText(data);
                        contactNGO.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(data.contains("NGOId")){
                                    String NGOId = data.substring(6,data.indexOf(' '));
//                                    Toast.makeText(NGOScanActivity.this, NGOId, Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(NGOScanActivity.this, idNumUser, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(NGOScanActivity.this, ViewNGORequestsActivity.class);
                                    intent.putExtra("NGOId",NGOId);
                                    intent.putExtra("VolunteerID", idNumUser);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(NGOScanActivity.this, "Invalid QR Code Scanned!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        // Start the scanner
        codeScanner.startPreview();
    }
}