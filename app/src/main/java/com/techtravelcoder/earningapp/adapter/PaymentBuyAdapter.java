package com.techtravelcoder.earningapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.activity.service.ImageActivity;
import com.techtravelcoder.earningapp.model.TrnxModelMain;

import java.util.ArrayList;

public class PaymentBuyAdapter extends RecyclerView.Adapter<PaymentBuyAdapter.NewMyViewHolder> {

    Context context;
    ArrayList<TrnxModelMain>list;

    public PaymentBuyAdapter(Context context, ArrayList<TrnxModelMain> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentBuyAdapter.NewMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.payment_buy_design,parent,false);
        return new NewMyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentBuyAdapter.NewMyViewHolder holder, int position) {
        TrnxModelMain trnxModel=list.get(position);
        holder.someText.setText("⚫️ Payment Number :  "+(position+1)+""+""+"\n⚫️ Details : "+trnxModel.getBalancePoints()+"\n⚫️ Date :  "+trnxModel.getDate()+"\n⚫️ Trnx Id :  "+trnxModel.getTrnxId());
        holder.stat.setText(trnxModel.getStatus());
        holder.stat.setBackgroundColor(Color.parseColor(trnxModel.getPayColor()));

        holder.see_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ImageActivity.class);
                intent.putExtra("imageString",trnxModel.getImage());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewMyViewHolder extends RecyclerView.ViewHolder {
        TextView someText ,stat,image,see_payment;
        public NewMyViewHolder(@NonNull View itemView) {
            super(itemView);
            someText=itemView.findViewById(R.id.some_id);
            stat=itemView.findViewById(R.id.payment_check_id);
            image=itemView.findViewById(R.id.payment_image_id);
            see_payment=itemView.findViewById(R.id.see_payment_id);

        }
    }
}
