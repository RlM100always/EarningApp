package com.techtravelcoder.earningapp.activity.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.techtravelcoder.earningapp.R;

public class GuideLineActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll1,ll2,ll3,ll4,ll5,ll6,ll7,ll8,ll9,ll10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_line);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        ll1=findViewById(R.id.ques1);
        ll2=findViewById(R.id.ques2);
        ll3=findViewById(R.id.ques3);
        ll4=findViewById(R.id.ques4);
        ll5=findViewById(R.id.ques5);
        ll6=findViewById(R.id.ques6);
        ll7=findViewById(R.id.ques7);
        ll8=findViewById(R.id.ques8);
        ll9=findViewById(R.id.ques9);

        ll1.setOnClickListener(this::onClick);
        ll2.setOnClickListener(this::onClick);
        ll3.setOnClickListener(this::onClick);
        ll4.setOnClickListener(this::onClick);
        ll5.setOnClickListener(this::onClick);
        ll6.setOnClickListener(this::onClick);
        ll7.setOnClickListener(this::onClick);
        ll8.setOnClickListener(this::onClick);
        ll9.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.ques1){
            Intent intent=new Intent(getApplicationContext(),GuideLineDetailsActivity.class);
            intent.putExtra("key","1");
            startActivity(intent);
        }

    }
}