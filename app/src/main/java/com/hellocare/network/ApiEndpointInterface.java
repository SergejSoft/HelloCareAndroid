package com.hellocare.network;


import com.hellocare.model.Job;
import com.hellocare.model.StatusType;
import com.hellocare.model.AuthResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpointInterface {

    String AUTHORIZATION_URL = "/api/auth/authorize";
    String ALL_JOBS_URL = "/api/jobs";
    String JOBS_BY_ID_URL = "api/jobs/{id}";
    String ACCEPT_JOB_URL = "api/jobs/{id}/accept";
    String DECLINE_JOB_URL = "api/jobs/{id}/decline";
    String DEVICES_URL = "/api/devices";
    //authorization
    @POST(AUTHORIZATION_URL)
    Call<AuthResult> loginUser();

    //Operations about jobs
    @GET(ALL_JOBS_URL)
    Call<List<Job>> getAllJobs(@Query("status") StatusType status);

    @GET(JOBS_BY_ID_URL)
    Call<Job> getJobById(@Path("id") int id);

    @PUT(ACCEPT_JOB_URL)
    Call<Object> acceptJob(@Path("id") int id);

    @PUT(DECLINE_JOB_URL)
    Call<Object> declineJob(@Path("id") int id);
    @FormUrlEncoded
    @POST(DEVICES_URL)
    Call<Object> registerDevice(@Field("uuid") String token);
}
