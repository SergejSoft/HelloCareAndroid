package com.hellocare.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hellocare.JobsAdapter;
import com.hellocare.R;
import com.hellocare.model.Job;
import com.hellocare.model.JobDate;
import com.hellocare.model.StatusType;
import com.hellocare.network.ApiFacade;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 22/11/2016.
 */

public class CalendarFragment extends Fragment {
    Date prevSelected = null;
    private HashMap<String, Object> extraData;

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

      caldroidFragment = new CaldroidSampleCustomFragment();

        Bundle args = new Bundle();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
// Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {


                ColorDrawable green = new ColorDrawable(Color.GREEN);

                if (prevSelected!=null){
                    caldroidFragment.clearSelectedDate(prevSelected);}
                prevSelected = date;
caldroidFragment.setSelectedDate(date);

                caldroidFragment.refreshView();
               // view.setBackground(green);

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
       extraData = new HashMap<String, Object>();
       // extraData.put()
        ApiFacade.getInstance().getApiService().
                getAllJobs(StatusType.ACCEPTED).enqueue(new Callback<Job[]>() {
            @Override
            public void onResponse(Call<Job[]> call, Response<Job[]> response) {
                extraData.put("key",response.body()[0].dates[0].starts_at);
            }

            @Override
            public void onFailure(Call<Job[]> call, Throwable t) {

            }
        });
        caldroidFragment.setExtraData(extraData);
//setCustomResourceForDates();
        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.replace(R.id.calendar_container, caldroidFragment);
        t.commit();

        return rootView;
    }

}
