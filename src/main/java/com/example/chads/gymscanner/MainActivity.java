package com.example.chads.gymscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnNewMember;
    Button btnCheckIn;
    Button btnAllMembers;

    static final int REQUEST_BARCODE = 100;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBManager(this);
        btnNewMember = (Button) findViewById(R.id.btnNewMember);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        btnAllMembers = (Button) findViewById(R.id.btnAllMembers);

        btnNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(ACTION_SCAN);
                scanIntent.putExtra("SCAN_MODE", "BAR_CODE_MODE");
                startActivityForResult(scanIntent, REQUEST_BARCODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BARCODE && resultCode == RESULT_OK) {
            String code = data.getStringExtra("SCAN_RESULT");
            String format = data.getStringExtra("SCAN_RESULT_FORMAT");
            Member member = db.getMemberByCode(code);
            if (member.getBarcode() != null && member.getBarcode().equals(code)){
                db.addVisit(code);
                Intent intent = new Intent(MainActivity.this, CheckInActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),"Sorry, barcode not found.", Toast.LENGTH_SHORT).show();
            }



        } else {
            Toast.makeText(getApplicationContext(),"Sorry, something went wrong.", Toast.LENGTH_SHORT);
        }
    }
}
