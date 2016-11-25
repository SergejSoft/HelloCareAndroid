package com.hellocare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Client implements Serializable {
    @SerializedName("first_name")
    public String first_name;
    @SerializedName("last_name")
    public String last_name;
    @SerializedName("phone")
    public String phone;
    @SerializedName("email")
    public String email;

}
