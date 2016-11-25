package com.hellocare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Service implements Serializable{
    @SerializedName("name")
    public String name;

}
