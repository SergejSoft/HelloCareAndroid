package com.hellocare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hellocare.JobsAdapter;
import com.hellocare.MainActivity;
import com.hellocare.R;
import com.hellocare.model.Job;
import com.hellocare.model.PaymentType;
import com.hellocare.model.ServiceType;
import com.hellocare.model.StatusType;
import com.hellocare.network.ApiFacade;
import com.hellocare.util.FormatUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JobDetailsActivity extends AppCompatActivity {
    public TextView dateAndAddress;
    public TextView duration;
    public TextView distance;

    public TextView client;
    public LinearLayout servicesLayout;
private View accept, decline, refresh, goToMap;

    TextView price, description, phone;
    private TextView timeStart, dateStart, dateFinish;
    private TextView timeFinish;
    private TextView fullAddress;

    private void fetchJobDetails(int id) {

        ApiFacade.getInstance().getApiService().getJobById(id).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                populateJob(response.body());
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setIcon(R.drawable.logo_red);
        
        phone = (TextView) findViewById(R.id.phone);
        description = (TextView) findViewById(R.id.description);
        fullAddress = (TextView) findViewById(R.id.full_address);
        dateAndAddress = (TextView) findViewById(R.id.date_address);
        distance = (TextView) findViewById(R.id.distance);
        duration = (TextView) findViewById(R.id.duration);
        price = (TextView) findViewById(R.id.price);
        client = (TextView) findViewById(R.id.client);
        dateStart = (TextView) findViewById(R.id.date_start);
        timeStart = (TextView) findViewById(R.id.time_start);
        dateFinish = (TextView) findViewById(R.id.date_finish);
        timeFinish = (TextView) findViewById(R.id.time_finish);
        servicesLayout = (LinearLayout) findViewById(R.id.services_layout);
accept =findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        refresh = findViewById(R.id.refresh);
        goToMap = findViewById(R.id.show_map);
        final int id = getIntent().getIntExtra(Statics.EXTRA_ID, 0);

       // fetchJobDetails(id);

        Job job = (Job) getIntent().getSerializableExtra(Statics.EXTRA_JOB);

        populateJob(job);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptJob(id);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineJob(id);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchJobDetails(id);
            }
        });
        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);

                startActivity(intent);

            }
        });

    }

    private void acceptJob(int id) {
        ApiFacade.getInstance().getApiService().acceptJob(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void declineJob(int id) {
        ApiFacade.getInstance().getApiService().declineJob(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void populateJob(Job job) {
        dateAndAddress.setText(FormatUtils.convertTimestamp(job.dates[0].starts_at,FormatUtils.PATTERN_DATE) +
                " " + FormatUtils.convertTimestamp(job.dates[0].ends_at,FormatUtils.PATTERN_DATE) + " " + job.location.full_address);
        dateStart.setText(FormatUtils.convertTimestamp(job.dates[0].starts_at,FormatUtils.PATTERN_DATE) );
        timeStart.setText(FormatUtils.convertTimestamp(job.dates[0].starts_at,FormatUtils.PATTERN_TIME) );
        dateFinish.setText(FormatUtils.convertTimestamp(job.dates[0].ends_at,FormatUtils.PATTERN_DATE) );
        timeFinish.setText(FormatUtils.convertTimestamp(job.dates[0].ends_at,FormatUtils.PATTERN_TIME) );
        distance.setText(job.confirmation + "");
        duration.setText(job.dates[0].hours + "");
        phone.setText(job.client.phone);
        client.setText(Arrays.toString(job.patients).replace("[","").replace("]",""));
        client.setVisibility(job.confirmation?View.VISIBLE:View.GONE);
        phone.setVisibility(job.confirmation?View.VISIBLE:View.GONE);
        description.setText(job.description);
        fullAddress.setText(job.confirmation?job.location.full_address:job.location.secret_address);

        price.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this,
                PaymentType.fromValue(job.payment_method).getDrawableResId()), null, null, null);
        price.setText(job.amount + " " + job.currency);


        servicesLayout.removeAllViewsInLayout();
        for (int i = 0; i < job.services.length; i++) {
            ImageView imageView = new ImageView(servicesLayout.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(54, 54);
            layoutParams.setMargins(6, 6, 6, 6);

            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(ServiceType.fromValue(job.services[i].name).getDrawableResId());

            servicesLayout.addView(imageView);
        }
        accept.setVisibility(job.confirmation? View.GONE:View.VISIBLE);
        decline.setVisibility(job.confirmation? View.GONE:View.VISIBLE);

    }


}
