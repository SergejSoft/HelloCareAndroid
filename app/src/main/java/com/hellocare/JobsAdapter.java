package com.hellocare;

import android.content.Intent;
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
       // holder.distance.setText(mDataset[position].confirmation + "");
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
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(54, 54);
                                layoutParams.setMargins(6, 6, 6, 6);

                                imageView.setLayoutParams(layoutParams);
                                imageView.setImageResource(ServiceType.fromValue(mDataset[position].services[i].name).getDrawableResId());

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