package com.example.montana.hackathonproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class fb_login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private Button login_button;
    private ProfileTracker profileTracker;
    private TextView text_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_login);

        //Facebook Login
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        login_button = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile != null)
                {
                    String name = currentProfile.getFirstName() + " " + currentProfile.getLastName();
                    text_name.setText(name);
                    text_name.setVisibility(View.VISIBLE);
                }else
                    text_name.setVisibility(View.INVISIBLE);
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.d("FacebookLogin","Logged In Successfully");

                        if (Profile.getCurrentProfile() != null)
                        {
                            String name = Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName();
                            //text_name.setText(name);
                            //text_name.setVisibility(View.VISIBLE);
                            startActivity(new Intent(getApplicationContext(),Profilestats.class));
                        }else
                            text_name.setVisibility(View.INVISIBLE);
                    }



                    @Override
                    public void onCancel() {
                        Log.d("FacebookLogin","Logged In Cancelled");
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        Log.d("FacebookLogin",exception.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

}
