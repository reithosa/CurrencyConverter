package com.example.currentlyconverter;

public class DataForList {
    private int imageCurrencyList;
    private String currencyNameList;

    private boolean itemCheckbox;
    public DataForList(int imageCurrencyList, String currencyNameList, boolean itemCheckbox) {
        this.imageCurrencyList = imageCurrencyList;
        this.currencyNameList = currencyNameList;
        this.itemCheckbox = itemCheckbox;
    }
    public int getImageCurrencyList() {
        return imageCurrencyList;
    }

    public String getCurrencyNameList() {
        return currencyNameList;
    }
    public boolean isItemCheckbox() {
        return itemCheckbox;
    }
    public void setItemCheckbox(boolean itemCheckbox) {
        this.itemCheckbox = itemCheckbox;
    }
}
