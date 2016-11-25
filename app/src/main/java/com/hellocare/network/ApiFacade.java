package com.hellocare.network;

import android.util.Log;

import com.hellocare.SettingManager;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFacade {
    private static final String TAG = "ApiFacade";
    private static final String BASE_URL = "http://hellocare-qa.syndicode.co";
    private static ApiFacade instance;

    public ApiEndpointInterface getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);
        return apiService;
    }

    public static ApiFacade getInstance() {
        if (instance == null) {
            instance = new ApiFacade();
        }

        return instance;
    }

    private ApiFacade() {
    }

    private static OkHttpClient getOkHttpClient() {
        try {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();


            builder.addInterceptor(logging);
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.writeTimeout(60, TimeUnit.SECONDS);


            builder.addNetworkInterceptor(
                    new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request request = null;
                            final String token = SettingManager.getInstance().readValue(SettingManager.TOKEN, "");
                            if (token != null && token.length() != 0) {
                                Log.d(TAG, "Token " + token);
                                Request original = chain.request();
                                Request.Builder requestBuilder = original.newBuilder()

                                        .addHeader("X-Auth-Hellocare-Token", token);

                                request = requestBuilder.build();


                            } else {
                                final String temporaryToken = SettingManager.getInstance().readValue(SettingManager.TEMPORARY_TOKEN, "");
                                final String tokenType = SettingManager.getInstance().readValue(SettingManager.TOKEN_TYPE, "");
                                Log.d(TAG, "tempToken " + temporaryToken);
                                Log.d(TAG, "TokenType " + tokenType);
                                Request original = chain.request();
                                Request.Builder requestBuilder = original.newBuilder()

                                        .addHeader("X-Auth-Type", tokenType)
                                        .addHeader("X-Auth-Token", temporaryToken);

                                request = requestBuilder.build();
                            }
                            return chain.proceed(request);
                        }
                    });


            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
