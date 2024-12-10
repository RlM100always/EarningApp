package com.techtravelcoder.admin1.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin1.R;
import com.techtravelcoder.admin1.adapter.TrnxDetailsAdapter;
import com.techtravelcoder.admin1.model.TrnxModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransectionDetailsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TrnxModel trnxModel;
    private TrnxDetailsAdapter trnxDetailsAdapter;
    ArrayList<TrnxModel> list;
    private DatabaseReference mbase;
    private AppCompatSpinner serachCategory;
    private ArrayList<String> spinnerItem;
    ArrayList<TrnxModel>filteredListRequestNumber;
    ArrayList<TrnxModel>filteredListName;
    ArrayList<TrnxModel>filteredListDate;
    ArrayList<TrnxModel>filteredListTrnx;
    private TextView all,today,yesterday,change,days7,days30,earning;
    double earn_number=0 ;



    EditText edit ;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transection_details);
        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);



        all=findViewById(R.id.alldata_id);
        yesterday=findViewById(R.id.yesterday_id);
        change=findViewById(R.id.change_id);
        days7=findViewById(R.id.last_7_days);
        days30=findViewById(R.id.last_30_days);
        today=findViewById(R.id.today_id);
        earning=findViewById(R.id.earning_id);

        recyclerView=findViewById(R.id.trnx_recycler_view);
        serachCategory=findViewById(R.id.search_spinner);
        edit=findViewById(R.id.edit_id);
        setSpinnerValue();




        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Wait User Information is Loading...");
        progressDialog.show();
        mbase = FirebaseDatabase.getInstance().getReference("Payment Details");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        allRetriveData();

        days7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earning.setVisibility(View.VISIBLE);
                earn_number=0;
                edit.getText().clear();
                change.setText("Last 7 days Data");
                filterDataForLastNDays(list,7);

            }
        });
        days30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earning.setVisibility(View.VISIBLE);
                earn_number=0;
                edit.getText().clear();
                change.setText("Last 30 days Data");
                filterDataForLastNDays(list,30);

            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earning.setVisibility(View.VISIBLE);
                earn_number=0;
                edit.getText().clear();
                change.setText("Todays data");
                filterDataForToday(list);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earning.setVisibility(View.VISIBLE);
                earn_number=0;
                edit.getText().clear();
                change.setText("All data");
                allRetriveData();
            }
        });
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earning.setVisibility(View.VISIBLE);
                earn_number=0;
                edit.getText().clear();
                change.setText("Yesterday data");
                filterDataForYesterday(list);
            }
        });


    }
    private void allRetriveData(){

        trnxDetailsAdapter=new TrnxDetailsAdapter(this,list);
        recyclerView.setAdapter(trnxDetailsAdapter );
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                if(snapshot.exists()){
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        trnxModel = dataSnapshot.getValue(TrnxModel.class);
                        if(trnxModel != null){
                            list.add(trnxModel);
                            if(trnxModel.getStatus().equals("Approve")){
                                calculateHowMuchEarn();
                            }
                        }
                    }
                    filteredListRequestNumber = new ArrayList<>(list);
                    filteredListName = new ArrayList<>(list);
                    filteredListDate=new ArrayList<>(list);
                    filteredListTrnx = new ArrayList<>(list);





                }
                earning.setText("Total Earning "+earn_number+" Taka");
                earningVisibilityGone();
                trnxDetailsAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setSpinnerValue(){
        spinnerItem=new ArrayList<>();

        spinnerItem.add("✔ Searh by name");
        spinnerItem.add("✔ Searh by date");
        spinnerItem.add("✔ Searh by request number");
        spinnerItem.add("✔ Searh by trnxid");




        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item ,spinnerItem);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        serachCategory.setAdapter(arrayAdapter);
        serachCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                type=parent.getItemAtPosition(position).toString();
                searchControl(type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void editTextListener(String variable){
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString(),variable);
            }

        });

    }
    public void searchControl(String type){

        if(type.equals("✔ Searh by request number")){
         //   edit.setHint(type);
            editTextListener("filteredListRequestNumber");
        }
        if(type.equals("✔ Searh by name")){
          //  edit.setHint(type);
            editTextListener("filteredListName");
        }
        if(type.equals("✔ Searh by date")){
          //  edit.setHint(type);
            editTextListener("filteredListDate");
        }
        if(type.equals("✔ Searh by trnxid")){
           // edit.setHint(type);
            editTextListener("filteredListTrnx");
        }


    }

    private void calculateHowMuchEarn(){
        if(trnxModel.getBalancePoints().equals("2000 point 50Tk")){
            earn_number=earn_number+50;
        }
        if(trnxModel.getBalancePoints().equals("4000 point 100Tk")){
            earn_number=earn_number+100;

        }
        if(trnxModel.getBalancePoints().equals("8000 point 200Tk")){
            earn_number=earn_number+200;

        }
        if(trnxModel.getBalancePoints().equals("12000 point 250Tk")){
            earn_number=earn_number+250;

        }
        if(trnxModel.getBalancePoints().equals("20000 point 400Tk")){
            earn_number=earn_number+400;
        }
        if(trnxModel.getBalancePoints().equals("50000 point 1000Tk")){
            earn_number=earn_number+1000;
        }
        if(trnxModel.getBalancePoints().equals("100000 point 1500Tk")){
            earn_number=earn_number+1500;
        }

    }

    private void filterDataForToday(List<TrnxModel> data) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());

        String todayDate = sdf.format(today);

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                if(snapshot.exists()){
                  //  progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        trnxModel = dataSnapshot.getValue(TrnxModel.class);
                        if(trnxModel != null){
                            if(trnxModel.getDate().equals(todayDate)){
                                data.add(trnxModel);
                                if(trnxModel.getStatus().equals("Approve")){
                                    calculateHowMuchEarn();
                                }

                            }
                        }
                    }
                    updateFilterList((ArrayList<TrnxModel>) data);

                }
                earning.setText("Total Earning "+earn_number+" Taka");
                earningVisibilityGone();
                trnxDetailsAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void earningVisibilityGone(){
        Handler handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               earning.setVisibility(View.GONE);
            }
        },5000);
    }
    private void filterDataForYesterday(List<TrnxModel> data) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());

        String yesterdayDate = sdf.format(yesterday);
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                data.clear();

                if(snapshot.exists()){
                    //  progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        trnxModel = dataSnapshot.getValue(TrnxModel.class);
                        if(trnxModel != null){
                            if(trnxModel.getDate().equals(yesterdayDate)){
                                data.add(trnxModel);
                                if(trnxModel.getStatus().equals("Approve")){
                                    calculateHowMuchEarn();
                                }

                            }
                        }
                    }
                    updateFilterList((ArrayList<TrnxModel>) data);


                }
                earning.setText("Total Earning "+earn_number+" Taka");
                earningVisibilityGone();

                trnxDetailsAdapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void filterDataForLastNDays(List<TrnxModel> data, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -numberOfDays);
        Date startDate = calendar.getTime();

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        trnxModel = dataSnapshot.getValue(TrnxModel.class);
                        if (trnxModel != null) {
                            String transactionDate = trnxModel.getDate();
                            if (transactionDate != null && isDateInRange(transactionDate, startDate)) {
                                data.add(trnxModel);
                                if(trnxModel.getStatus().equals("Approve")){
                                    calculateHowMuchEarn();
                                }
                            }
                        }
                    }

                    updateFilterList((ArrayList<TrnxModel>) data);
                }
                earning.setText("Total Earning "+earn_number+" Taka");
                earningVisibilityGone();
                trnxDetailsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private boolean isDateInRange(String transactionDate, Date startDate) {
        // Assuming transactionDate is in the format "dd MMMM yyyy,EEEE"
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());

        try {
            Date date = sdf.parse(transactionDate);
            // Check if parsed date is after or equal to startDate
            return date.after(startDate) || date.equals(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateFilterList(ArrayList<TrnxModel> data){
        filteredListRequestNumber = new ArrayList<>(data);
        filteredListName = new ArrayList<>(data);
        filteredListDate = new ArrayList<>(data);
        filteredListTrnx = new ArrayList<>(data);
        trnxDetailsAdapter.filterList(data);

    }


    private void filter (String text,String var){
        List<TrnxModel> flist= new ArrayList<>();

        if(var.equals("filteredListRequestNumber")){
            for(TrnxModel obj : filteredListRequestNumber ){
                if(obj.getCount().toLowerCase().contains(text.toLowerCase())){
                    flist.add(obj);
                }
            }
            trnxDetailsAdapter.filterList((ArrayList<TrnxModel>) flist);
            list.clear();

        }
        if(var.equals("filteredListTrnx")){
            for(TrnxModel obj : filteredListTrnx ){
                if(obj.getTrnxId().toLowerCase().contains(text.toLowerCase())){
                    flist.add(obj);
                }
            }
            trnxDetailsAdapter.filterList((ArrayList<TrnxModel>) flist);
        }
        if(var.equals("filteredListName")){
            for(TrnxModel obj : filteredListName ){
                if(obj.getNames().toLowerCase().contains(text.toLowerCase())){
                    flist.add(obj);
                }
            }
            trnxDetailsAdapter.filterList((ArrayList<TrnxModel>) flist);
        }

        if(var.equals("filteredListDate")){
            for(TrnxModel obj : filteredListDate ){
                if(obj.getDate().toLowerCase().contains(text.toLowerCase())){
                    flist.add(obj);
                }
            }
            trnxDetailsAdapter.filterList((ArrayList<TrnxModel>) flist);
        }


    }
}