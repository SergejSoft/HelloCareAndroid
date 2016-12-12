package com.hellocare.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Job implements Serializable, Comparable<Job> {
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
    public List<JobDate> dates;
    @SerializedName("location")
    public JobLocation location;
    @SerializedName("patients")
    public Patient[] patients;

    public JobDate getNearestJob(){
        Collections.sort(dates);
        Log.d("==Ela==", dates.toString());
        for (JobDate jobDate :dates){
            try {


             if   (!new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jobDate.starts_at).before(new Date())){
                 return  jobDate;
             }
            } catch (ParseException e) {
                e.printStackTrace();
                return dates.get(0);
            }
        }
        return  dates.get(0);
    }

    @Override
    public int compareTo(Job job) {
        return getNearestJob().compareTo(job.getNearestJob());
    }

}
