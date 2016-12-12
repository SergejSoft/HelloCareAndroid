package com.hellocare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.hellocare.activity.JobDetailsActivity;
import com.hellocare.activity.Statics;
import com.hellocare.fragment.MapFragment;
import com.hellocare.model.Job;
import com.hellocare.model.PaymentType;
import com.hellocare.model.ServiceType;
import com.hellocare.util.FormatUtils;
import com.hellocare.util.ResourceUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
    private final List<Job> mDataset;

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

    public JobsAdapter(List<Job> myDataset) {
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
        Collections.sort( mDataset.get(position).dates);
        String dateAdress = "<b>" + FormatUtils.timestampToProperString(holder.client.getContext(),
                mDataset.get(position).getNearestJob().starts_at) + "</b>" +" "+
                (mDataset.get(position).confirmation?
                        (mDataset.get(position).location==null||mDataset.get(position).location.full_address==null)?"":
                        mDataset.get(position).location.full_address:
                        mDataset.get(position).location.secret_address) ;
        holder.dateAndAddress.setText(Html.fromHtml(dateAdress));
        if (SettingManager.getInstance().getCurrentLocation()!=null){
            Location location = new Location("jobLocation");

            location.setLatitude(mDataset.get(position).location.lat);
            location.setLongitude(mDataset.get(position).location.lng);

            float distance = SettingManager.getInstance().getCurrentLocation().distanceTo(location);
        holder.distance.setText(Math.round(distance/1000 )+"");
        }
        else {holder.distance.setVisibility(View.GONE);}
        holder.duration.setText("("+FormatUtils.formatDecimal(mDataset.get(position).dates.get(0).hours)+" "+
                holder.duration.getContext().getString(R.string.hours)+")");
        holder.price.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(holder.price.getContext(),
                PaymentType.fromValue(mDataset.get(position).payment_method).getDrawableResId()), null,null,null);
        String priceString = "<b>" + FormatUtils.formatCurrency(mDataset.get(position).amount,
                mDataset.get(position).currency)+", " +
                "</b>"+FormatUtils.formatCurrency(mDataset.get(position).hourly_rate,
                mDataset.get(position).currency)+"/"+holder.client.getContext().getString(R.string.hour)

              ;
        holder.price.setText(Html.fromHtml(priceString));
        String clientString = "<b>" +  holder.client.getContext().getString(R.string.client) + ": "+"</b>" +
                Arrays.toString(mDataset.get(position).patients).replace("[","").replace("]","");
        holder.client.setText(Html.fromHtml(clientString));

        holder.client.setVisibility(mDataset.get(position).confirmation?View.VISIBLE:View.GONE);

                            holder.servicesLayout.removeAllViewsInLayout();
                            for (int i = 0; i <mDataset.get(position).services.length; i++) {
                                ImageView imageView = new ImageView (holder.servicesLayout.getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60
                                        , 60);
imageView.setAdjustViewBounds(true);
                                layoutParams.setMargins(8, 8, 8, 8);

                                imageView.setLayoutParams(layoutParams);
                                Bitmap bitmap = ResourceUtil.
                                        getBitmap(
                                                imageView.getContext(),
                                                ServiceType.fromValue(mDataset.get(position).services[i].name).getDrawableResId());
//Convert bitmap to drawabl
                                Drawable drawable = new BitmapDrawable( bitmap);
                                imageView.setImageDrawable(drawable);

                                holder.servicesLayout.addView(imageView);
                            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), JobDetailsActivity.class);
                intent.putExtra(Statics.EXTRA_ID, mDataset.get(position).id);
                intent.putExtra(Statics.EXTRA_JOB, mDataset.get(position));
                v.getContext().startActivity(intent);
            }
        });

    }


    // Return the size of dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public Job getItemAtPosition(int pos) {
        return mDataset.get(pos);
    }


}