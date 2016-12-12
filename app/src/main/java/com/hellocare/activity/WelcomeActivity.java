package com.hellocare.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hellocare.MainActivity;
import com.hellocare.R;
import com.hellocare.SettingManager;
import com.hellocare.model.AuthResult;
import com.hellocare.network.ApiFacade;
import com.viewpagerindicator.CirclePageIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 09/12/2016.
 */

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    private int[] layouts;

    private SettingManager prefManager;
    private Button btnLogin;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private CirclePageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
      /*  prefManager = SettingManager.getInstance();
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
*/


        setContentView(R.layout.activity_welcome);
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        btnLogin = (Button) findViewById(R.id.signup);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            public String TAG ="Welcome";

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: "
                        + loginResult.getAccessToken().getToken());
                SettingManager.getInstance().saveValue(SettingManager.TOKEN_TYPE, SettingManager.TOKEN_TYPE_FACEBOOK);
                SettingManager.getInstance().saveValue(SettingManager.TEMPORARY_TOKEN, loginResult.getAccessToken().getToken());
                signIn();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, exception.getMessage());
            }
        });


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3
             };



        // making notification bar transparent
       // changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        mIndicator = indicator;
        indicator.setViewPager(viewPager);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void signIn() {
        ApiFacade.getInstance().getApiService().loginUser().enqueue(new Callback<AuthResult>() {

            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {

                if (response.code() == 201) {
                    SettingManager.getInstance().saveValue(SettingManager.TOKEN, response.body().authentication_token);
                    FirebaseMessaging.getInstance().subscribeToTopic(response.body().location+"-jobs");
                    Intent goToMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
                    goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(goToMainActivity);
                    finish();

                } else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(WelcomeActivity.this);
                    builder.setTitle(getString(R.string.error));
                    builder.setMessage(response.code() + " " + response.message());
                    builder.setPositiveButton(getString(R.string.ok), null);

                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
                t.printStackTrace();
            }


        });
    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        if (!SettingManager.getInstance().userLoggedIn()) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        } else {
            Intent goToMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
