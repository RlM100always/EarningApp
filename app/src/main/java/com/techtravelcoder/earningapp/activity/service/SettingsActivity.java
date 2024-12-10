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
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techtravelcoder.earningapp.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText link,title;
    String selectedItem;
    private TextView calcPoints,subMitId;
    private ImageView image;
    int ans;
    Uri uri;
    int SELECT_PICTURE=200;
     AppCompatSpinner spinner;
     String val;
     //String selectedItem;
     DatabaseReference databaseReference;
     String s_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);


        title=findViewById(R.id.company_title_id);
        image=findViewById(R.id.select_image_id);
        link=findViewById(R.id.link_id);
        calcPoints=findViewById(R.id.point_calculate_id);
        subMitId=findViewById(R.id.submit_id);
        spinner=findViewById(R.id.worker_id);

        link.setOnClickListener(this);
        calcPoints.setOnClickListener(this);
        subMitId.setOnClickListener(this);

        s_uid=FirebaseAuth.getInstance().getUid();


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGalary();
            }
        });
        handleSpinner();

        FirebaseDatabase.getInstance().getReference("User Information").child(s_uid).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        val=snapshot.getValue(String.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        databaseReference=FirebaseDatabase.getInstance().getReference("Link Details");





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

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.submit_id){
            balanceCheck();
        }

    }


    private void handleSpinner(){

        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("How many traffic/visitor need ?");
        spinnerItems.add("50");
        spinnerItems.add("100");
        spinnerItems.add("200");
        spinnerItems.add("500");
        spinnerItems.add("1000");
        spinnerItems.add("2000");
        spinnerItems.add("5000");
        spinnerItems.add("10000");
        spinnerItems.add("20000");
        spinnerItems.add("50000");
        spinnerItems.add("100000");







        // Create an ArrayAdapter using the list of string items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedItem = parentView.getItemAtPosition(position).toString();
                if (!selectedItem.equals("How many traffic/visitor need ?")) {

                    try {
                        int pt = Integer.parseInt(selectedItem);
                         ans = pt * 10;

                        Toast.makeText(SettingsActivity.this, "Total " + ans + " Points", Toast.LENGTH_SHORT).show();
                        // Uncomment the following line if you want to set the text in calcPoints
                        calcPoints.setText("Total " + ans + " Points");

                    }catch (NumberFormatException e){
                        Toast.makeText(SettingsActivity.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                    }
                }
                if(selectedItem.equals("How many traffic/visitor need ?")){
                    calcPoints.setText("Toatal Needs 0 Points..");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });







    }

    private void submitValueInDataBase(){

        if(selectedItem.equals("How many traffic/visitor need ?")){
            calcPoints.setText("Total 0 Points");
            Toasty.success(SettingsActivity.this, "Please Select how many workers.", Toast.LENGTH_SHORT).show();

        }
        else {

            if( uri!=null && !TextUtils.isEmpty(link.getText().toString().trim()) && !TextUtils.isEmpty(title.getText().toString().trim()))
            {

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("✔✔ Submit Details ");
                progressDialog.setMessage("✔✔ Wait, Data is Loading..");
                progressDialog.show();
                String entryKey = databaseReference.push().getKey();
                String s_link = link.getText().toString();

                //insert
//                FirebaseDatabase.getInstance().getReference("User Information").child(FirebaseAuth.getInstance().getUid())
//                        .child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                int curret_bal = snapshot.getValue(Integer.class);
//                                curret_bal = curret_bal - ans;
//                                FirebaseDatabase.getInstance().getReference("User Information").child(FirebaseAuth.getInstance().getUid())
//                                        .child("balance").setValue(curret_bal);
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                //insert and retrive for single
                FirebaseDatabase.getInstance().getReference("Specefic User Details")
                        .child(s_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long listSize = snapshot.getChildrenCount() + 1;
                                int points = Integer.parseInt(selectedItem) * 10;

                                if (entryKey != null) {
                                    Calendar calendar = Calendar.getInstance();
                                    String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                                    Map<String, Object> entryValues = new HashMap<>();
                                    entryValues.put("points", String.valueOf(points));
                                    entryValues.put("uid", s_uid);
                                    entryValues.put("date", date);
                                    entryValues.put("workerNumber", (selectedItem));
                                    entryValues.put("count", String.valueOf(listSize));
                                    entryValues.put("status", "Pending");
                                    entryValues.put("postKey", entryKey);
                                    entryValues.put("backColor", "#FF6C6464");


                                    FirebaseDatabase.getInstance().getReference("Specefic User Details").child(s_uid)
                                            .child(entryKey).setValue(entryValues)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Handle success
                                                    progressDialog.dismiss();


                                                    // incrementPostCounter(entryKey,entryValues);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle failure
                                                    Toasty.error(SettingsActivity.this, "Failed to submit data", Toasty.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                //insert and retrive for all
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        long listSize = snapshot.getChildrenCount() + 1;
                        int points = Integer.parseInt(selectedItem) * 10;

                        if (entryKey != null) {
                            Calendar calendar = Calendar.getInstance();
                            Date times=calendar.getTime();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());

                            String date = sdf.format(times);

                            Map<String, Object> entryValues = new HashMap<>();
                            entryValues.put("link", s_link);
                            entryValues.put("points", String.valueOf(points));
                            entryValues.put("uid", s_uid);
                            entryValues.put("date", date);
                            entryValues.put("workerComplete", 0);
                            entryValues.put("workerNumber", (selectedItem));
                            entryValues.put("count", String.valueOf(listSize));
                            entryValues.put("names", val);
                            entryValues.put("status", "Pending");
                            entryValues.put("postKey", entryKey);
                            entryValues.put("backColor", "#FF6C6464");
                            entryValues.put("workingHistory", "Running");
                            entryValues.put("workingHistoryColor", "#FF0000");
                            entryValues.put("title", title.getText().toString());


                            // Use setValue() to store the values under the unique key
                            databaseReference.child(entryKey).setValue(entryValues)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Handle success
                                            progressDialog.dismiss();
                                            link.setText("");
                                            handleSpinner();
                                            title.setText("");
                                            image.setImageResource(R.drawable.company);
                                            dilogueShow();
                                            uploadImage(entryKey);

                                            // incrementPostCounter(entryKey,entryValues);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle failure
                                            Toasty.error(SettingsActivity.this, "Failed to submit data", Toasty.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });




                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
            else {
                Toasty.info(getApplicationContext(),"Title or Link or Image  is Missing....",Toasty.LENGTH_SHORT).show();
            }


        }

    }

    private void uploadImage(String entryKey) {

        String imagePath="Business/"+entryKey+"/image"+".jpg";
        StorageReference storageRef =  FirebaseStorage.getInstance().getReference().child(imagePath);

        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference("Link Details").child(entryKey).child("image").
                                setValue(uri.toString());

                    }
                });

            }
        });


    }

    private void dilogueShow() {
        AlertDialog.Builder alertObj= new AlertDialog.Builder(SettingsActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.buy_point_confirmation,null);
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

    public void  balanceCheck(){
        FirebaseDatabase.getInstance().getReference("User Information").child(FirebaseAuth.getInstance().getUid())
                .child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                          int curret_bal=snapshot.getValue(Integer.class);
                          if(curret_bal>=ans){
                              submitValueInDataBase();
                          }
                          else {
                              Toasty.error(getApplicationContext(),"Insufficient Balance",Toasty.LENGTH_SHORT).show();
                          }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }





}