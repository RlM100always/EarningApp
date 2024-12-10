package com.techtravelcoder.earningapp.activity.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.activity.service.FirstActivity;
import com.techtravelcoder.earningapp.activity.service.MainActivity;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;
     private TextView login,signup ;
     private FirebaseAuth mAuth;
     String currentStatus="";
     private TextInputEditText te_gmail,te_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        login=findViewById(R.id.login_button_click);
        signup=findViewById(R.id.login_signup_id);
        te_gmail=findViewById(R.id.login_email_id);
        te_password=findViewById(R.id.login_password_id);

        progressDialog1=new ProgressDialog(this);
        progressDialog1.setCancelable(false);
        progressDialog1.setTitle("Loading...");
        progressDialog1.setMessage("Wait your account is loading...");



        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        te_gmail.setOnClickListener(this);
        te_password.setOnClickListener(this);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();




        if(mAuth.getCurrentUser() != null)
        {
            progressDialog1.show();

            FirebaseDatabase.getInstance().getReference("User Information").child(FirebaseAuth.getInstance().getUid()).child("userStatus")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            currentStatus=snapshot.getValue(String.class);
                            if(currentStatus==null){
                                currentStatus="1";
                            }
                            //Toast.makeText(LoginActivity.this, ""+currentStatus, Toast.LENGTH_SHORT).show();
                            newMethod(currentStatus);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



            }

        }


     //   Toast.makeText(this, ""+currentStatus, Toast.LENGTH_SHORT).show();



    public void newMethod(String currentStatus){

        progressDialog1.dismiss();
        if(currentStatus.equals("Disable")){
            Toasty.error(getApplicationContext(),"Admin Block your ID",Toasty.LENGTH_SHORT).show();

        }
         if(currentStatus.equals("Active") ) {

            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.login_signup_id){
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId()==R.id.login_button_click){
            userLogin();
        }
    }

    private void userLogin() {
      //  Toast.makeText(this, ""+currentStatus, Toast.LENGTH_SHORT).show();


        if(currentStatus.equals("Disable")){
            Toasty.error(getApplicationContext(),"Admin Block your ID",Toasty.LENGTH_SHORT).show();

        }
        if(currentStatus.equals("") || currentStatus.equals("Active")) {

            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.background);
            progressDialog.getWindow().setBackgroundDrawable(drawable);
            progressDialog.setMessage("✔ Login is Processing....");
            progressDialog.setTitle("✔ Please Wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();

            String email, password;
            email = te_gmail.getText().toString();
            password = te_password.getText().toString();

            if (email.isEmpty()) {
                te_gmail.setError("Enter an email Address");
                te_gmail.requestFocus();
                progressDialog.dismiss();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                te_gmail.setError("Enter a Valid email Address");
                te_gmail.requestFocus();
                progressDialog.dismiss();
                return;
            }
            if (password.isEmpty()) {
                te_password.setError("Enter a Password ");
                te_password.requestFocus();
                progressDialog.dismiss();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // user.isEmailVerified()
                        if (user != null ) {
                            Toasty.success(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT, true).show();

                            Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                            startActivity(intent);
                            finish();
                        } //else {
//                            Toasty.error(getApplicationContext(), "Please verify your email first.", Toast.LENGTH_LONG, true).show();
//                            mAuth.signOut();
//                        }
                    } else {
                        Toasty.error(getApplicationContext(), "Something wrong or Internet Issue..", Toast.LENGTH_SHORT, true).show();
                    }
                }
            });

        }
        if (currentStatus == "1") {
            Toasty.info(getApplicationContext(), "You have no account , Create an account", Toasty.LENGTH_SHORT).show();
            Toasty.error(getApplicationContext(), "Admin Delete your Account ", Toasty.LENGTH_SHORT).show();
            Toasty.info(getApplicationContext(), "If Login Problem ,Clear Your App Data", Toasty.LENGTH_SHORT).show();


        }

    }


}