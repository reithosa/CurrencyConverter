package com.example.currentlyconverter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<DataClass> dataList;

    interface OnCurrencyClickListener{
        void OnCurrencyClick(DataClass dataClass, int position);
    }
    private final OnCurrencyClickListener onClickListener;
    public MyAdapter(List<DataClass> dataList, OnCurrencyClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass dataClass = dataList.get(position);
        holder.imageForCurrency.setImageResource(dataList.get(position).getImageForCurrency());
        holder.currencyName.setText(dataList.get(position).getCurrencyName());
        holder.currencyValue.setText(Double.toString(dataList.get(position).getCurrencyValue()));
        holder.currencySourceValue.setText("1");
        holder.currencySourceName.setText(dataList.get(position).getCurrencyName());
        holder.currencyTargetName.setText(dataList.get(position).getCurrencyTargetName());
        holder.currencyTargetValue.setText(Double.toString(dataList.get(position).getCurrencyTargetValue()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.OnCurrencyClick(dataClass, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageForCurrency;
        TextView currencyName, currencyValue, currencySourceName, currencySourceValue, currencyTargetName, currencyTargetValue;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            currencyName = itemView.findViewById(R.id.currencyName);
            currencyValue = itemView.findViewById(R.id.currencyValue);
            currencySourceName = itemView.findViewById(R.id.currencySourceName);
            currencySourceValue = itemView.findViewById(R.id.currencySourceValue);
            currencyTargetName = itemView.findViewById(R.id.currencyTargetName);
            currencyTargetValue = itemView.findViewById(R.id.currencyTargetValue);
            imageForCurrency = itemView.findViewById(R.id.imageForCurrency);
        }
    }
}

