package com.hellocare;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hellocare.fragment.CaldroidSampleCustomFragment;
import com.hellocare.fragment.CalendarFragment;
import com.hellocare.fragment.JobsListFragment;
import com.hellocare.fragment.MapFragment;
import com.hellocare.model.StatusType;
import com.hellocare.network.ApiFacade;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setIcon(R.drawable.logo_red);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.group_3).setText("");
        tabLayout.getTabAt(1).setIcon(R.drawable.group_2).setText("");
        tabLayout.getTabAt(2).setIcon(R.drawable.map_pin).setText("");
        tabLayout.getTabAt(3).setIcon(R.drawable.calendar_icon).setText("");

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(JobsListFragment.newInstance(StatusType.ASSIGNED), "ONE");
        adapter.addFragment(JobsListFragment.newInstance(StatusType.ACCEPTED), "TWO");
        adapter.addFragment(new MapFragment(), "THR");
        adapter.addFragment(new CaldroidSampleCustomFragment(), "4rr");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
