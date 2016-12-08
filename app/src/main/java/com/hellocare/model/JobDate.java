package com.hellocare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JobDate implements Serializable,Comparable<JobDate> {

    @SerializedName("starts_at")
    public String starts_at;
    @SerializedName("hours")
    public double hours;
    @SerializedName("ends_at")
    public String ends_at;

    @Override
    public String toString() {
        return starts_at + " "+ends_at;
    }

    @Override
    public int compareTo(JobDate jobDate) {
        return starts_at.compareTo(jobDate.starts_at);
    }
}
