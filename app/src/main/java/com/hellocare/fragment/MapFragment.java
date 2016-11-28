package com.hellocare.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.hellocare.R;
import com.hellocare.activity.JobDetailsActivity;
import com.hellocare.activity.Statics;
import com.hellocare.model.Job;
import com.hellocare.model.PaymentType;
import com.hellocare.model.ServiceType;
import com.hellocare.model.StatusType;
import com.hellocare.network.ApiFacade;
import com.hellocare.util.FormatUtils;
import com.hellocare.util.ResourceUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by elvira on 5/22/2016.
 */
public class MapFragment extends Fragment {
    private GoogleMap mMap;
    private LatLng myPosition;
    SupportMapFragment mapFragment;
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(MapFragment.this.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapFragment.this.getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                ApiFacade.getInstance().getApiService().
                        getAllJobs(StatusType.DEFAULT).enqueue(new Callback<Job[]>() {
                    @Override
                    public void onResponse(Call<Job[]> call, Response<Job[]> response) {
                        ArrayList<Marker> markers = new ArrayList<Marker>();
                        for (Job issue : response.body()) {
                            LatLng issueLatLng = new LatLng(issue.location.lat, issue.location.lng);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(issueLatLng)
                                    .title("Today").snippet(issue.location.secret_address)
                                    .icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.
                                            getBitmap(
                                                    MapFragment.this.getActivity(),
                                                    issue.confirmation ?
                                                            R.drawable.map_pin_accepted : R.drawable.map_pin_assigned))));
                            ;
                            marker.setTag(issue);
                            markers.add(marker);

                        }
                        LatLngBounds.Builder b = new LatLngBounds.Builder();
                        for (Marker m : markers) {
                            b.include(m.getPosition());
                        }
                        LatLngBounds bounds = b.build();

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 70);
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent intent = new Intent(getContext(), JobDetailsActivity.class);
                                intent.putExtra(Statics.EXTRA_ID, ((Job) marker.getTag()).id);
                                intent.putExtra(Statics.EXTRA_JOB, ((Job) marker.getTag()));
                                getContext().startActivity(intent);
                            }
                        });
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(final Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(final Marker marker) {

                                View info = inflater.inflate(R.layout.job_card, null);

                                //  info.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(16, 16, 16, 16);
                                Job job = (Job) marker.getTag();
                                info.setLayoutParams(layoutParams);
                                TextView dateAndAddress = (TextView) info.findViewById(R.id.date_address);
                                TextView distance = (TextView) info.findViewById(R.id.distance);
                                TextView duration = (TextView) info.findViewById(R.id.duration);
                                TextView price = (TextView) info.findViewById(R.id.price);
                                TextView client = (TextView) info.findViewById(R.id.client);
                                LinearLayout servicesLayout = (LinearLayout) info.findViewById(R.id.services_layout);
                                dateAndAddress.setText(FormatUtils.convertTimestamp(job.dates[0].starts_at,FormatUtils.PATTERN_DATE) +
                                        " " + FormatUtils.convertTimestamp(job.dates[0].ends_at,FormatUtils.PATTERN_DATE) + " " + job.location.full_address);
                                distance.setText(job.confirmation + "");
                                duration.setText(job.dates[0].hours + "");
                                price.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(),
                                        PaymentType.fromValue(job.payment_method).getDrawableResId()), null, null, null);
                                price.setText(job.amount + " " + job.currency);
                                client.setText(Arrays.toString(job.patients).replace("[","").replace("]",""));
                                client.setVisibility(job.confirmation?View.VISIBLE:View.GONE);
                                servicesLayout.removeAllViewsInLayout();
                                for (int i = 0; i < job.services.length; i++) {
                                    ImageView imageView = new ImageView(servicesLayout.getContext());
                                    LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(54, 54);
                                    layoutParams.setMargins(6, 6, 6, 6);

                                    imageView.setLayoutParams(imageLayoutParams);
                                    imageView.setImageResource(ServiceType.fromValue(job.services[i].name).getDrawableResId());

                                    servicesLayout.addView(imageView);
                                }


                                return info;
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Job[]> call, Throwable t) {

                    }
                });

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}