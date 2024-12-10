package com.techtravelcoder.admin1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techtravelcoder.admin1.R;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private LinearLayout campain,transection,userDetails,adds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        mAuth=FirebaseAuth.getInstance();
        onLoginButtonClick();

        campain=findViewById(R.id.campain_id);
        transection=findViewById(R.id.transection_id);
        userDetails=findViewById(R.id.user_id);
        adds=findViewById(R.id.add_id);


        adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdsControlActivity.class);
                startActivity(intent);
            }
        });


        campain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CampainingActivity.class);
                startActivity(intent);
            }
        });

        transection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TransectionDetailsActivity.class);
                startActivity(intent);
            }
        });

        userDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onLoginButtonClick() {
        String email = "bijoy18rakib@gmail.com";
        String password = "123456";


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}