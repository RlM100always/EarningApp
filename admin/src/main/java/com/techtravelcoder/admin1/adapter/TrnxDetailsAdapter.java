package com.techtravelcoder.admin1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin1.R;
import com.techtravelcoder.admin1.activity.ImageActivity;
import com.techtravelcoder.admin1.activity.MainActivity;
import com.techtravelcoder.admin1.model.TrnxModel;

import java.util.ArrayList;

public class TrnxDetailsAdapter extends RecyclerView.Adapter<TrnxDetailsAdapter.MyViewHolder> {
    Context context;
    ArrayList<TrnxModel> list;

    public TrnxDetailsAdapter(Context context, ArrayList<TrnxModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrnxDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.trnx_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrnxDetailsAdapter.MyViewHolder holder, int position) {
        TrnxModel trnxModel=list.get(position);

        holder.someText.setText("⚫️ Request Number :  "+trnxModel.getCount()+""+"\n⚫️ Name :  "+trnxModel.getNames()+""+"\n⚫️ Details : "+trnxModel.getBalancePoints()+"\n⚫️ Date :  "+trnxModel.getDate()+"\n⚫️ Trnx :  "+trnxModel.getTrnxId());
        holder.stat.setText(trnxModel.getStatus());
        holder.stat.setBackgroundColor(Color.parseColor(trnxModel.getPayColor()));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent=new Intent(context, ImageActivity.class);
                  intent.putExtra("imageString",trnxModel.getImage());
                  context.startActivity(intent);
            }
        });

        holder.stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlAccount(trnxModel,holder.editText);
            }
        });
    }

    public void filterList(ArrayList<TrnxModel> filterlist){
        list=filterlist;
        notifyDataSetChanged();

    }

    private void balanceUpdate(int number,TrnxModel trnxModel,int nums){

        if(nums==1){

            FirebaseDatabase.getInstance().getReference("User Information").
                    child(trnxModel.getUid()).child("balance").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Integer num=snapshot.getValue(Integer.class);
                                Integer ans=num+number;

                                FirebaseDatabase.getInstance().getReference("User Information")
                                        .child(trnxModel.getUid()).child("balance")
                                        .setValue(ans);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//            Intent intent=new Intent(context, MainActivity.class);
//            context.startActivity(intent);
//            ((Activity)context).finish();

        }
        if(nums==2){
            FirebaseDatabase.getInstance().getReference("User Information").
                    child(trnxModel.getUid()).child("balance").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Integer num=snapshot.getValue(Integer.class);
                                Integer ans=num-number;

                                FirebaseDatabase.getInstance().getReference("User Information")
                                        .child(trnxModel.getUid()).child("balance")
                                        .setValue(ans);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//            Intent intent=new Intent(context, MainActivity.class);
//            context.startActivity(intent);
//            ((Activity)context).finish();

        }



    }
    public  void controlAccount(TrnxModel trnxModel,EditText editText){
        if(trnxModel.getStatus().equals("Pending")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("✔ Are you want ✅✅Approve✅✅ this balance request ??");
            builder.setTitle("✔ Account Balance ....");

            builder.setPositiveButton("❌ No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });

            builder.setNegativeButton("✔ Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  //  editText.getText().clear();
                    FirebaseDatabase.getInstance().getReference("Payment Details")
                            .child(trnxModel.getPostKey()).child("status")
                            .setValue("Approve");
                    FirebaseDatabase.getInstance().getReference("Payment Details")
                            .child(trnxModel.getPostKey()).child("payColor")
                            .setValue("#85cc18");

                    if(trnxModel.getBalancePoints().equals("2000 point 50Tk")){
                           balanceUpdate(2000,trnxModel,1);
                    }
                    if(trnxModel.getBalancePoints().equals("4000 point 100Tk")){
                        balanceUpdate(4000,trnxModel,1);

                    }
                    if(trnxModel.getBalancePoints().equals("8000 point 200Tk")){
                        balanceUpdate(8000,trnxModel,1);

                    }
                    if(trnxModel.getBalancePoints().equals("12000 point 250Tk")){
                        balanceUpdate(12000,trnxModel,1);

                    }
                    if(trnxModel.getBalancePoints().equals("20000 point 400Tk")){
                        balanceUpdate(20000,trnxModel,1);
                    }
                    if(trnxModel.getBalancePoints().equals("50000 point 1000Tk")){
                        balanceUpdate(50000,trnxModel,1);
                    }
                    if(trnxModel.getBalancePoints().equals("100000 point 1500Tk")){
                        balanceUpdate(10000,trnxModel,1);
                    }


                }
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();



        }
        if(trnxModel.getStatus().equals("Approve")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("✔ Are you want ❌❌Reject❌❌  this balance request ??");
            builder.setTitle("✔ Account Balance ....");

            builder.setPositiveButton("❌ No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });

            builder.setNegativeButton("✔ Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase.getInstance().getReference("Payment Details")
                            .child(trnxModel.getPostKey()).child("status")
                            .setValue("Pending");
                    FirebaseDatabase.getInstance().getReference("Payment Details")
                            .child(trnxModel.getPostKey()).child("payColor")
                            .setValue("#FF6C6464");

                    if(trnxModel.getBalancePoints().equals("2000 point 50Tk")){
                        balanceUpdate(2000,trnxModel,2);
                    }
                    if(trnxModel.getBalancePoints().equals("4000 point 100Tk")){
                        balanceUpdate(4000,trnxModel,2);

                    }
                    if(trnxModel.getBalancePoints().equals("8000 point 200Tk")){
                        balanceUpdate(8000,trnxModel,2);

                    }
                    if(trnxModel.getBalancePoints().equals("12000 point 250Tk")){
                        balanceUpdate(12000,trnxModel,2);

                    }
                    if(trnxModel.getBalancePoints().equals("20000 point 400Tk")){
                        balanceUpdate(20000,trnxModel,2);
                    }
                    if(trnxModel.getBalancePoints().equals("50000 point 1000Tk")){
                        balanceUpdate(50000,trnxModel,2);
                    }
                    if(trnxModel.getBalancePoints().equals("100000 point 1500Tk")){
                        balanceUpdate(10000,trnxModel,2);
                    }


                }
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView someText ,stat,image;
        EditText editText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            someText=itemView.findViewById(R.id.some_id);
            stat=itemView.findViewById(R.id.payment_check_id);
            editText=itemView.findViewById(R.id.edit_id);
            image=itemView.findViewById(R.id.payment_image_id);


        }
    }
}
