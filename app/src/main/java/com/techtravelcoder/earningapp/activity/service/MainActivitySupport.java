package com.techtravelcoder.earningapp.activity.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.activity.broadcast.VisibilityAlarmReceiver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import es.dmoral.toasty.Toasty;

public class MainActivitySupport extends AppCompatActivity {


    int user_points;
     CountDownTimer countDownTimer;
    String key,uid;
    int siz ;
    TextView s_second ;
    ArrayList<String> keysList=new ArrayList<>() ;

    WebView webView1;
    public boolean isRunning  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_support);

        String s_link = getIntent().getStringExtra("link");
        key=getIntent().getStringExtra("postKey");
        uid=getIntent().getStringExtra("uid");
        siz=getIntent().getIntExtra("size",0);

        s_second=findViewById(R.id.second_id);





           webView1=findViewById(R.id.webview_id);
            webView1.loadUrl(s_link);
            WebSettings obj= webView1.getSettings();

            obj.setJavaScriptEnabled(true);
            webView1.setWebViewClient(new WebViewClient());
             webView1.getSettings().setDomStorageEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webView1.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Use a custom WebViewClient to handle SSL errors
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
        });

//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s_link));
//            startActivity(intent);


        keysList = getKeysList();
            startTimer();

    }

    private void startTimer() {

        isRunning=true;
        countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountdownTextView(millisUntilFinished);
              //  Toast.makeText(MainActivitySupport.this, "Wait 10 second", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                handlePointsAndNavigate();

            }
        }.start();
    }
    private void updateCountdownTextView(long millisUntilFinished) {
        long secondsRemaining = millisUntilFinished / 1000;
        long minutes = secondsRemaining / 60;
        long seconds = secondsRemaining % 60;

        String timeRemaining = String.format("Wait : %02d:%02d", minutes, seconds);
        s_second.setText(timeRemaining);
    }
    private void dialoguePage(){

        FirebaseDatabase.getInstance().getReference("Control")
                .child("Visit Website").child("points")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         user_points=snapshot.getValue(Integer.class);
                     //   Toast.makeText(MainActivitySupport.this, ""+user_points, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivitySupport.this, "Something Problem..."+user_points, Toast.LENGTH_SHORT).show();

                    }
                });

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View view=getLayoutInflater().inflate(R.layout.dialogue_design,null);
        TextView collect=view.findViewById(R.id.collect_id);
        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        alertDialog.setCancelable(false);
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_dialogue_back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(getApplicationContext(),"Congratulations You earn "+user_points+" Points ",Toasty.LENGTH_SHORT).show();
                alertDialog.dismiss();

                updateBalanceAndInformation();

            }
        });


    }
    private void updateBalanceAndInformation() {
        FirebaseDatabase.getInstance().getReference("User Information").
                child(FirebaseAuth.getInstance().getUid()).child("balance").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Integer num=snapshot.getValue(Integer.class);
                            Integer ans=num+10;

                            FirebaseDatabase.getInstance().getReference("User Information")
                                    .child(FirebaseAuth.getInstance().getUid()).child("balance")
                                    .setValue(ans);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference("Link Details")
                .child(key)
                .child("workerComplete")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            Integer num = snapshot.getValue(Integer.class);
                            // Toast.makeText(MainActivitySupport.this, ""+num, Toast.LENGTH_SHORT).show();
                            int ans =num+1;

                            FirebaseDatabase.getInstance().getReference("Link Details")
                                    .child(key)
                                    .child("workerNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Integer total =Integer.parseInt(snapshot.getValue(String.class));
                                            if(ans<total){
                                                FirebaseDatabase.getInstance().getReference("Link Details").child(key)

                                                        .child("workerComplete").setValue(ans).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                FirebaseDatabase.getInstance().getReference("Link Details")
                                                                        .child(key)
                                                                        .child("visibility").child(FirebaseAuth.getInstance().getUid()).setValue("invisible");
                                                            }
                                                        });
                                            }
                                            if(ans==total){
                                                FirebaseDatabase.getInstance().getReference("Link Details").child(key)

                                                        .child("workerComplete").setValue(ans);
                                                FirebaseDatabase.getInstance().getReference("Link Details")
                                                        .child(key)
                                                        .child("workingHistory").setValue("Done");
                                                FirebaseDatabase.getInstance().getReference("Link")
                                                        .child(key)
                                                        .child("workingHistoryColor").setValue("#008000");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void handlePointsAndNavigate() {
        s_second.setVisibility(View.GONE);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        keysList.add(key);
        saveKeysList(keysList);



        dialoguePage();
       // Toasty.success(MainActivitySupport.this, "Successfully you earn 10 Points", Toast.LENGTH_SHORT).show();
        countDownTimer.cancel();
        isRunning=false;
          if(siz==1){
              scheduleVisibilityAlarm();

          }



//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);


    }
    private void scheduleVisibilityAlarm() {
        long delayInMillis = 2*1000; // One day in millisecondsgetK


        ArrayList<String> strKeyLisy=getKeysList();


      Toast.makeText(this, " "+keysList.size(), Toast.LENGTH_SHORT).show();
//
//        for (String key : keysList) {
//            Toast.makeText(this, " "+key, Toast.LENGTH_SHORT).show();
//        }

        FirebaseDatabase.getInstance().getReference();
        Intent alarmIntent = new Intent(this, VisibilityAlarmReceiver.class);
        alarmIntent.putExtra("userUid", FirebaseAuth.getInstance().getUid());
        alarmIntent.putStringArrayListExtra("keysList", keysList);
       // alarmIntent.putExtra("keys", key);
       // Toast.makeText(getApplicationContext(), ""+key, Toast.LENGTH_SHORT).show();



        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayInMillis, pendingIntent);
    }

    @Override
    public void onBackPressed() {

        if (webView1.canGoBack()){
            webView1.goBack();
        }else{
            if (isRunning==true) {
                countDownTimer.cancel();
                isRunning=false;
                Toasty.info(MainActivitySupport.this, "পয়েন্ট এড হয়নি", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivitySupport.this,MainActivity.class);
                startActivity(intent);
                super.onBackPressed();
                finish();

            }
            else if(!isRunning){
                Intent intent=new Intent(MainActivitySupport.this,MainActivity.class);
                startActivity(intent);
                super.onBackPressed();
                finish();

            }
        }

//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            Toast.makeText(MainActivitySupport.this, "Timer canceled, points not added", Toast.LENGTH_SHORT).show();
//        }

    }

    private void saveKeysList(ArrayList<String> keysList) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert the ArrayList to a Set and store it in SharedPreferences
        editor.putStringSet("keysList", new HashSet<>(keysList));
        editor.apply();
    }

    // Retrieve the keysList from SharedPreferences
    private ArrayList<String> getKeysList() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Retrieve the Set from SharedPreferences and convert it back to ArrayList
        Set<String> keysSet = preferences.getStringSet("keysList", new HashSet<>());
        return new ArrayList<>(keysSet);
    }

}
