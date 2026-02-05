package com.example.currentlyconverter;

import android.media.Image;

public class DataClass {
    private int imageForCurrency;
    private String currencyName;
    private String currencyTargetName;
    private double currencyTargetValue;
    private double currencyValue;

    public DataClass(String currencyName, String currencyTargetName, double currencyTargetValue, double currencyValue, int imageForCurrency) {
        this.imageForCurrency = imageForCurrency;
        this.currencyName = currencyName;
        this.currencyTargetName = currencyTargetName;
        this.currencyTargetValue = currencyTargetValue;
        this.currencyValue = currencyValue;
        this.imageForCurrency = imageForCurrency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyTargetName() {
        return currencyTargetName;
    }

    public double getCurrencyTargetValue() {
        return currencyTargetValue;
    }

    public double getCurrencyValue() {
        return currencyValue;
    }

    public int getImageForCurrency() {
        return imageForCurrency;
    }
}
