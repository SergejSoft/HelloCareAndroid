package com.hellocare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JobLocation implements Serializable {



    @SerializedName("full_address")
    public String full_address;
    @SerializedName("zipcode")
    public int zipcode;
    @SerializedName("secret_address")
    public String secret_address;
    @SerializedName("lat")
    public double lat;
    @SerializedName("lng")
    public double lng;

}
