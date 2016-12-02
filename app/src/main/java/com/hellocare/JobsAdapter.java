package com.hellocare;

import android.content.Intent;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hellocare.activity.JobDetailsActivity;
import com.hellocare.activity.Statics;
import com.hellocare.model.Job;
import com.hellocare.model.PaymentType;
import com.hellocare.model.ServiceType;
import com.hellocare.util.FormatUtils;

import java.util.Arrays;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
    private final Job[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView dateAndAddress;
        public final TextView duration;
        public final TextView distance;
        public final TextView price;
        public final TextView client;
        public LinearLayout servicesLayout;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            dateAndAddress = (TextView) itemLayoutView.findViewById(R.id.date_address);
            duration = (TextView) itemLayoutView.findViewById(R.id.duration);
            distance = (TextView) itemLayoutView.findViewById(R.id.distance);
            price = (TextView) itemLayoutView.findViewById(R.id.price);
            client = (TextView) itemLayoutView.findViewById(R.id.client);
            servicesLayout = (LinearLayout) itemLayoutView.findViewById(R.id.services_layout);
        }


    }

    public JobsAdapter(Job[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String dateAdress = "<b>" + FormatUtils.timestampToProperString(holder.client.getContext(),
                mDataset[position].dates[0].starts_at) + "</b>" +" "+
                (mDataset[position].confirmation? mDataset[position].location.full_address: mDataset[position].location.secret_address) ;
        holder.dateAndAddress.setText(Html.fromHtml(dateAdress));
        if (SettingManager.getInstance().getCurrentLocation()!=null){
            Location location = new Location("jobLocation");

            location.setLatitude(mDataset[position].location.lat);
            location.setLongitude(mDataset[position].location.lng);

            float distance = SettingManager.getInstance().getCurrentLocation().distanceTo(location);
        holder.distance.setText(Math.round(distance/1000 )+" "+holder.distance.getContext().getString(R.string.km));
        }
        else {holder.distance.setVisibility(View.GONE);}
        holder.duration.setText("("+FormatUtils.formatDecimal(mDataset[position].dates[0].hours)+" "+
                holder.duration.getContext().getString(R.string.hours)+")");
        holder.price.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(holder.price.getContext(),
                PaymentType.fromValue(mDataset[position].payment_method).getDrawableResId()), null,null,null);
        String priceString = "<b>" + FormatUtils.formatCurrency(mDataset[position].amount, mDataset[position].currency)+", " +
                "</b>"+FormatUtils.formatCurrency(mDataset[position].hourly_rate,
                mDataset[position].currency)+"/"+holder.client.getContext().getString(R.string.hour)

              ;
        holder.price.setText(Html.fromHtml(priceString));
        String clientString = "<b>" +  holder.client.getContext().getString(R.string.client) + ": "+"</b>" +
                Arrays.toString(mDataset[position].patients).replace("[","").replace("]","");
        holder.client.setText(Html.fromHtml(clientString));

        holder.client.setVisibility(mDataset[position].confirmation?View.VISIBLE:View.GONE);

                            holder.servicesLayout.removeAllViewsInLayout();
                            for (int i = 0; i < mDataset[position].services.length; i++) {
                                ImageView imageView = new ImageView (holder.servicesLayout.getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(58, 58);

                                imageView.setImageResource(ServiceType.fromValue(mDataset[position].services[i].name).getDrawableResId());
                                layoutParams.setMargins(8, 8, 8, 8);

                                imageView.setLayoutParams(layoutParams);
                                holder.servicesLayout.addView(imageView);
                            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), JobDetailsActivity.class);
                intent.putExtra(Statics.EXTRA_ID, mDataset[position].id);
                intent.putExtra(Statics.EXTRA_JOB, mDataset[position]);
                v.getContext().startActivity(intent);
            }
        });

        /*: [{"id":2,"created_at":"2016-11-17T12:52:39+01:00",
        "amount":"228.0","currency":"EUR","hourly_rate":"28.5",
        "duration":8.0,"payment_method":"cash","status":"new",
        "confirmation":false,"flexible":false,"description":"",
        "services":[{"name":"cooking"},{"name":"shopping"},{"name":"support"}],
        "client":{"first_name":"Maite","last_name":"Lancaster","phone":"+632-21-1460277",
        "email":"iamdeadgeist+1@gmail.com"},

        "dates":[
        {"starts_at":"2016-12-10T10:30:00+01:00","ends_at":"2016-12-10T12:30:00+01:00","hours":2.0},
        {"starts_at":"2016-12-08T12:30:00+01:00", "ends_at":"2016-12-08T14:30:00+01:00","hours":2.0},
        {"starts_at":"2016-12-08T09:00:00+01:00","ends_at":"2016-12-08T11:00:00+01:00","hours":2.0},
        {"starts_at":"2016-12-07T07:30:00+01:00","ends_at":"2016-12-07T09:30:00+01:00","hours":2.0}]

        ,"location":{"full_address":"Odentaler 13 Köln 51069  DE","secret_address":"Köln 51069","zipcode":51069,"lat":51.0011483,"lng":7.0425768},"patients":[{"first_name":"Geist","last_name":"Wargeist","gender":"male","age":50}]},{"id":2,"created_at":"2016-11-17T12:52:39+01:00","amount":"228.0","currency":"EUR","hourly_rate":"28.5","duration":8.0,"payment_method":"cash","status":"new","confirmation":false,"flexible":false,"description":"","services":[{"name":"cooking"},{"name":"shopping"},{"name":"support"}],"client":{"first_name":"Maite","last_name":"Lancaster","phone":"+632-21-1460277","email":"iamdeadgeist+1@gmail.com"},"dates":[{"starts_at":"2016-12-10T10:30:00+01:00","ends_at":"2016-12-10T12:30:00+01:00","hours":2.0},{"starts_at":"2016-12-08T12:30:00+01:00","ends_at":"2016-12-08T14:30:00+01:00","hours":2.0},{"starts_at":"2016-12-08T09:00:00+01:00","ends_at":"2016-12-08T11:00:00+01:00","hours":2.0},{"starts_at":"2016-12-07T07:30:00+01:00","ends_at":"2016-12-07T09:30:00+01:00","hours":2.0}],"location":{"full_address":"Odentaler 13 Köln 51069  DE","secret_address":"Köln 51069","zipcode":51069,"lat":51.0011483,"lng":7.0425768},"patients":[{"first_name":"Geist","last_name":"Wargeist","gender":"male","age":50}]},{"id":2,"created_at":"2016-11-17T12:52:39+01:00","amount":"228.0","currency":"EUR","hourly_rate":"28.5","duration":8.0,"payment_method":"cash","status":"new","confirmation":false,"flexible":false,"description":"","services":[{"name":"cooking"},{"name":"shopping"},{"name":"support"}],"client":{"first_name":"Maite","last_name":"Lancaster","phone":"+632-21-1460277","email":"iamdeadgeist+1@gmail.com"},"dates":[{"starts_at":"2016-12-10T10:30:00+01:00","ends_at":"2016-12-10T12:30:00+01:00","hours":2.0},{"starts_at":"2016-12-08T12:30:00+01:00","ends_at":"2016-12-08T14:30:00+01:00","hours":2.0},{"starts_at":"2016-12-08T09:00:00+01:00","ends_at":"2016-12-08T11:00:00+01:00","hours":2.0},{"starts_at":"2016-12-07T07:30:00+01:00","ends_at":"2016-12-07T09:30:00+01:00","hours":2.0}],"location":{"full_address":"Odentaler 13 Köln 51069  DE","secret_address":"Köln 51069","zipcode":51069,"lat":51.0011483,"lng":7.0425768},"patients":[{"first_name":"Geist","last_name":"Wargeist","gender":"male","age":50}]},{"id":2,"created_at":"2016-11-17T12:52:39+01:00","amount":"228.0","currency":"EUR","hourly_rate":"28.5","duration":8.0,"payment_method":"cash","status":"new","confirmation":false,"flexible":false,"description":"","services":[{"name":"cooking"},{"name":"shopping"},{"name":"support"}],"client":{"first_name":"Maite","last_name":"Lancaster","phone":"+632-21-1460277","email":"iamdeadgeist+1@gmail.com"},"dates":[{"starts_at":"2016-12-10T10:30:00+01:00","ends_at":"2016-12-10T12:30:00+01:00","hours":2.0},{"starts_at":"2016-12-08T12:30:00+01:00","ends_at":"2016-12-08T14:30:00+01:00","hours":2.0},{"starts_at":"2016-12-08T09:00:00+01:00","ends_at":"2016-12-08T11:00:00+01:00","hours":2.0},{"starts_at":"2016-12-07T07:30:00+01:00","ends_at":"2016-12-07T09:30:00+01:00","hours":2.0}],"location":{"full_address":"Odentaler 13 Köln 51069  DE","secret_address":"Köln 51069","zipcode":51069,"lat":51.0011483,"lng":7.0425768},"patients":[{"first_name":"Geist","last_name":"Wargeist","gender":"male","age":5
11-29 18:08:37.776 19168-11907/com.hellocare D/OkHttp: 0}]},

{"id":3,"created_at":"2016-11-21T13:58:11+01:00",
"amount":"114.0",
"currency":"EUR","hourly_rate":"28.5","duration":4.0,"payment_method":"cash","status":"new","confirmation":true,"flexible":true,
"description":"","services":[{"name":"cooking"},{"name":"shopping"},{"name":"support"}],
"client":{"first_name":"Maite","last_name":"Lancaster","phone":"+632-21-1460277","email":"iamdeadgeist+1@gmail.com"},

"dates":[{"starts_at":"2016-12-02T12:30:00+01:00","ends_at":"2016-12-02T14:30:00+01:00","hours":2.0},
{"starts_at":"2016-11-30T09:00:00+01:00","ends_at":"2016-11-30T11:00:00+01:00","hours":2.0}]

,"location":{"full_address":"Odentaler 13 Köln 51069  DE","secret_address":"Köln 51069","zipcode":51069,"lat":51.0011483,"lng":7.0425768},"patients":[{"first_name":"Geist","last_name":"Wargeist","gender":"male","age":50}]},{"id":3,"created_at":"2016-11-21T13:58:11+01:00","amount":"114.0","currency":"EUR","hourly_rate":"28.5","duration":4.0,"payment_method":"cash","status":"new","confirmation":true,"flexible":true,"description":"","services":[{"name":"cooking"},{"name":"shopping"},{"name":"support"}],"client":{"first_name":"Maite","last_name":"Lancaster","phone":"+632-21-1460277","email":"iamdeadgeist+1@gmail.com"},"dates":[{"starts_at":"2016-12-02T12:30:00+01:00","ends_at":"2016-12-02T14:30:00+01:00","hours":2.0},{"starts_at":"2016-11-30T09:00:00+01:00","ends_at":"2016-11-30T11:00:00+01:00","hours":2.0}],"location":{"full_address":"Odentaler 13 Köln 51069  DE","secret_address":"Köln 51069","zipcode":51069,"lat":51.0011483,"lng":7.0425768},"patients":[{"first_name":"Geist","last_name":"Wargeist","gender":"male","age":50}]}]
11-29 18:08:37.776 19168-11907/com.hellocare D/OkHttp: <-- END HTTP*/

    }


    // Return the size of dataset
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    public Job getItemAtPosition(int pos) {
        return mDataset[pos];
    }


}