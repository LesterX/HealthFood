package com.example.montana.hackathonproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profilestats extends AppCompatActivity {

    private AnimationDrawable animation;
    String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static int calorieIntake=200;
    static int workoutpoints=1;
    //private Camera camera = null;

/*

    private CallbackManager callbackManager;
    private Button login_button;
    private ProfileTracker profileTracker;
    private TextView text_name;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilestats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imView = (ImageView) findViewById(R.id.avatarImageView);
        imView.setBackgroundResource(R.drawable.animationhappy);
        animation = (AnimationDrawable) imView.getBackground();


        FloatingActionButton camBtn = (FloatingActionButton) findViewById(R.id.addFoodButton);
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        //text_name = findViewById(R.id.text_name);
        //Add workout Button
        FloatingActionButton addBtn= (FloatingActionButton) findViewById(R.id.addWorkoutButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchtoworkout = new Intent(getApplicationContext(),WorkoutTab.class);
                startActivity(switchtoworkout);
            }
        });
        /*//Facebook Login
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
*/

        //new testTask().execute(".");

        //Workout Progress Bar
        TextView status = (TextView)findViewById(R.id.workoutProgressView);
        ProgressBar workoutbar = (ProgressBar) findViewById(R.id.workoutDoneBar);
        workoutpoints+=getIntent().getExtras().getInt("inttoadd");
        workoutbar.setProgress(workoutpoints);
        status.setText(workoutpoints+ "/" + workoutbar.getMax());





        //Food Progress Bar
        TextView status1 = (TextView)findViewById(R.id.foodProgressView);
        ProgressBar foodbar = (ProgressBar) findViewById(R.id.foodIntakeBar);
        calorieIntake+= Math.round(getIntent().getExtras().getFloat("caloriesAdd"));
        status1.setText(calorieIntake+"/"+foodbar.getMax());
        Log.d("MYBUG",String.valueOf(calorieIntake));
        foodbar.incrementProgressBy(calorieIntake);



        //Happiness Level Bar
        ProgressBar happylvl = (ProgressBar)findViewById(R.id.happyLevel);
        Boolean Happy =false;
        Boolean Sick= false;
        Boolean Hungry=false;
        ImageView vw;

        if (foodbar.getProgress()<=1000 && workoutbar.getProgress()<=29){
            Sick=true;
            vw = (ImageView) findViewById(R.id.avatarImageView);
            vw.setBackgroundResource(R.drawable.animationsad);
            happylvl.setProgress(20);
            happylvl.setProgressTintList(ColorStateList.valueOf(Color.RED));

        }
        else if (foodbar.getProgress()<=1000 && workoutbar.getProgress()<=38){
            Hungry=true;
            vw = (ImageView) findViewById(R.id.avatarImageView);
            vw.setBackgroundResource(R.drawable.animationhungry);
            happylvl.setProgress(60);
            happylvl.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        }
        else {
            Happy=true;
            vw = (ImageView) findViewById(R.id.avatarImageView);
            vw.setBackgroundResource(R.drawable.animationhappy);
            happylvl.setProgress(80);
            happylvl.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }

        animation = (AnimationDrawable) vw.getBackground();


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

        // Save a file: path ffree character spriteor use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void enterFoodText(View v){
        Intent intent = new Intent(this,Nutrition.class);
        startActivity(intent);
    }
}
