package com.example.currentlyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private DrawerLayout drawerLayout;
    AdapterList adapterList;
    MyAdapter adapter;
    List<DataClass> dataList = new ArrayList<>();
    List<DataForList> dataList2 = new ArrayList<>();
    Map<String, Double> currencies;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Инициализация DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, // Строка для открытия
                R.string.navigation_drawer_close // Строка для закрытия
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //список для checkbox recyclerView
        dataList2.add(new DataForList(R.drawable.united_states, "USD", true));
        dataList2.add(new DataForList(R.drawable.united_kingdom, "GBP", true));
        dataList2.add(new DataForList(R.drawable.european_union, "EUR", true));
        dataList2.add(new DataForList(R.drawable.china, "CNY", true));
        dataList2.add(new DataForList(R.drawable.russia, "RUB", true));
        dataList2.add(new DataForList(R.drawable.japan, "JPY", true));
        dataList2.add(new DataForList(R.drawable.switzerland, "CHF", true));
        dataList2.add(new DataForList(R.drawable.canada, "CAD", true));
        dataList2.add(new DataForList(R.drawable.australia, "AUD", true));
        dataList2.add(new DataForList(R.drawable.india, "INR", true));
        dataList2.add(new DataForList(R.drawable.ukraine, "UAH", true));
        dataList2.add(new DataForList(R.drawable.kazakhstan, "KZT", true));

        //Ведущая валюта
        ImageView imageView = findViewById(R.id.MainImageCurrency);
        textView = findViewById(R.id.MainCurrencyName);
        editText = findViewById(R.id.MainInputField);

        if (currencies == null) {
            Log.i("MyLogs", "Загрузка данных в currencies из кэша");
            restoreFromCache();
            Toast.makeText(this, "Загрузка данных...", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("MyLogs", "Данные не загружены в currencies из кэша");
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdateList(editText, textView);
            }
        });


        MyAdapter.OnCurrencyClickListener currencyClickListener = new MyAdapter.OnCurrencyClickListener() {
            @Override
            public void OnCurrencyClick(DataClass dataClass, int position) {
                textView.setText(dataClass.getCurrencyName());
                imageView.setImageResource(dataClass.getImageForCurrency());
                UpdateList(editText, textView);
            }
        };

        //инициализация recyclerView1
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //инициализация adapter1
        adapter = new MyAdapter(dataList, currencyClickListener);
        recyclerView.setAdapter(adapter);

        //инициализация recyclerView2
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        //инициализация adapter2
        adapterList = new AdapterList(dataList2);
        recyclerView2.setAdapter(adapterList);

        restoreFromCache();
        UpdateList(editText, textView);
        //Обновление по нажатию кнопки
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            Log.i("MyLogs", "Нажатие на кнопку");
            restoreFromCache();
            UpdateList(editText, textView);
            Toast.makeText(this, "Добавлено " + currencies.size() + " элементов", Toast.LENGTH_SHORT).show();
        });
    }

    private void restoreFromCache() {
        String jsonData = getIntent().getStringExtra("currency_data");
        Log.i("MyLogs", "Взятие данных из intent");
        if (jsonData == null) {
            Log.i("MyLogs", "intent пуст");
            SharedPreferences prefs = getSharedPreferences("currency-prefs", MODE_PRIVATE);
            jsonData = prefs.getString("last_rates", null);
        }
        if (jsonData != null) {
            Log.i("MyLogs", "jsonData заполнен");
            try {
                Gson gson = new Gson();
                DataFromAPI data = gson.fromJson(jsonData, DataFromAPI.class);
                currencies = data.getCurrency();
                UpdateList(editText, textView);
            } catch (Exception e) {
                Log.e("MyLogs", "Ошибка парсинга JSON", e);
            }
        } else {
            Log.i("MyLogs", "Нет доступных данных в кэше");
        }
    }

    private void UpdateList(EditText editText, TextView textView) {
        dataList.clear();
        List<DataForList> selectedItems = adapterList.getSelectedItems(); // Получаем выбранные элементы

        if (editText.getText().toString().isEmpty()) {
            editText.setText("1");
            return;
        }

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Нет выбранных валют", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currencies == null) {
            Toast.makeText(this, "Данные о валютах не загружены", Toast.LENGTH_SHORT).show();
            return;
        }

        String p = editText.getText().toString();
        if (p.isEmpty()) {
            Toast.makeText(this, "Введите сумму", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            for (DataForList item : selectedItems) {
                String currencyCode = item.getCurrencyNameList();
                Double exchangeRate = currencies.get(currencyCode);
                Double amount = 0d;
                if (exchangeRate != null) {
                    Double mainCurrencyRate = currencies.get(textView.getText().toString());
                    if (textView.getText().toString().equals("USD")) {
                        amount = exchangeRate * Double.parseDouble(p);
                        exchangeRate = 1 / exchangeRate;
                    }
                    else {
                        amount = exchangeRate * Double.parseDouble(p) / mainCurrencyRate;
                        exchangeRate = mainCurrencyRate / exchangeRate;
                    }
                    dataList.add(new DataClass(
                            item.getCurrencyNameList(),
                            textView.getText().toString(),
                            Math.floor((exchangeRate) * 10000) / 10000,
                            Math.floor(amount * 10000) / 10000,
                            item.getImageCurrencyList()));
                } else {
                    Log.e("MyLogs", "Курс для валюты " + currencyCode + " не найден");
                }
            }
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        } catch (NumberFormatException e) {
            runOnUiThread(() -> Toast.makeText(this, "Некорректная сумма", Toast.LENGTH_SHORT).show());
        }
    }
}
