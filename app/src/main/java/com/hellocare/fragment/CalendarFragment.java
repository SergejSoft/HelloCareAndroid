package com.hellocare.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hellocare.JobsTimeAdapter;
import com.hellocare.R;
import com.hellocare.model.Job;
import com.hellocare.model.StatusType;
import com.hellocare.network.ApiFacade;
import com.hellocare.util.FormatUtils;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 22/11/2016.
 */

public class CalendarFragment extends Fragment {
    Date prevSelected = null;
    private HashMap<String, Object> extraData;
    private RecyclerView mRecyclerView;
    private JobsTimeAdapter mAdapter;

    public CalendarFragment() {
        // Required empty public constructor
    }

    CaldroidSampleCustomFragment caldroidFragment;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            //  caldroidFragment.setTextColorForDate(R.color.wh, blueDate);
            //  caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        if (getActivity() == null){return  rootView;}

        caldroidFragment = new CaldroidSampleCustomFragment();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Bundle args = new Bundle();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
// Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {


                ColorDrawable green = new ColorDrawable(Color.GREEN);

                if (prevSelected != null) {
                    caldroidFragment.clearSelectedDate(prevSelected);
                }
                prevSelected = date;
                caldroidFragment.setSelectedDate(date);

                caldroidFragment.refreshView();
                if (view.getTag() != null) {
                    ArrayList<String> items = (ArrayList<String>) view.getTag();

                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter = new JobsTimeAdapter(items);
                    // mAdapter = new JobsAdapter(response.body());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
                // view.setBackground(green);

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = month + "." + year;
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, 2);
                DateTime dt = CalendarHelper.convertDateToDateTime(cal.getTime());

                Log.d("==Ela==", text + " MONT " + dt.getMonth() + "." + dt.getYear());
                if (caldroidFragment.getRightArrowButton() != null) {
                    if (text.equals(dt.getMonth() + "." + dt.getYear())) {
                        caldroidFragment.getRightArrowButton().setVisibility(View.GONE);
                    } else {
                        caldroidFragment.getRightArrowButton().setVisibility(View.VISIBLE);
                    }
                }
                cal = Calendar.getInstance();
                DateTime current = CalendarHelper.convertDateToDateTime(cal.getTime());
                Log.d("==Ela==", text + " CUR " + current.getMonth() + "." + current.getYear());
                if (caldroidFragment.getLeftArrowButton() != null) {
                    if (text.equals(current.getMonth() + "." + current.getYear())) {
                        caldroidFragment.getLeftArrowButton().setVisibility(View.GONE);
                    } else {
                        caldroidFragment.getLeftArrowButton().setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getDateViewPager()!= null) {
                    caldroidFragment.setEnableSwipe(false);
                }

            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
        extraData = new HashMap<String, Object>();
        // extraData.put()
        ApiFacade.getInstance().getApiService().
                getAllJobs(StatusType.ACCEPTED).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.code()==200){



                HashMap<String, ArrayList<String>> calendarKeys = new HashMap<String, ArrayList<String>>();
                for (int i = 0; i < response.body().size(); i++) {
                    Collections.sort(response.body().get(i).dates);
                    for (int j = 0; j < response.body().get(i).dates.size(); j++) {
                        String key = FormatUtils.convertTimestamp(response.body().get(i).dates.get(j).starts_at, FormatUtils.PATTERN_DATE);

                        if (calendarKeys.containsKey(key)) {
                            calendarKeys.get(key).add(response.body().get(i).id + "," + response.body().get(i).dates.get(j).starts_at
                                    + "," + response.body().get(i).dates.get(j).ends_at);
                        } else {
                            ArrayList<String> jobs = new ArrayList<String>();
                            jobs.add(response.body().get(i).id + "," + response.body().get(i).dates.get(j).starts_at
                                    + "," + response.body().get(i).dates.get(j).ends_at);
                            calendarKeys.put(key, jobs);
                        }
                    }

                }

                extraData.put("extra", calendarKeys);
                caldroidFragment.setExtraData(extraData);

                }
                FragmentTransaction t = getChildFragmentManager().beginTransaction();
                t.replace(R.id.calendar_container, caldroidFragment);
                t.commit();
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {

            }
        });


        return rootView;
    }

}
