package com.example.currentlyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartActivity extends AppCompatActivity {
    private String query = "https://openexchangerates.org/api/latest.json?";
    private String baseCurrency = "&base=";
    private String namesOfCurrency = "&symbols=USD,GBP,EUR,CNY,RUB,JPY,CHF,CAD,AUD,INR,UAH,KZT&prettyprint=0";
        private static final String PREFS_NAME = "currency_prefs";
        private static final String LAST_RATES_KEY = "last_rates";
        private static final String LAST_UPDATE_KEY = "last_update_time";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);
            String key = getIntent().getStringExtra("KEY");
            new Handler().postDelayed(() -> loadInitialData(key), 1500); // Уменьшил задержку
        }

        private void loadInitialData(String key) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String cachedJson = prefs.getString(LAST_RATES_KEY, null);
            long lastUpdate = prefs.getLong(LAST_UPDATE_KEY, 0);

            if(cachedJson != null && System.currentTimeMillis() - lastUpdate <= 86400000) {
                proceedToMain(cachedJson, false);
                return;
            }

            loadFromNetwork(key);
        }

        private void loadFromNetwork(String key) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .cache(new Cache(getCacheDir(), 10 * 1024 * 1024))
                    .build();

            Request request = new Request.Builder()
                    .url(query + key + baseCurrency + "USD" + namesOfCurrency)
                    .header("Cache-Control", "public, max-age=86400")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("Network", "Ошибка загрузки", e);
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    String cachedJson = prefs.getString(LAST_RATES_KEY, null);
                    proceedToMain(cachedJson, false);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        if(response.isSuccessful() && response.body() != null) {
                            String json = response.body().string();
                            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString(LAST_RATES_KEY, json);
                            editor.putLong(LAST_UPDATE_KEY, System.currentTimeMillis());
                            editor.apply();
                            proceedToMain(json, true);
                        } else {
                            throw new IOException("Неудачный ответ: " + response.code());
                        }
                    } catch (Exception e) {
                        Log.e("Network", "Ошибка обработки", e);
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        String cachedJson = prefs.getString(LAST_RATES_KEY, null);
                        proceedToMain(cachedJson, false);
                    }
                }
            });
        }

        private void proceedToMain(String jsonData, boolean isFreshData) {
            Intent intent = new Intent(this, MainActivity.class);
            if(jsonData != null) {
                intent.putExtra("currency_data", jsonData);
                intent.putExtra("is_fresh", isFreshData);
            }
            startActivity(intent);
            finish();
        }
}