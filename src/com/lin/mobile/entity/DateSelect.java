package com.lin.mobile.entity;

/**
 * Created by admin on 2017/12/5.
 */

public class DateSelect {
    int year;
    int month;
    int day;
    int type;

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public DateSelect(int year, int month, int day, int type) {
        super();
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
    }
}
