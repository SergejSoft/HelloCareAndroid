package com.hellocare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hellocare.MainActivity;
import com.hellocare.R;
import com.hellocare.SettingManager;
import com.hellocare.util.ValidityUtils;
import com.hellocare.model.AuthResult;
import com.hellocare.network.ApiFacade;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText email, pass;
    private TextInputLayout email_txt, pass_txt;
    private Button sign_up;
    private CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email_txt = (TextInputLayout) findViewById(R.id.input_layout_email);
        pass_txt = (TextInputLayout) findViewById(R.id.input_layout_password);

        // Edittext
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        // Button
        sign_up = (Button) findViewById(R.id.signup);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmailField()) {
                    return;
                }
                if (pass.getText().toString().length() == 0) {
                    pass_txt.setError(getString(R.string.password_required));
                    return;
                }
                SettingManager.getInstance().createTempTokenFromLoginPassword(email.getText().toString(), pass.getText().toString());
                SettingManager.getInstance().saveValue(SettingManager.TOKEN_TYPE, SettingManager.TOKEN_TYPE_HELLOCARE);
                signIn();
            }
        });
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
    }

    private void signIn() {
        ApiFacade.getInstance().getApiService().loginUser().enqueue(new Callback<AuthResult>() {

            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                Log.d(TAG, response.code() + "");
                if (response.code() == 201) {
                    SettingManager.getInstance().saveValue(SettingManager.TOKEN, response.body().authentication_token);
                    Intent goToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(goToMainActivity);
                    finish();

                } else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(LoginActivity.this);
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

    private boolean validateEmailField() {
        if (email.getText().toString().length() == 0) {
            email_txt.setError(getString(R.string.email_required));
            return false;
        }
        if (!ValidityUtils.validateEmail(email.getText().toString())) {
            email_txt.setError(getString(R.string.email_invalid));
            return false;
        }
        email_txt.setError(null);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

