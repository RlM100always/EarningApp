package com.techtravelcoder.earningapp.activity.ads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.common.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.activity.broadcast.EarningAlarmReceiver;
import com.techtravelcoder.earningapp.activity.broadcast.VisibilityAlarmReceiver;

import java.util.ArrayList;
import java.util.Random;

public class SolveMathActivity extends AppCompatActivity {

    TextView increment,num1,num2,submit,totalTask;
    EditText ans ;
    private int ranAns;
    private int intIncre=0;
    Banner startAppBanner ;
    int intTaskNum,intCurrentTaskNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.setTestAdsEnabled(true);

        setContentView(R.layout.activity_solve_math);

        startAppBanner=findViewById(R.id.startAppBanner);
        startAppBanner.loadAd();


        increment=findViewById(R.id.sm_increament_id);
        totalTask=findViewById(R.id.total_task_number);

        num1=findViewById(R.id.num1_id);
        num2=findViewById(R.id.num2_id);
        ans=findViewById(R.id.sm_ans_id);
        submit=findViewById(R.id.sm_submit_id);

        FirebaseDatabase.getInstance().getReference("Control").child("StartIo").
                child("admin").child("taskNumber").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            intTaskNum=snapshot.getValue(Integer.class);
                            totalTask.setText(String.valueOf(intTaskNum));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        callMore();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ranAns==Integer.parseInt(ans.getText().toString())){
                    Toast.makeText(SolveMathActivity.this, "Equal", Toast.LENGTH_SHORT).show();
                    ans.setText("");
                    intIncre++;
                    if(intIncre==intTaskNum){
                       totalCurrentEquals();
                    }else {
                        setCurrentIncreamentValue(intIncre);
                        callMore();
                        startActivity(new Intent(SolveMathActivity.this, AnsActivity.class));
                        StartAppAd.showAd(SolveMathActivity.this);
                    }


                }else {
                    Toast.makeText(SolveMathActivity.this, "Not Equal", Toast.LENGTH_SHORT).show();

                }
            }
        });




    }

    private void totalCurrentEquals() {
        FirebaseDatabase.getInstance().getReference("Control").child("StartIo").
                child("user").child(FirebaseAuth.getInstance().getUid()).
                child("visibility").setValue(false);

        FirebaseDatabase.getInstance().getReference("Control").child("StartIo").
                child("user").child(FirebaseAuth.getInstance().getUid()).
                child("currentTaskNumber").setValue(0);

        // 1000 * 60 * 60
        long delayInMillis = 10 * 1000; // Set the delay time in milliseconds (3 seconds for testing)

        Intent alarmIntent = new Intent(this, EarningAlarmReceiver.class);
        alarmIntent.putExtra("userUid", FirebaseAuth.getInstance().getUid());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Use setExact for precise timing
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayInMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayInMillis, pendingIntent);
        }

        Toast.makeText(this, "Alarm set for 3 seconds", Toast.LENGTH_SHORT).show();
    }

    private void setCurrentIncreamentValue(int val) {
        FirebaseDatabase.getInstance().getReference("Control").child("StartIo").
                child("user").child(FirebaseAuth.getInstance().getUid()).
                child("currentTaskNumber").setValue(val);
    }

    public void callMore(){
        FirebaseDatabase.getInstance().getReference("Control").child("StartIo").
                child("user").child(FirebaseAuth.getInstance().getUid()).
                child("currentTaskNumber").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            if(snapshot.getValue(Integer.class)==null){
                                intIncre=0;
                                increment.setText("0");

                            }else {
                                intIncre=snapshot.getValue(Integer.class);
                                increment.setText(String.valueOf(intIncre));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        increment.setText(String.valueOf(intIncre));

        int ran1=generateRandomNumber();
        int ran2=generateRandomNumber();
         ranAns=ran1+ran2;

        num1.setText(String.valueOf(ran1));
        num2.setText(String.valueOf(ran2));
    }


    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(100);
    }
}