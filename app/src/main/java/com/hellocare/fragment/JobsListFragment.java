package com.hellocare.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hellocare.JobsAdapter;
import com.hellocare.R;
import com.hellocare.model.Job;
import com.hellocare.model.StatusType;
import com.hellocare.network.ApiFacade;
import com.hellocare.util.ResourceUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 22/11/2016.
 */

public class JobsListFragment extends Fragment {
    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    private RecyclerView mRecyclerView;
    private JobsAdapter mAdapter;
    private StatusType type;

    public JobsListFragment() {

    }

    public static JobsListFragment newInstance(StatusType statusType) {
        JobsListFragment fragment = new JobsListFragment();
        Bundle bundle = new Bundle();

        bundle.putString(EXTRA_TYPE, statusType.toString());
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            Log.d("MyFragment "+getArguments().getString(EXTRA_TYPE), "Fragment is visible.");
        Log.d("==Ela==",getArguments().getString(EXTRA_TYPE));
        StatusType type = StatusType.fromValue(getArguments().getString(EXTRA_TYPE));
        fetchJobs(type);}

        else
            Log.d("MyFragment", "Fragment is not visible.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.jobs_list_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getActivity()));
        Log.d("==Ela==",getArguments().getString(EXTRA_TYPE));
        StatusType type = StatusType.fromValue(getArguments().getString(EXTRA_TYPE));
        fetchJobs(type);
        return rootView;
    }

    private void fetchJobs(StatusType statusType) {

        ApiFacade.getInstance().getApiService().
                getAllJobs(statusType).enqueue(new Callback<Job[]>() {
            @Override
            public void onResponse(Call<Job[]> call, Response<Job[]> response) {
                mAdapter = new JobsAdapter(response.body());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Job[]> call, Throwable t) {

            }
        });

    }

}
