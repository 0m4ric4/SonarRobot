package com.omarica.sonarrobot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    SonarView mSonarView; // A variable to represent the SonarView
    JoystickView mJoystickView;
    int screenHeight;
    int screenWidth;
    float rectHeight;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSonarView = findViewById(R.id.sonarView); // Initializing the SonarView object
        mJoystickView = findViewById(R.id.joystickView);
        // Getting screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        rectHeight = (float) (0.35 * screenHeight);
        mSonarView.getLayoutParams().height = (int) (0.35 * screenHeight); // Setting the SonarView's height to 40% of the screen height
        mJoystickView.getLayoutParams().height = (int) (0.50 * screenHeight);
        mJoystickView.getLayoutParams().width = (int) (0.50 * screenHeight);


        mDatabase = FirebaseDatabase.getInstance(); // Firebase database object
        myRef = mDatabase.getReference(); // Firebase database reference


        mJoystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                myRef.child("joystickAngle").setValue(angle);
                myRef.child("joystickStrength").setValue(strength);

            }
        });




    }
}
