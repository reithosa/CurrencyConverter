package com.example.currentlyconverter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {
    private List<DataForList> items;
    public AdapterList(List<DataForList> items)
    {
        this.items = items;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_of_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataForList item = items.get(position);
        holder.currencyNameList.setText(item.getCurrencyNameList());
        holder.imageCurrencyList.setImageResource(item.getImageCurrencyList());
        holder.itemCheckbox.setChecked(item.isItemCheckbox()); // Установка состояния CheckBox

        holder.itemCheckbox.setOnCheckedChangeListener(null); // Убираем предыдущий слушатель
        holder.itemCheckbox.setChecked(item.isItemCheckbox());
        holder.itemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setItemCheckbox(isChecked); // Обновление состояния элемента в модели данных
        });

    }
    public List<DataForList> getSelectedItems() {
        List<DataForList> selectedItems = new ArrayList<>();
        for (DataForList item : items) {
            if (item.isItemCheckbox()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageCurrencyList;
        TextView currencyNameList;
        CheckBox itemCheckbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCurrencyList = itemView.findViewById(R.id.imageCurrencyList);
            currencyNameList = itemView.findViewById(R.id.currencyNameList);
            itemCheckbox = itemView.findViewById(R.id.itemCheckbox);
        }
    }
}


