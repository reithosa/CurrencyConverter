package com.example.currentlyconverter;

import java.util.HashMap;
import java.util.Map;

public class DataFromAPI {
    public Map<String, Double> getCurrency() {
        return rates;
    }

    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    Map<String, Double> rates;
}
