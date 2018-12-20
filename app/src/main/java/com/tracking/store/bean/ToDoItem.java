package com.tracking.store.bean;

/**
 * Created by ZASS on 4/24/2018.
 */

public class ToDoItem {

    @com.google.gson.annotations.SerializedName("id")
    public String mId;

    @com.google.gson.annotations.SerializedName("lat")
    public String lat;
    @com.google.gson.annotations.SerializedName("lng")
    public String lng;
    @com.google.gson.annotations.SerializedName("timestamp")
    public String timestamp;
    @com.google.gson.annotations.SerializedName("userid")
    public int userid;
}
