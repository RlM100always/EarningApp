package com.techtravelcoder.admin1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.admin1.R;
import com.techtravelcoder.admin1.activity.MainActivity;
import com.techtravelcoder.admin1.model.CampainModel;
import com.techtravelcoder.admin1.model.UserModel;

import java.util.ArrayList;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    ArrayList<UserModel> list;

    public UserAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_design,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        UserModel userModel=list.get(position);

        SpannableStringBuilder nameBuilder = new SpannableStringBuilder("⚫️ Name :  " + userModel.getName());
        nameBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        // Create a SpannableStringBuilder for Gmail
        SpannableStringBuilder gmailBuilder = new SpannableStringBuilder("⚫️ Gmail :  " + userModel.getGmail());
        gmailBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableStringBuilder balanceBuilder = new SpannableStringBuilder("⚫️Balance :  " );
        gmailBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.name.setText(nameBuilder);
        holder.gmail.setText(gmailBuilder);

        holder.status.setText(userModel.getUserStatus());
        holder.status.setBackgroundColor(Color.parseColor(userModel.getStatusColor()));


        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlAccount(userModel);
            }
        });


    }

    private void updateUserStatus(String userId, String newStatus, String newStatusColor) {
        // Update user status in the database
        FirebaseDatabase.getInstance().getReference("User Information")
                .child(userId)
                .child("userStatus")
                .setValue(newStatus);

        // Update status color
        FirebaseDatabase.getInstance().getReference("User Information")
                .child(userId)
                .child("statusColor")
                .setValue(newStatusColor);
        FirebaseAuth.getInstance().signOut();


    }
    public  void controlAccount(UserModel userModel){
        if(userModel.getUserStatus().equals("Active")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("Are you want disable or delete this request ??");
            builder.setTitle("Account Control");

            builder.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    updateUserStatus(userModel.getUid(), "Disable", "#FF0000");

                    Intent intent=new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                   // ((Activity) context).finish();
                    dialog.cancel();

                }
            });

            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAccount(userModel);
                }
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        if(userModel.getUserStatus().equals("Disable")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("Are you want Active or delete this request ??");
            builder.setTitle("Account Control");
            builder.setPositiveButton("Active", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    updateUserStatus(userModel.getUid(), "Active", "#355E3B");

                    Intent intent=new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                   // ((Activity) context).finish();
                    dialog.cancel();

                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   deleteAccount(userModel);

                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }


    }

    private void deleteAccount(UserModel userModel){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Account will be delete Permanently..");
        builder.setTitle("Delete Account..");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseDatabase.getInstance().getReference("User Information").
                        child(userModel.getUid()).removeValue();
//                Intent intent=new Intent(context, MainActivity.class);
//                context.startActivity(intent);
//                ((Activity) context).finish();
                dialog.cancel();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,gmail,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_id);
            gmail=itemView.findViewById(R.id.gmail_id);
            status=itemView.findViewById(R.id.status_check_id);

        }
    }
}
