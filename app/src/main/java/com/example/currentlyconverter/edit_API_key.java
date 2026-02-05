package com.example.currentlyconverter;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class edit_API_key extends AppCompatActivity {
    private EditText apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_api_key);

        apiKey = findViewById(R.id.ApiKey);
        MaterialButton sendAPI = findViewById(R.id.buttonSendAPI);
        sendAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Log.i("MyLogs", "API отправлено");
                String inputKey = apiKey.getText().toString();

                if(inputKey==null || inputKey.trim().isEmpty()){
                    Log.i("API", "null key");
                    Toast.makeText(edit_API_key.this, "Введите API ключ", Toast.LENGTH_SHORT).show();
                    v.setEnabled(true);
                    return;
                }

                if(inputKey.length() != 32){
                    Log.i("API", "not valid key (!=32)");
                    Toast.makeText(edit_API_key.this, "Ключ недействителен", Toast.LENGTH_SHORT).show();
                    v.setEnabled(true);
                    return;
                }

                String key = "app_id=" + inputKey;
                final View button = v;

                try{
                    checkApiKey(key, new checkApiKeyCallback() {
                        @Override
                        public void onResult(boolean isValid) {
                            runOnUiThread(() -> {
                                if (isValid){
                                    Log.i("API", "intent key API to Start Window");
                                    Intent intent = new Intent(edit_API_key.this, StartActivity.class);
                                    intent.putExtra("KEY", key);
                                    Log.i("API", "Start Window");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.i("API", "isValid is false");
                                    button.setEnabled(true);
                                    Toast.makeText(edit_API_key.this, "Ключ не подошёл", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    Log.e("API", "Error in check and intent key API", e);
                    v.setEnabled(true);
                    Toast.makeText(edit_API_key.this, "Произошла ошибка" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private interface checkApiKeyCallback {
        void onResult(boolean isValid);
    }

    private void checkApiKey(String key, checkApiKeyCallback callback) {
        String query = "https://openexchangerates.org/api/latest.json?";
        String baseCurrency = "&base=";
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(query + key + baseCurrency + "USD" + "&symbols=USD&prettyprint=0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("API", "html call error", e);
                runOnUiThread(() -> {
                    if (callback != null) {
                        callback.onResult(false);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                boolean isValid = false;
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.i("API", "Good response");
                        String json = response.body().string();
                        isValid = !json.contains("\"error\":") && !json.contains("Invalid");
                    } else {
                        Log.i("API", "Bad response");
                    }
                } catch (Exception e) {
                    Log.e("API", "error response", e);
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
                final boolean responseIsValid = isValid;
                runOnUiThread(() -> {
                    if (callback != null) {
                        callback.onResult(responseIsValid);
                    }
                });
            }
        });
    }
}