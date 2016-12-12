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

import com.hellocare.activity.JobDetailsActivity;
import com.hellocare.activity.Statics;
import com.hellocare.model.Job;
import com.hellocare.model.PaymentType;
import com.hellocare.model.ServiceType;
import com.hellocare.util.FormatUtils;
import com.hellocare.util.ResourceUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class JobsTimeAdapter extends RecyclerView.Adapter<JobsTimeAdapter.ViewHolder> {
    private final List<String> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView time;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            time = (TextView) itemLayoutView.findViewById(R.id.time);

        }


    }

    public JobsTimeAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public JobsTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String[] parts = mDataset.get(position).split(",");
       final String id = parts[0];
        String startAt = parts[1];
        String endAt = parts[2];
        holder.time.setText(FormatUtils.convertTimestamp(startAt,FormatUtils.PATTERN_TIME)+
        " - "+FormatUtils.convertTimestamp(endAt,FormatUtils.PATTERN_TIME));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), JobDetailsActivity.class);
                intent.putExtra(Statics.EXTRA_ID, Integer.parseInt(id));
              //  intent.putExtra(Statics.EXTRA_JOB, mDataset.get(position));
                v.getContext().startActivity(intent);
            }
        });

    }


    // Return the size of dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public String getItemAtPosition(int pos) {
        return mDataset.get(pos);
    }


}