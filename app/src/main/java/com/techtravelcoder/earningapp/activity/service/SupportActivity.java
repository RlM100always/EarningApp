package com.techtravelcoder.earningapp.activity.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.techtravelcoder.earningapp.R;

public class SupportActivity extends AppCompatActivity {

    private LinearLayout fb,tele,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        fb=findViewById(R.id.fb_id);
        tele=findViewById(R.id.telegram_id);
        contact=findViewById(R.id.contact_id);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100085994976088&mibextid=9R9pXO"));
                startActivity(intent);
            }
        });

        tele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+8oc59c9ai-I5ZWM1"));
                startActivity(intent);
            }
        });

    }
}