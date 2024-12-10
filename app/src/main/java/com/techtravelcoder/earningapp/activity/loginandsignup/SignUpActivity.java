package com.techtravelcoder.earningapp.activity.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.model.UserModel;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    String s_uid;

    private TextView alreadySign ,signButton;
    private TextInputEditText name ,gmail,password ;
     private FirebaseAuth auth;
     private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        auth=FirebaseAuth.getInstance();


        alreadySign=findViewById(R.id.already_sign_up);
        name=findViewById(R.id.signup_name_id);
        gmail=findViewById(R.id.signup_email_id);
        password=findViewById(R.id.signup_password_id);
        signButton=findViewById(R.id.sign_button_click);


        alreadySign.setOnClickListener(this);
        name.setOnClickListener(this);
        gmail.setOnClickListener(this);
        password.setOnClickListener(this);
        signButton.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);





    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.already_sign_up){
            Intent intent =new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.sign_button_click){
            signUpAuthentication();
        }
    }

    public void signUpAuthentication(){

          String s_name =name.getText().toString().trim();
          String s_gmail =gmail.getText().toString().trim();
          String s_password =password.getText().toString().trim();
          int balance =0 ;





        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        progressDialog.getWindow().setBackgroundDrawable(drawable);
        progressDialog.setMessage(" ℹ️ℹ️ Registering user...");
        progressDialog.show();

        if (s_name.isEmpty() || s_gmail.isEmpty() || s_password.isEmpty() ) {
            progressDialog.dismiss();

            Toasty.info(this, "All fields are Required... ℹ️ℹ️", Toast.LENGTH_SHORT, true).show();
            return;
        }



        auth.createUserWithEmailAndPassword(s_gmail, s_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss(); // Dismiss the ProgressDialog once registration is complete

                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();

                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                            if (emailTask.isSuccessful()) {

                                Toasty.success(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT, true).show();
                                Toasty.info(SignUpActivity.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT, true).show();


                                //  Toasty.info(SignUpActivity.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT, true).show();
                                s_uid= auth.getCurrentUser().getUid();
                                UserModel userModel=new UserModel(s_name,s_gmail,s_password,s_uid,balance);
                                addUserDataInDatabase(userModel);

                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toasty.error(SignUpActivity.this, "⚠️⚠️ Error sending verification email.", Toast.LENGTH_SHORT, true).show();
                            }
                        });
                    }
                } else {

                    Toasty.error(getApplicationContext(), "⚠️⚠️ Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
        });

    }

    public void addUserDataInDatabase(UserModel userModel ){
       // Toast.makeText(getApplicationContext(), ""+s_uid, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference("User Information").child(s_uid).
                setValue(userModel);
        //#355E3B
        FirebaseDatabase.getInstance().getReference("User Information").child(s_uid).
                child("userStatus").setValue("Active");
        FirebaseDatabase.getInstance().getReference("User Information").child(s_uid).
                child("statusColor").setValue("#355E3B");

        FirebaseDatabase.getInstance().getReference("User Balance")
                .child(s_uid).child("balance")
                .setValue(000);
    }
}