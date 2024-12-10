package com.techtravelcoder.admin1.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin1.R;
import com.techtravelcoder.admin1.model.CampainModel;

import java.util.List;


public class CampainAdapter extends RecyclerView.Adapter<CampainAdapter.MyViewHolder> {


    Context context;
    List<CampainModel> list;


    public CampainAdapter(Context context, List<CampainModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CampainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.campain_design,parent,false);
        return new MyViewHolder(view);
    }

    public static void copyToClipboard(Context context, String textToCopy) {
        // Get the ClipboardManager
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // Create a ClipData object to hold the text
        ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);

        // Set the ClipData to the clipboard
        clipboard.setPrimaryClip(clip);

        // Show a toast message to indicate successful copying
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBindViewHolder(@NonNull CampainAdapter.MyViewHolder holder, int position) {
        CampainModel campainModel=  list.get(position);

        holder.pending.setText(campainModel.getStatus());
        holder.pending.setBackgroundColor(Color.parseColor(campainModel.getBackColor()));
        holder.copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(context,campainModel.getLink());

            }
        });
        holder.setAll.setText("⚫️ Serial Number :  "+campainModel.getCount()+"\n"+"⚫️ Name :  "+campainModel.getNames()+"\n⚫️ Workers Need :  "+campainModel.getWorkerNumber()+"\n⚫️ Total Cost :  "+campainModel.getPoints()+" points "+"\n⚫️ Date :  "+campainModel.getDate());

        holder.pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(campainModel);
            }
        });

    }

    private void rejectUpdate(CampainModel campainModel){
        FirebaseDatabase.getInstance().getReference("Link Details").
                child(campainModel.getPostKey()).child("status")
                .setValue("Reject");
        FirebaseDatabase.getInstance().getReference("Link Details").
                child(campainModel.getPostKey()).child("backColor")
                .setValue("#FF0000");

        FirebaseDatabase.getInstance().getReference("Specefic User Details").
                child(campainModel.getUid()).
                child(campainModel.getPostKey()).child("status")
                .setValue("Reject");
        FirebaseDatabase.getInstance().getReference("Specefic User Details").
                child(campainModel.getUid()).
                child(campainModel.getPostKey()).child("backColor")
                .setValue("#FF0000");

        FirebaseDatabase.getInstance().getReference("User Information").child(campainModel.getUid())
                .child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int bal=snapshot.getValue(Integer.class);
                        int up_bal=bal+Integer.parseInt(campainModel.getPoints());
                        Toast.makeText(context, ""+up_bal+"   "+bal+"   "+campainModel.getPoints(), Toast.LENGTH_LONG).show();
                        FirebaseDatabase.getInstance().getReference("User Information").child(campainModel.getUid())
                                .child("balance").setValue(up_bal);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void approveUpdate(CampainModel campainModel){

        FirebaseDatabase.getInstance().getReference("Link Details").
                child(campainModel.getPostKey()).child("status")
                .setValue("Approve");
        FirebaseDatabase.getInstance().getReference("Link Details").
                child(campainModel.getPostKey()).child("backColor")
                .setValue("#85cc18");

        FirebaseDatabase.getInstance().getReference("Specefic User Details").
                child(campainModel.getUid()).
                child(campainModel.getPostKey()).child("status")
                .setValue("Approve");
        FirebaseDatabase.getInstance().getReference("Specefic User Details").
                child(campainModel.getUid()).
                child(campainModel.getPostKey()).child("backColor")
                .setValue("#85cc18");
        FirebaseDatabase.getInstance().getReference("User Information").child(campainModel.getUid())
                .child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int bal=snapshot.getValue(Integer.class);
                        int up_bal=bal-Integer.parseInt(campainModel.getPoints());
                        Toast.makeText(context, ""+up_bal+"   "+bal+"   "+campainModel.getPoints(), Toast.LENGTH_LONG).show();
                        FirebaseDatabase.getInstance().getReference("User Information").child(campainModel.getUid())
                                .child("balance").setValue(up_bal);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void checkStatus(CampainModel campainModel){
        if(campainModel.getStatus().equals("Pending")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("✔ Are you want approve or reject this request ??");
            builder.setTitle("✔ Approval and Reject confirmation ");


            builder.setPositiveButton("✔ Approve", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    approveUpdate(campainModel);
//                Intent intent=new Intent(context, MainActivity.class);
//                context.startActivity(intent);
//                ((Activity) context).finish();



                    dialog.cancel();

                }
            });
            builder.setNegativeButton("❌ Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    FirebaseDatabase.getInstance().getReference("Link Details").
                            child(campainModel.getPostKey()).child("status")
                            .setValue("Reject");
                    FirebaseDatabase.getInstance().getReference("Link Details").
                            child(campainModel.getPostKey()).child("backColor")
                            .setValue("#FF0000");

                    FirebaseDatabase.getInstance().getReference("Specefic User Details").
                            child(campainModel.getUid()).
                            child(campainModel.getPostKey()).child("status")
                            .setValue("Reject");
                    FirebaseDatabase.getInstance().getReference("Specefic User Details").
                            child(campainModel.getUid()).
                            child(campainModel.getPostKey()).child("backColor")
                            .setValue("#FF0000");


                    dialog.cancel();
                }
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        if(campainModel.getStatus().equals("Approve")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("❌❌ Are you want  reject this request ??");
            builder.setTitle("❌ Reject confirmation ");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rejectUpdate(campainModel);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }
        if(campainModel.getStatus().equals("Reject")){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("✅✅ Are you want  approve this request ??");
            builder.setTitle("✅ Approve confirmation ");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    approveUpdate(campainModel);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
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
        TextView setAll,copyLink,pending,approve,reject;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            setAll=itemView.findViewById(R.id.all_id);
            copyLink=itemView.findViewById(R.id.copy_link_id);
            pending=itemView.findViewById(R.id.pending_id);




        }
    }
}
