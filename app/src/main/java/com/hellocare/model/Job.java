package com.hellocare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Job implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("amount")
    public double amount;
    @SerializedName("currency")
    public String currency;
    @SerializedName("hourly_rate")
    public double hourly_rate;
    @SerializedName("duration")
    public double duration;
    @SerializedName("payment_method")
    public String payment_method;
    @SerializedName("status")
    public StatusType status;
    @SerializedName("confirmation")
    public boolean confirmation;
    @SerializedName("flexible")
    public boolean flexible;
    @SerializedName("description")
    public String description;
    @SerializedName("services")
    public Service[] services;
    @SerializedName("client")
    public Client client;
    @SerializedName("dates")
    public JobDate[] dates;
    @SerializedName("location")
    public JobLocation location;
    @SerializedName("patients")
    public Patient[] patients;

}
