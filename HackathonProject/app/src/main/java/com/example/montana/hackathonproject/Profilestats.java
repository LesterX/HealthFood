package com.example.montana.hackathonproject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profilestats extends AppCompatActivity {

    private AnimationDrawable animation;
    String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //private Camera camera = null;


    private CallbackManager callbackManager;
    private Button login_button;
    private ProfileTracker profileTracker;
    private TextView text_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilestats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imView = (ImageView) findViewById(R.id.avatarImageView);
        imView.setBackgroundResource(R.drawable.animation);
        animation = (AnimationDrawable) imView.getBackground();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addWorkoutButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton camBtn = (FloatingActionButton) findViewById(R.id.addFoodButton);
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        text_name = findViewById(R.id.text_name);
        //Add workout Button
        FloatingActionButton addBtn= (FloatingActionButton) findViewById(R.id.addWorkoutButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchtoworkout = new Intent(getApplicationContext(),WorkoutTab.class);
                startActivity(switchtoworkout);
            }
        });
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
                            text_name.setText(name);
                            text_name.setVisibility(View.VISIBLE);
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

        //Workout Progress Bar
        ProgressBar workoutbar = (ProgressBar) findViewById(R.id.workoutDoneBar);
        int number = getIntent().getExtras().getInt("inttoadd");
        workoutbar.incrementProgressBy(number);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.animation.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profilestats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //runs when camera button is pressed
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // will capture a picture when user presses button
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("","creating temp file");
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.montana.hackathonproject.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d("",photoURI.toString());
                //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //create the file for image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
