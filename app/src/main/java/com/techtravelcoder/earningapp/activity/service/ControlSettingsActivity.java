package com.techtravelcoder.earningapp.activity.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techtravelcoder.earningapp.R;

public class ControlSettingsActivity extends AppCompatActivity {

    private LinearLayout newCamp,campHist,postedJob,payHis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_settings);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        newCamp=findViewById(R.id.new_camp_id);
        campHist=findViewById(R.id.camp_history_id);
        postedJob=findViewById(R.id.posted_job_id);
        payHis=findViewById(R.id.payment_his_id);


        newCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ControlSettingsActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        campHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ControlSettingsActivity.this,CampainHisActivity.class);
                startActivity(intent);
            }
        });
        postedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ControlSettingsActivity.this,SpecificUserActivity.class);
                startActivity(intent);
            }
        });

        payHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ControlSettingsActivity.this,PaymentBuyActivity.class);
                startActivity(intent);
            }
        });


    }
}