package com.hellocare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Patient implements Serializable {
    @SerializedName("first_name")
    public String first_name;
    @SerializedName("last_name")
    public String last_name;
    @SerializedName("gender")
    public String gender;
    @SerializedName("age")
    public int age;

}
