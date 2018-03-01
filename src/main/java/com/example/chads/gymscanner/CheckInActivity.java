package com.example.chads.gymscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class CheckInActivity extends AppCompatActivity {
    private DBManager db;
    private Member member;
    private String code;
    private Button btnCheckIn;
    private TextView txtAge;
    private TextView txtDob;
    private TextView txtCode;
    private TextView txtVisits;
    private TextView txtFirstName;
    private TextView txtLastName;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        db = new DBManager(this);

        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        txtAge = (TextView) findViewById(R.id.txtAge);
        txtDob = (TextView) findViewById( R.id.txtDob );
        txtCode = (TextView) findViewById( R.id.txtCode );
        txtVisits = (TextView) findViewById( R.id.txtVisits );
        txtFirstName = (TextView) findViewById( R.id.etxtFirstName);
        txtLastName = (TextView) findViewById( R.id.txtLastName );
        imageView = (ImageView) findViewById( R.id.imageView );
        code = getIntent().getStringExtra("code");
        member = db.getMemberByCode(code);

        Log.d("member loaded", member.toString());

        txtAge.setText(String.valueOf(member.getAge()));
        txtCode.setText("#" + member.getBarcode());
        txtDob.setText(member.getDob());
        txtFirstName.setText(member.getFirstName());
        txtLastName.setText(member.getLastName());
        txtVisits.setText("Visits: " + String.valueOf(member.getVisits()));

        File imgFile = new File(member.getAvatar());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }



        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckInActivity.this, MainActivity.class);
                if (member.isStatus()){
                    db.setStatus(code, false);
                    intent.putExtra("status", "checked-out");
                }
                else {
                    db.setStatus(code, true);
                    intent.putExtra("status", "checked-in");
                    db.addVisit(code);
                }
                startActivity(intent);
            }
        });
    }
}
