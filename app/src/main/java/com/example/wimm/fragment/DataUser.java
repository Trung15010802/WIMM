package com.example.wimm.fragment;

public class DataUser {
    private String date;
    UserList list;

    public DataUser() {
    }

    public DataUser(String date, UserList list) {
        this.date = date;
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserList getList() {
        return list;
    }

    public void setList(UserList list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DataUser{" +
                "date='" + date + '\'' +
                ", list=" + list +
                '}';
    }
}
