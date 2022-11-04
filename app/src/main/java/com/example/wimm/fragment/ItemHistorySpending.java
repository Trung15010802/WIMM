package com.example.wimm.fragment;

public class ItemHistorySpending {
    private String imgType;
    private String txtType;
    private double money;

    public ItemHistorySpending(String imgType, String txtType, double money) {
        this.imgType = imgType;
        this.txtType = txtType;
        this.money = money;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getTxtType() {
        return txtType;
    }

    public void setTxtType(String txtType) {
        this.txtType = txtType;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return txtType + " -" + money;
    }
}
