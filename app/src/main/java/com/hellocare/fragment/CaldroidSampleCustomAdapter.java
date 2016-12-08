package com.hellocare.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;


import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hellocare.R;
import com.hellocare.model.Job;
import com.hellocare.model.JobDate;
import com.hellocare.model.ServiceType;
import com.hellocare.util.FormatUtils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {

    public CaldroidSampleCustomAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
        boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
        boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
        return (sameDay && sameMonth && sameYear);
        //	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //return fmt.format(date1).equals(fmt.format(date2));
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);
        LinearLayout jobsCount = (LinearLayout) cellView.findViewById(R.id.jobs_count);
        tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);

            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(resources
                    .getColor(R.color.colorPrimaryDark));

            tv1.setTextColor(Color.WHITE);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
               // cellView.setBackgroundColor(cellView.getContext().getResources().getColor(R.color.colorAccent));
                tv1.setTextColor(Color.GRAY);
            } else {
                cellView.setBackgroundResource(R.drawable.calendar_cell);
            }
        }

        tv1.setText("" + dateTime.getDay());

        //JobDate datejob = (JobDate) extraData.get("key");
       // String datejob = "2016-12-10T10:30:00+01:00";
        HashMap<String, ArrayList<Job>> calendarKeys = (HashMap<String, ArrayList<Job>>) extraData.get("extra");
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        try {
            for (String dateDay:calendarKeys.keySet()){
            Date date = sdf.parse(dateDay);

            if (isSameDay(date, FormatUtils.convertDateTimeToDate(dateTime))) {

               drawJobsCount(jobsCount, calendarKeys.get(dateDay).size(), R.drawable.circle);
                cellView.setTag(calendarKeys.get(dateDay));
            }}

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }

    private void drawJobsCount(LinearLayout jobsCount, int count, int drawableResId) {
        jobsCount.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(2, 2, 2, 2);

            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableResId));

            jobsCount.addView(imageView);
        }
    }

}
