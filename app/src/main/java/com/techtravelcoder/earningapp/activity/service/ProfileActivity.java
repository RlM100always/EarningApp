package com.techtravelcoder.earningapp.activity.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.activity.loginandsignup.LoginActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    AppCompatSpinner spinner;
    TextView bal,all_use_text;
    boolean trnxIdExists = false;
    private int SELECT_PICTURE=300 ;
    Uri uri ;


    String pay;
    private EditText trnxId ;
    String s_uid,val;
    private ImageView image;

    private TextView logout,submit;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        image=findViewById(R.id.profile_image_id);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

      //  logout=findViewById(R.id.log_out_id);
        bal=findViewById(R.id.balace);
        submit=findViewById(R.id.submit_id);
        trnxId=findViewById(R.id.trnx_id);
        all_use_text=findViewById(R.id.al_ready_use_text_id);

        s_uid=FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference("User Information")
                        .child(FirebaseAuth.getInstance().getUid()).child("balance")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer val=snapshot.getValue(Integer.class);
                                bal.setText(String.valueOf(val));


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        handleSpinner();

        FirebaseDatabase.getInstance().getReference("User Information").child(s_uid).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        val=snapshot.getValue(String.class);
                        //Toast.makeText(ProfileActivity.this, ""+val, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        databaseReference=FirebaseDatabase.getInstance().getReference("Payment Details");


//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logOutConfirm();
//            }
//        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGalary();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPaymentData();
            }
        });
    }


    public void handleSpinner(){

        List<String> dataList = Arrays.asList("➕ Add Points","2000 point 50Tk", "4000 point 100Tk", "8000 point 200Tk","12000 point 250Tk","20000 point 400Tk","50000 point 1000Tk","100000 point 1500Tk");
        spinner=findViewById(R.id.add_points_id);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle item selection
                pay = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item ,dataList);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(arrayAdapter);
    }


    private void addPaymentData() {


        String trnxDetails = trnxId.getText().toString();

        // Assuming databaseReference is a reference to your Firebase database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Payment Details");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean trnxIdExists = false;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String existingTrnxId = dataSnapshot.child("trnxId").getValue(String.class);

                    if (existingTrnxId != null && existingTrnxId.equals(trnxDetails)) {
                        trnxIdExists = true;
                        break;
                    }
                }

                if (trnxIdExists) {
                    all_use_text.setVisibility(View.VISIBLE);
                    Toasty.info(ProfileActivity.this, "This TransactionId is already used..", Toast.LENGTH_SHORT).show();
                } else {
                    if (pay.equals("➕ Add Points") || trnxDetails.isEmpty() || uri ==null) {
                        Toasty.info(getApplicationContext(), "Something is Missing , Please fillup all information !!!", Toasty.LENGTH_SHORT).show();
                    } else {

                        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                        progressDialog.setTitle("✔✔ Payment Details ");
                        progressDialog.setMessage("✔✔ Wait, Data is Loading..");
                        progressDialog.show();

                        String entryKey = databaseReference.push().getKey();
                        uploadImage(entryKey);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long listSize = snapshot.getChildrenCount() + 1;

                                if (entryKey != null) {
                                    Calendar calendar = Calendar.getInstance();
                                    Date currentDate = calendar.getTime();

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                                    String date = sdf.format(currentDate);

                                    //String date = DateFormat.getDateInstance(DateFormat.).format(calendar.getTime());

                                    Map<String, Object> entryValues = new HashMap<>();
                                    entryValues.put("uid", FirebaseAuth.getInstance().getUid());
                                    entryValues.put("date", date);
                                    entryValues.put("count", String.valueOf(listSize));
                                    entryValues.put("names", val);
                                    entryValues.put("status", "Pending");
                                    entryValues.put("postKey", entryKey);
                                    entryValues.put("payColor", "#FF6C6464");
                                    entryValues.put("trnxId", trnxDetails);
                                    entryValues.put("balancePoints",pay);

                                    // Use setValue() to store the values under the unique key
                                    databaseReference.child(entryKey).setValue(entryValues)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();
                                                    trnxId.setText("");
                                                    handleSpinner();
                                                    image.setImageResource(R.drawable.payment_prove);
                                                    dilogueShow();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toasty.error(ProfileActivity.this, "Failed to submit data", Toasty.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                                Toasty.error(ProfileActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void selectImageFromGalary() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    private void uploadImage(String entryKey){
        String imagePath="Payment/"+entryKey+"/image"+".jpg";
        StorageReference storageRef =  FirebaseStorage.getInstance().getReference().child(imagePath);

        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference("Payment Details").child(entryKey).child("image").
                                setValue(uri.toString());

                    }
                });

            }
        });
    }

    private void dilogueShow() {
        AlertDialog.Builder alertObj= new AlertDialog.Builder(ProfileActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.give_money,null);
        TextView ok=view.findViewById(R.id.ok_id);
        alertObj.setView(view);
        AlertDialog dialog = alertObj.create();
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_dialogue_back);
        dialog.getWindow().setBackgroundDrawable(drawable);
    }




}