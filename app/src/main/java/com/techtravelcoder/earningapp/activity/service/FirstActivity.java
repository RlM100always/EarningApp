package com.techtravelcoder.earningapp.activity.service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.activity.ads.SolveMathActivity;

public class FirstActivity extends AppCompatActivity {

    Button button;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ConstraintLayout marketing,buyPoint,guideline;
    private LinearLayout visitWeb;
    private LinearLayout supp,video,leader,notification;
    private TextView balance,marque ;
    Toolbar toolbar;
    String text;
    private LinearLayout solveMath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        navigationView=findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.draw_layout);
        toolbar = findViewById(R.id.tolbar);
        marketing=findViewById(R.id.marketing_id);
        buyPoint=findViewById(R.id.buy_point_id);
        supp=findViewById(R.id.ll_support_id);
        video=findViewById(R.id.video_id);
        balance=findViewById(R.id.user_balance_id);
        leader=findViewById(R.id.leaderboard_id);
        marque=findViewById(R.id.marque_first_id);
        notification=findViewById(R.id.notification_first_id);
        guideline=findViewById(R.id.guideline_id_first);
        solveMath=findViewById(R.id.first_solve_math_id);


        visitWeb=findViewById(R.id.visit_web);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.white);
        }
        getWindow().setStatusBarColor(color);

        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


      //  StartAppSDK.setTestAdsEnabled(true);

        FirebaseDatabase.getInstance().getReference("Control").child("Notification")
                .child("Home Page Text").child("text").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                             text = snapshot.getValue(String.class);
                            marque.setText(text);
                            marque.setSelected(true);
                            marque.setMarqueeRepeatLimit(-1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        solveMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVpnConnected(getApplicationContext())) {
                    Intent intent=new Intent(getApplicationContext(), SolveMathActivity.class);
                    startActivity(intent);                }
                else {
                    vpnConnectDialogue();
                }
            }
        });



        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NotificationActivity.class);
                startActivity(intent);
            }
        });

        guideline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GuideLineActivity.class);
                startActivity(intent);
            }
        });



        leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LeaderBoardActivity.class);
                startActivity(intent);

            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/@SelfMeTeam?si=uI6EP5Nvn4y8v8uL"));
                startActivity(intent);
            }
        });

        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SupportActivity.class);
                startActivity(intent);
            }
        });

        visitWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        marketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ControlSettingsActivity.class);
                startActivity(intent);
            }
        });

        buyPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });

        navItemOnClickListener();
        FirebaseDatabase.getInstance().getReference("User Information")
                .child(FirebaseAuth.getInstance().getUid())
                .child("balance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Integer bal = snapshot.getValue(Integer.class);
                            balance.setText("Balance: " + bal);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
        // Set the three-bar menu icon color

    }

    private void vpnConnectDialogue() {
        AlertDialog.Builder alertObj= new AlertDialog.Builder(FirstActivity.this);

        alertObj.setTitle("❌❌  VPN Connect নেই");
        alertObj.setMessage("✅✅ PlayStore থেকে ভিপিএন ডাউনলোড করে , USA or UK or Canada Country Select  করুন।");
        alertObj.setCancelable(false);

        alertObj.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = alertObj.create();
        dialog.show();



        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_dialogue_back);
        dialog.getWindow().setBackgroundDrawable(drawable);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isVpnConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }

        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
    }
    private void setMenuCategoryColor(MenuItem menuItem,int textSizeInSp){
        SpannableString s = new SpannableString(menuItem.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        s.setSpan(new AbsoluteSizeSpan(textSizeInSp, true), 0, s.length(), 0);

        menuItem.setTitle(s);
    }
    public void navItemOnClickListener(){

        Menu menu=navigationView.getMenu();
        MenuItem menuItem=menu.findItem(R.id.product_id);
        setMenuCategoryColor(menuItem,16);

        MenuItem menuItem1=menu.findItem(R.id.earn_money_id);
        setMenuCategoryColor(menuItem1,16);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

    }




}